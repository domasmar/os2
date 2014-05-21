package os2.main.resources.descriptors;

import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

public class FromGUIDescriptor implements ResourceDescriptorInterface {

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
