package os2.main.resources.descriptors;

import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

public class PrintDescriptor implements ResourceDescriptorInterface {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
