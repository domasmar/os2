package os2.main.processes;

public abstract class Process {
	
	private int pid = PID.getNew(); 
	
	protected ProcessType type;
	protected int step = 0;
	protected ProcessStatus status;
	
	protected long startTime = System.nanoTime();
	
	protected int priority = 100;
	
	public abstract void nextStep();
	
	public long getPriority() {
		//long timeWithoutWork = (System.nanoTime() - startTime); 
		//return timeWithoutWork;
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	

	protected void changeStep(int i) {
		this.priority--;
		//this.startTime = System.nanoTime();
		this.step = i;		
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

	public int getStep() {
		return this.step;
	}
	
	public int getPid() {
		return this.pid;
	}
}
