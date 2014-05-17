package os2.main.processes;

public abstract class Process {
		
	protected ProcessType type;
	protected int step = 0;
	protected ProcessStatus status;
	
	protected int priority = 100;
	
	public abstract void nextStep();
	
	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public ProcessType getType() {
		return this.type;
	}

	public ProcessStatus getStatus() {
		return this.status;
	}

	public void setStatus(ProcessStatus status) {
		this.status = status;		
	}
	
}
