package os2.main.resources.descriptors;

import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

public class ProgramInHDDDescriptor implements ResourceDescriptorInterface {

	private int[] programName;
	private boolean fromJobToSwap = true;
	
	public int[] getProgramName() {
		return this.programName;
	}

	public void setProgramName(int[] programName) {
		this.programName = programName;
	}

	public boolean isFromJobToSwap() {
		return fromJobToSwap;
	}

	public void setFromJobToSwap(boolean fromJobToSwap) {
		this.fromJobToSwap = fromJobToSwap;
	}

}
