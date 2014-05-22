package os2.main.software.commandsConverter;

public class Variable {

	private String name;
	private int value;

	public Variable(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public int getValue() {
		return this.value;
	}

}