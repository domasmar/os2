package os2.main.resources.descriptors;

import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

public class ExecParamsDescriptor implements ResourceDescriptorInterface {
	
	private String programName;
	private int startAddress;
	private int endAddress;

	public String getProgramName() {
		return this.programName;
	}
	
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	
	public int getStartAddress() {
		return this.startAddress;
	}
	
	public void setStartAddress(int startAddress) {
		this.startAddress = startAddress;
	}

	public int getEndAddress() {
		return endAddress;
	}

	public void setEndAddress(int endAddress) {
		this.endAddress = endAddress;
	}

}
