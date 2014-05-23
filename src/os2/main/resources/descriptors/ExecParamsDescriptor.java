package os2.main.resources.descriptors;

import java.util.ArrayList;

import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

public class ExecParamsDescriptor implements ResourceDescriptorInterface {
	
	private String programName;
	private int addressInSupMemory;
	private ArrayList vars;
	
	public String getProgramName() {
		return this.programName;
	}
	
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	
	public int getAddress() {
		return this.addressInSupMemory;
	}
	
	public void setAddress(int addressInSupMemory) {
		this.addressInSupMemory = addressInSupMemory;
	}

	public ArrayList getVars() {
		return vars;
	}

	public void setVars(ArrayList vars) {
		this.vars = vars;
	}

}
