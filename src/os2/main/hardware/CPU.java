package os2.main.hardware;

public class CPU {
	/**
	 * darbinis registras
	 */
	public static byte AX = 0;
	/**
	 * darbinis registras
	 */
	public static byte BX = 0;
	/**
	 * puslapių lentelės registras
	 */
	public static byte PTR = 0;
	/**
	 * vykdomos komandos skaitikliukas
	 */
	public static byte IP = 0;
	/**
	 * steko rodyklė
	 */
	public static byte SP = 0;
	/**
	 * loginis registras
	 */
	public static byte C = 0;
	/**
	 * 0 - vartotojo režimas, 1 - supervizoriaus režimas
	 */
	public static byte MODE = 0;
	/**
	 * programinis pertraukimas
	 */
	public static byte PI = 0;
	/**
	 * supervizoriaus pertraukimas
	 */
	public static byte SI = 0;
	/**
	 * išvedimo, įvedimo pertraukimas
	 */
	public static byte IOI = 0;
	/**
	 * timerio pertraukimas
	 */
	public static byte TI = 0;
	/**
	 * 1 kanalo registras
	 */
	public static byte CHST0 = 0;
	/**
	 * 2 kanalo registras
	 */
	public static byte CHST1 = 0;
	/**
	 * 3 kanalo registras
	 */
	public static byte CHST2 = 0;
	/**
	 * steko pertraukimas
	 */
	public static byte STI = 0;
	/**
	 * timeris
	 */
	public static byte TIMER = 0;
	/**
	 * duomenų segmentas
	 */
	public static byte DS = 0;
	/**
	 * kodo segmentas
	 */
	public static byte CS = 0;
	/**
	 * steko segmentas
	 */
	public static byte SS = 0;
	
}
