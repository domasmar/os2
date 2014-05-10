package os2.main.process;

public enum ProcessType {
	SYSTEM(2), USER(1);
	
	private int value;
	
	private ProcessType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}

}
