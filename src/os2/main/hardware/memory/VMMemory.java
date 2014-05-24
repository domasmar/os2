package os2.main.hardware.memory;

import os2.main.hardware.CPU;

public class VMMemory {
	private static final int VIRTUAL_MEMORY_SIZE = 256;

	private int ptr = 0;
	private int pageNumber = 0;

	private static final int REGISTER_SPACE = 20;

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
			CPU.setPI((byte) 1);
			throw new RuntimeException("PI = 1");
		}
	}

	public int get(int address) {
		if (address / 16 <= this.ptr / (16 * 16)) {
			return RMMemory.get(getRealAddress(address));
		} else {
			CPU.setPI((byte) 1);
			throw new RuntimeException("PI = 1");
		}
	}

	public void loadCPUState() {
		int i = 0;
		CPU.setAX(this.get(i++));
		CPU.setBX(this.get(i++));
		CPU.setPTR(this.get(i++));
		CPU.setC((byte) this.get(i++));
		CPU.setCHST0((byte) this.get(i++));
		CPU.setCHST1((byte) this.get(i++));
		CPU.setCHST2((byte) this.get(i++));
		CPU.setCS((short) this.get(i++));
		CPU.setDS((short) this.get(i++));
		CPU.setEND((byte) this.get(i++));
		CPU.setIOI((byte) this.get(i++));
		CPU.setIP((byte) this.get(i++));
		CPU.setMODE((byte) this.get(i++));
		CPU.setPI((byte) this.get(i++));
		CPU.setSI((byte) this.get(i++));
		CPU.setSP((byte) this.get(i++));
		CPU.setSS((byte) this.get(i++));
		CPU.setSTI((byte) this.get(i++));
		CPU.setTI((byte) this.get(i++));
	}

	public void saveCPUState() {
		int i = 0;
		this.set(i++, CPU.getAX());
		this.set(i++, CPU.getBX());
		this.set(i++, CPU.getPTR());
		this.set(i++, CPU.getC());
		this.set(i++, CPU.getCHST0());
		this.set(i++, CPU.getCHST1());
		this.set(i++, CPU.getCHST2());
		this.set(i++, CPU.getCS());
		this.set(i++, CPU.getDS());
		this.set(i++, CPU.getEND());
		this.set(i++, CPU.getIOI());
		this.set(i++, CPU.getIP());
		this.set(i++, CPU.getMODE());
		this.set(i++, CPU.getPI());
		this.set(i++, CPU.getSI());
		this.set(i++, CPU.getSP());
		this.set(i++, CPU.getSS());
		this.set(i++, CPU.getSTI());
		this.set(i++, CPU.getTI());
	}

	public void loadProgram(int[] program) {
		int varsSeparatorIndex = 0;
		for (int i = 0; i < program.length; i++) {
			if (program[i] == RMMemory.VARS_SEPARATOR) {
				varsSeparatorIndex = i;
			}
		}
		int varsSize = varsSeparatorIndex;
		int programSize = program.length - varsSeparatorIndex
				- (varsSize > 0 ? 1 : 0);

		int i;
		for (i = 0; i < varsSize; i++) {
			this.set(REGISTER_SPACE + i, program[i]);
		}
		CPU.setDS((short) REGISTER_SPACE);
		CPU.setSS((short) (REGISTER_SPACE + i));

		for (i = 0; i < programSize; i++) {
			this.set(VIRTUAL_MEMORY_SIZE - i - 1, program[program.length - 1
					- i]);
		}
		CPU.setCS((short) (VIRTUAL_MEMORY_SIZE - i));
	}

	public void destroy() {
		for (int i = 0; i < VIRTUAL_MEMORY_SIZE; i++) {
			this.set(i, 0);
		}
		RMMemory.clearPage(this.pageNumber);
	}

	public String toString() {
		String r = "";
		for (int i = 0; i < VIRTUAL_MEMORY_SIZE; i++) {
			r += i + ": " + this.get(i) + " \n";
		}
		return r;
	}

}
