package os2.main.resources.descriptors;

import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

public class PrintDescriptor implements ResourceDescriptorInterface {

	private int startAddress;
	private int endAddress;

	public int getStartAddress() {
		return this.startAddress;
	}

	public void setStartAddress(int startAddress) {
		this.startAddress = startAddress;
	}
	
	public int getEndAddress() {
		return this.endAddress;
	}

	public void setEndAddress(int endAddress) {
		this.endAddress = endAddress;
	}
}
