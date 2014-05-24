package os2.main.hardware.ChannelsDevice;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import os2.main.hardware.HDD.FileLoader;
import os2.main.hardware.HDD.FileSaver;
import os2.main.hardware.memory.RMMemory;
import os2.main.hardware.memory.VMMemory;

public class ChannelsDevice {
	
	/*
	 *  ST, DT:
	 *  1 - VARTOTOJO ATMINTIS
	 *  2 - SUPERVIZORINĖ ATMINTIS
	 *  3 - IŠORINĖ ATMINTIS
	 *  4 - IŠVEDIMO SRAUTAS
	 */
	
	public static int SB;
	public static int DB;
	public static int ST;
	public static int DT;
	
	public static VMMemory vmm;

	public static int[] programName;
	public static int startAddress;
	public static int endAddress;
	
	public static boolean XCHG() {
		/* Kopijavimas iš supervizorinės atminties į išorinę */
		if (ST == 2 && DT == 3) {
			FileSaver programToHDD = null;
			int[] program;
			program = RMMemory.getIntProgramFromMemory(startAddress, endAddress);
			try {
				programToHDD = new FileSaver(programName);
			} catch (Exception e) {
				System.out.println("Nepavyko išskirti vietos išorinėje atminties naujai programai!");
				e.printStackTrace();
				return false;
			}
			for (int i = 0; i < program.length / 16 + 1; i++) {
				int[] block = Arrays.copyOfRange(program, i * 16, i * 16 + 16);
				try {
					programToHDD.saveBlockOfFile(block);
				} catch (UnsupportedEncodingException e) {
					System.out.println("Nepavyko programos perkelti iš supervizorinės atminties į išorinę!");
					e.printStackTrace();
					return false;
				}
			}
			programToHDD.closeSavedFile();
		}
		
		/* Kopijavimas iš išorinės atminties į vartotojo */
		if (ST == 3 && DT == 1) {
			FileLoader programInHDD = null;
			int[] program = new int[0];
			int[] programBlock;
			try {
				programInHDD = new FileLoader(programName);
			} catch (Exception e) {
				System.out.println("Problema skaitant programą iš išorinės atminties!");
				e.printStackTrace();
			}
			while (!programInHDD.checkIfFileEnd()) {
				programBlock = programInHDD.getBlockOfFile();
				program = concat(program, programBlock);
			}
			vmm.loadProgram(program);
		}
		
		/* Kopijavimas iš supervizorinės atminties į išvedimo srautą */
		if (ST == 2 && DT == 4) {
			int[] messageInts;
			messageInts = RMMemory.getIntProgramFromMemory(startAddress, endAddress);
		}
		
		return true;
	}
	
	private static int[] concat(int[] A, int[] B) {
		int aLen = A.length;
		int bLen = B.length;
		int[] C = new int[aLen + bLen];
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);
		return C;
	}

}
