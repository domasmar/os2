package os2.main.hardware.memory;

import os2.main.hardware.CPU;

public class VMMemory {
	private static final int VIRTUAL_MEMORY_SIZE = 256;
	
	private int ptr = 0;
	private int pageNumber = 0;

	public VMMemory(int ptr, int pageNumber) {
		this.ptr = ptr;
		this.pageNumber = pageNumber;
	}

	private int getRealAddress(int index) {
		int ptrA2 = this.ptr / 16 % 16;
		int ptrA3 = this.ptr % 16;
		int realBlock = RMMemory.get((16 * ptrA2 + ptrA3) * 16 + index / 16
				+ RMMemory.MEMORY_SIZE);
		return realBlock * 16 + index % 16;
	}

	public void set(int address, int value) {
		if (address / 16 <= this.ptr / (16 * 16)) {
			RMMemory.set(getRealAddress(address), value);
		} else {
			CPU.PI = (byte) 1;
			throw new RuntimeException("PI = 1");
		}
	}

	public int get(int address) {
		if (address / 16 <= this.ptr / (16 * 16)) {
			return RMMemory.get(getRealAddress(address));
		} else {
			CPU.PI = (byte) 1;
			throw new RuntimeException("PI = 1");
		}
	}
	
	public void destroy() {
		for (int i = 0; i < VIRTUAL_MEMORY_SIZE; i++) {
			this.set(i, 0);
		}
		RMMemory.clearPage(this.pageNumber);
	}

}
