package os2.main.hardware.ChannelsDevice;

import java.util.ArrayList;
import java.util.Arrays;

import os2.main.hardware.HDD.FileSaver;
import os2.main.hardware.memory.RMMemory;

public class ChannelsDevice {
	
	/*
	 *  DT, ST:
	 *  1 - VARTOTOJO ATMINTIS
	 *  2 - SUPERVIZORINĖ ATMINTIS
	 *  3 - IŠORINĖ ATMINTIS
	 *  4 - ĮVEDIMO SRAUTAS
	 */
	
	public static int SB;
	public static int DB;
	public static int ST;
	public static int DT;
	
	public static void XCHG() {
		ArrayList<Integer> programList = new ArrayList<Integer>();
		boolean isEnd = false;
		int[] programArray;
		int index = SB;
		int value;
		if (ST == 2 && DT == 3) {
			do {
				value = RMMemory.get(index);
				programList.add(value);
				index++;
			} while (value != -1);
			FileSaver programToHDD = new FileSaver("programos pavadinimas"); // programos pavadinimas integerių masyve
			programArray = new int[programList.size()];
			for (int i = 0; i < programList.size(); i++) {
				programArray[i] = programList.get(i);
			}
			for (int i = 0; i < programArray.length / 16 + 1; i++) {
				int[] block = Arrays.copyOfRange(programArray, i * 16, i * 16 + 16);
				programToHDD.saveBlockOfFile(block);
			}
			programToHDD.closeSavedFile();
		}
	}

}
