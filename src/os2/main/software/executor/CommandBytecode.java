package os2.main.software.executor;

/**
 *
 * @author Arturas
 */
public class CommandBytecode {

	public static final byte MOV_AX = (byte) 0b1100_0000;
	public static final byte MOV_BX = (byte) 0b1101_0000;
	public static final byte LOA_AX = (byte) 0b1000_0000;
	public static final byte LOA_BX = (byte) 0b1001_0000;
	public static final byte STO_AX = (byte) 0b1000_0001;
	public static final byte STO_BX = (byte) 0b1001_0001;
	public static final byte PUSH = (byte) 0b1000_0010;
	public static final byte POP = (byte) 0b1000_0011;
	public static final byte ADD = (byte) 0b1000_0100;
    public static final byte SUB = (byte) 0b1000_0101;
    public static final byte CMP = (byte) 0b1000_0111;
    public static final byte JMP = (byte) 0b0100_0100;
    public static final byte JA = (byte) 0b0100_0000;
    public static final byte JB = (byte) 0b0100_0001;
    public static final byte JE = (byte) 0b0100_0010;
    public static final byte JNE = (byte) 0b0100_0011;
    public static final byte OUTR_AX = (byte) 0b1110_0000;
    public static final byte OUTR_BX = (byte) 0b1111_0000;
    public static final byte OUTM = (byte) 0b1111_0111;
    public static final byte STOP = (byte) 0b0000_0001;

}
