package os2.main.hardware;

public class CPU {
	/**
	 * darbinis registras
	 */
	private static int AX = 0;
	/**
	 * darbinis registras
	 */
	private static int BX = 0;
	/**
	 * puslapių lentelės registras
	 */
	private static int PTR = 0;
	/**
	 * vykdomos komandos skaitikliukas
	 */
	private static short IP = 0;
	/**
	 * steko rodyklė
	 */
	private static short SP = 0;
	/**
	 * loginis registras
	 */
	private static byte C = 0;
	/**
	 * 0 - vartotojo režimas, 1 - supervizoriaus režimas
	 */
	private static byte MODE = 0;
	/**
	 * programinis pertraukimas
	 */
	private static byte PI = 0;
	/**
	 * supervizoriaus pertraukimas
	 */
	private static byte SI = 0;
	/**
	 * išvedimo, įvedimo pertraukimas
	 */
        private static byte END = 0;
        /**
	 * programos pabaigos pertraukimas
	 */
	private static byte IOI = 0;
	/**
	 * timerio pertraukimas
	 */
	private static byte TI = 0;
	/**
	 * 1 kanalo registras
	 */
	private static byte CHST0 = 0;
	/**
	 * 2 kanalo registras
	 */
	private static byte CHST1 = 0;
	/**
	 * 3 kanalo registras
	 */
	private static byte CHST2 = 0;
	/**
	 * steko pertraukimas
	 */
	private static byte STI = 0;
	/**
	 * timeris
	 */
	private static int TIMER = 0;
	/**
	 * duomenų segmentas
	 */
	private static short DS = 0;
	/**
	 * kodo segmentas
	 */
	private static short CS = 0;
	/**
	 * steko segmentas
	 */
	private static short SS = 0;

	public static int getAX() {
		return AX;
	}

	public static int getBX() {
		return BX;
	}

	public static int getPTR() {
		return PTR;
	}

	public static int getTIMER() {
		return TIMER;
	}

	public static short getCS() {
		return CS;
	}

	public static short getDS() {
		return DS;
	}

	public static short getSS() {
		return SS;
	}

	public static short getIP() {
		return IP;
	}

	public static short getSP() {
		return SP;
	}

	public static byte getC() {
		return C;
	}
        
        public static byte getEND() {
		return END;
	}

	public static byte getMODE() {
		return MODE;
	}

	public static byte getCHST0() {
		return CHST0;
	}

	public static byte getCHST1() {
		return CHST1;
	}

	public static byte getCHST2() {
		return CHST2;
	}

	public static byte getSTI() {
		return STI;
	}
        
        public static byte getSI() {
		return SI;
	}
        
        public static byte getIOI() {
		return IOI;
	}
        
        public static byte getTI() {
		return TI;
	}

	public static byte getPI() {
		return PI;
	}

	public static void setAX(int value) {
		AX = value;
	}

	public static void setBX(int value) {
		BX = value;
	}

	public static void setPTR(int value) {
		PTR = value;
	}

	public static void setTIMER(int value) {
		TIMER = value;
	}

	public static void setCS(short value) {
		CS = value;
	}

	public static void setDS(short value) {
		DS = value;
	}

	public static void setSS(short value) {
		SS = value;
	}

	public static void setIP(short value) {
		IP = value;
	}

	public static void setSP(short value) {
		SP = value;
	}

	public static void setC(byte value) {
		C = value;
	}

	public static void setMODE(byte value) {
		MODE = value;
	}

	public static void setCHST0(byte value) {
		CHST0 = value;
	}

	public static void setCHST1(byte value) {
		CHST1 = value;
	}

	public static void setCHST2(byte value) {
		CHST2 = value;
	}

	public static void setSTI(byte value) {
		STI = value;
	}
        
        public static void setEND(byte value) {
		END = value;
	}
        
        public static void setSI(byte value) {
		SI = value;
	}

	public static void setPI(byte value) {
		PI = value;
	}
        
        public static void setTI(byte value) {
		TI = value;
	}
        
        public static void setIOI(byte value) {
		IOI = value;
	}
}
