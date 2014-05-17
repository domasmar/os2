package os2.main.resources;

import os2.main.processes.Process;

public class Resource {
	
	private Process parent = null;
	private String name;
	private Object information = null;
	
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
	
	public void setInformation(Object information) {
		this.information = information;
	}
	
	public Object getInformation() {
		return this.information;
	}
}
