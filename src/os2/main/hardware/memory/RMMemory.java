package os2.main.hardware.memory;

import java.util.Random;

/**
 * Realios mašinos atmintis. Vartotojo atmintis [0.. 3071] Supervizorinė
 * atmintis [3072..4098] Puslapių lentelė prasideda nuo 3072 iki 3263. 
 * Supervizorine atmintis darbui nuo 3264 iki 4098
 * 
 * @author domas
 * 
 */
public class RMMemory {	
	public static final int SUPERVISOR_MEMORY_SIZE = 1024;
	public static final int MEMORY_SIZE = 3072;
	public static final int BLOCK_SIZE = 16;
	
	public static final int TOTAL_MEMORY_SIZE = MEMORY_SIZE + SUPERVISOR_MEMORY_SIZE;
	
	public static final int SUPERVISOR_MEMORY_BEGIN = 3264;

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

	private static int generatePagesTable(int start) {
		for (int i = start; i < start + BLOCK_SIZE; i++) {
			int guess = 0;
			do {
				Random rand = new Random();
				guess = rand.nextInt(MEMORY_SIZE / 16);
			} while (!checkIfUsed(guess));
			set(i + MEMORY_SIZE, guess);
		}
		return (BLOCK_SIZE - 1) * 16 * 16 + start;
	}

	public static VMMemory createVMMemory() {
		int page = findEmptyPage();
		if ((page) != -1) {
			int ptr = generatePagesTable(page*BLOCK_SIZE);
			return new VMMemory(ptr, page);
		}
		throw new RuntimeException("No memory space!");
	}

	public static void clearPage(int page) {
		int absolutePageAddress = MEMORY_SIZE + page * BLOCK_SIZE;
		for (int i = absolutePageAddress; i < absolutePageAddress + BLOCK_SIZE; i++) {
			RMMemory.set(i, 0);
		}
	}
	
	/**
	 * 
	 * @param address
	 * @return -1 - invalid, 1 - valid, memory, 2- valid, supervisor memory
	 */
	private static int validAddress(int address) {
		if (address >= 0 && address < MEMORY_SIZE + SUPERVISOR_MEMORY_SIZE) {
			if (address < MEMORY_SIZE) {
				return 1;
			}
			if (address >= MEMORY_SIZE) {
				return 2;
			}
		}
		return 0;
	}
	
	// Prints only supervisor memory
	public static String toStr() {
		String s = "";
		for (int i = MEMORY_SIZE; i < MEMORY_SIZE + SUPERVISOR_MEMORY_SIZE; i++) {
			s += i + ": " + get(i) + "\n";
		}
		return s;
	}

	public static boolean loadProgramToMemory(byte[] content) {
		if (content.length >= TOTAL_MEMORY_SIZE - SUPERVISOR_MEMORY_BEGIN) {
			return false;
		} 
		for (int i = 0; i < content.length; i++) {
			SUPERVISOR_MEMORY[SUPERVISOR_MEMORY_BEGIN - MEMORY_SIZE + i] = content[i];
		}
		return true;
	}

}
