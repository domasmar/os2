package os2.main.resources;

import os2.main.processes.Process;
import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

public class Resource {
	
	private Process parent = null;
	private String name;
	private ResourceDescriptorInterface descriptor = null;
	
	public Resource(String name) {
		this.name = name;
	}
	
	public Process getParent() {
		return this.parent;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void removeParent() {
		this.parent = null;
	}
	
	public void setParent(Process process) {
		this.parent = process;
	}
	
	public void setDescriptor(ResourceDescriptorInterface descriptor) {
		this.descriptor = descriptor;
	}
	
	public ResourceDescriptorInterface getDescriptor() {
		return this.descriptor;
	}
}
