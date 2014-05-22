package os2.main.software.executor;

/**
 *
 * @author Arturas
 */
public class CmdWithVar {
    public byte commandOpc;
    public int variable;
    public Command command;
    public int row;
    
    public String toString() {
    	String a = "\nOpc : " + commandOpc;
    	a += "\nVar: " + variable;
    	a += "\nCommand " + command;
    	a += "\nRow: " + row;
    	return a;
    }
}
