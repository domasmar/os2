package os2.main.hardware;

import java.util.Random;

/**
 * Realios mašinos atmintis. Vartotojo atmintis [0.. 3071] Supervizorinė
 * atmintis [3072..4098] Puslapių lentelė prasideda nuo 3072 iki 3263. 12 blokų
 * 
 * @author domas
 * 
 */
public class RMMemory {
	private static int SUPERVISOR_MEMORY_SIZE = 1024;
	private static int MEMORY_SIZE = 3072;
	private static int BLOCK_SIZE = 16;

	private static int[] SUPERVISOR_MEMORY = new int[SUPERVISOR_MEMORY_SIZE];
	private static int[] MEMORY = new int[MEMORY_SIZE];

	public static int get(int address) {
		switch (validAddress(address)) {
		case (1):
			return MEMORY[address];
		case (2):
			return SUPERVISOR_MEMORY[address - MEMORY_SIZE];
		default:
			throw new RuntimeException("Memory out of bounds");
		}
	}

	public static void set(int address, int value) {
		// System.out.println(address);
		switch (validAddress(address)) {
		case (1):
			MEMORY[address] = value;
			return;
		case (2):
			SUPERVISOR_MEMORY[address - MEMORY_SIZE] = value;
			return;
		default:
			throw new RuntimeException("Memory out of bounds");
		}
	}

	public static int findEmptyPage() {
		int pageCount = MEMORY_SIZE / (BLOCK_SIZE * BLOCK_SIZE);
		for (int i = 0; i < pageCount; i++) {
			if (get(MEMORY_SIZE + i * BLOCK_SIZE) == 0
					&& get(MEMORY_SIZE + i * BLOCK_SIZE + 1) == 0) {
				return i;
			}
		}
		return -1;
	}

	private static boolean checkIfUsed(int value) {
		for (int i = MEMORY_SIZE; i < MEMORY_SIZE + BLOCK_SIZE * BLOCK_SIZE; i++) {
			if (get(i) != value) {
				return true;
			}
		}
		return false;
	}

	private static void generatePagesTable(int start) {
		for (int i = start; i < start + BLOCK_SIZE; i++) {
			int guess = 0;
			do {
				Random rand = new Random();
				guess = rand.nextInt(MEMORY_SIZE / 16);
			} while (!checkIfUsed(guess));
			set(i + MEMORY_SIZE, guess);
		}
	}

	public static VMMemory createVMMemory() {
		int page = findEmptyPage();
		if ((page) != -1) {
			generatePagesTable(page*BLOCK_SIZE);
			return new VMMemory(BLOCK_SIZE);
		}
		throw new RuntimeException("No memory space!");
	}

	/**
	 * 
	 * @param address
	 * @return -1 - invalid, 1 - valid, memory, 2- valid, supervisor memory
	 */
	private static int validAddress(int address) {
		if (address >= 0 && address < MEMORY_SIZE + SUPERVISOR_MEMORY_SIZE) {
			if (address < MEMORY_SIZE) {
				// System.out.println("Vartotojo");
				return 1;
			}
			if (address >= MEMORY_SIZE) {
				// System.out.println("Super");
				return 2;
			}
		}
		return 0;
	}

	public static String toStr() {
		String s = "";
		for (int i = MEMORY_SIZE; i < MEMORY_SIZE + SUPERVISOR_MEMORY_SIZE; i++) {
			s += i + ": " + get(i) + "\n";
		}
		return s;
	}

}
