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
        
        
        public int getAX() {
            return this.AX;
        }
        
        public int getBX() {
            return this.BX;
        }
	
        public int getPTR() {
            return this.PTR;
        }
        
        public int getTIMER() {
            return this.TIMER;
        }
        
        public short getCS() {
            return this.CS;
        }
        
        public short getDS() {
            return this.DS;
        }
        
        public short getSS() {
            return this.SS;
        }
        
        public short getIP() {
            return this.IP;
        }
        
        public short getSP() {
            return this.SP;
        }
        
        public byte getC() {
            return this.C;
        }
        
        public byte getMODE() {
            return this.MODE;
        }
        
        public byte getCHST0() {
            return this.CHST0;
        }
        
        public byte getCHST1() {
            return this.CHST1;
        }
        
        public byte getCHST2() {
            return this.CHST2;
        }
        
        public byte getSTI() {
            return this.STI;
        }
        
        public void setAX(int value) {
            this.AX = value;
        }
        
        public void setBX(int value) {
            this.AX = value;
        }
	
        public void setPTR(int value) {
            this.AX = value;
        }
        
        public void setTIMER(int value) {
            this.AX = value;
        }
        
        public void setCS(short value) {
            this.AX = value;
        }
        
        public void setDS(short value) {
            this.AX = value;
        }
        
        public void setSS(short value) {
            this.AX = value;
        }
        
        public void setIP(short value) {
            this.AX = value;
        }
        
        public void setSP(short value) {
            this.AX = value;
        }
        
        public void setC(byte value) {
            this.AX = value;
        }
        
        public void setMODE(byte value) {
            this.AX = value;
        }
        
        public void setCHST0(byte value) {
            this.AX = value;
        }
        
        public void setCHST1(byte value) {
            this.AX = value;
        }
        
        public void setCHST2(byte value) {
            this.AX = value;
        }
        
        public void setSTI(byte value) {
            this.AX = value;
        }
}
