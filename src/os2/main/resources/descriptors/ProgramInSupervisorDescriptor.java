package os2.main.resources.descriptors;

import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

public class ProgramInSupervisorDescriptor implements
		ResourceDescriptorInterface {

	private int programBegin;
	private int programEnd;
	private String programName;

	public int getProgramEnd() {
		return programEnd;
	}

	public void setProgramEnd(int programEnd) {
		this.programEnd = programEnd;
	}

	public int getProgramBegin() {
		return programBegin;
	}

	public void setProgramBegin(int programBegin) {
		this.programBegin = programBegin;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}
}
