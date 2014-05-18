package os2.main.hardware.ChannelsDevice;

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
		if (ST == 2 && DT == 3) {
			// paimti informaciją nurodytu adresu supervizorinėje atmintyje
			// įrašyti informaciją nurodytu adresu į HDD
		}
	}

}
