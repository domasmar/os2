package os2.main.resources.descriptors;

import java.util.ArrayList;

import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

public class ProgramInHDDDescriptor implements ResourceDescriptorInterface {
	
	private ArrayList vars;
	private int[] programName;
	
	public int[] getProgramName() {
		return this.programName;
	}
	
	public void setProgramName(int[] programName) {
		this.programName = programName;
	}
	
	public ArrayList getVars() {
		return this.vars;
	}
	
	public void setVars(ArrayList vars) {
		this.vars = vars;
	}

}
