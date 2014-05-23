package os2.main.hardware.ChannelsDevice;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
	public static int[] programName;
	public static VMMemory vmm;
	public static ArrayList vars;
	public static int endAddress;
	
	public static boolean XCHG() {
		/* Kopijavimas iš supervizorinės atminties į išorinę */
		if (ST == 2 && DT == 3) {
			ArrayList<Integer> programList = new ArrayList<Integer>();
			FileSaver programToHDD = null;
			int[] programArray;
			programArray = RMMemory.getProgramFromMemory(SB, endAddress);
			for (int i = 0; i < programArray.length; i++) {
				System.out.println(programArray[i]);
			}
			try {
				programToHDD = new FileSaver(programName);
			} catch (Exception e) {
				System.out.println("Nepavyko programos perkelti iš supervizorinės atminties į išorinę! (01)");
				e.printStackTrace();
				return false;
			}
			programArray = new int[programList.size()];
			for (int i = 0; i < programList.size(); i++) {
				programArray[i] = programList.get(i);
			}
			for (int i = 0; i < programArray.length / 16 + 1; i++) {
				int[] block = Arrays.copyOfRange(programArray, i * 16, i * 16 + 16);
				try {
					programToHDD.saveBlockOfFile(block);
				} catch (UnsupportedEncodingException e) {
					System.out.println("Nepavyko programos perkelti iš supervizorinės atminties į išorinę! (02)");
					e.printStackTrace();
					return false;
				}
			}
			programToHDD.closeSavedFile();
		}
		
		/* Kopijavimas iš išorinės atminties į vartotojo */
		if (ST == 3 && DT == 1) {
			FileLoader program = null;
			try {
				program = new FileLoader(programName);
			} catch (Exception e) {
				System.out.println("Problema, skaitant programą iš išorinės atminties!");
				e.printStackTrace();
			}
			while (!program.checkIfFileEnd()) {
				
			}
		}
		
		return true;
	}

}
