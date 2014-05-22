package os2.main.software.commandsConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CommandsConverter {

	private static String[] machineCommands = { "mov", "loa", "sto", "push",
			"pop", "add", "sub", "cmp", "jmp", "ja", "jb", "je", "jne", "outr",
			"outm", "fork", "stop" };

	private ArrayList<Variable> variables = new ArrayList<Variable>();
	private ArrayList<Label> labels = new ArrayList<Label>();
	
	private String[] sourceCode;
	private String[] commands;

	public CommandsConverter(String sourceCode) throws Exception {
		sourceCode = sourceCode.trim();
		saveSourceCode(sourceCode);
		validateSourceCode();
		saveCommands();
		saveVariables();
		saveLabels(findJumpVariables());
		replaceVarNameWithAddress();
		replaceLabelNameWithAddress();
		removeEmptyLines();
	}

	public String[] getSourceCode() {
		return this.sourceCode;
	}

	public ArrayList<Variable> getVariables() {
		return this.variables;
	}

	public ArrayList<Label> getLabels() {
		return this.labels;
	}

	/* Pagalbinė funkcija: patikrina, ar eilutė yra 16-ainio skaičiaus pavidalo */
	private static boolean isNumeric(String str) {
		try {
			int d = Integer.parseInt(str, 16);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/*
	 * Iš paduoto pirminio kodo teksto pašalina visas tuščias eilutes ir tarpus
	 * bei į masyvą "sourceCode" įrašo „švaraus“ kodo eilutės.
	 */
	private void saveSourceCode(String sourceCode) {
		String tidySourceCode = sourceCode.replaceAll("(?m)^[ \t]*\r?\n", "")
				.trim().toLowerCase();
		this.sourceCode = tidySourceCode.split("\r\n");
	}

	public String[] getCommands() {
		return this.commands;
	}

	/*
	 * Į sąrašą išsaugo tik tas pirminio kodo eilutes, kuriose yra komandos.
	 * Grąžina komandų masyvą.
	 */
	private void saveCommands() {
		ArrayList<String> commands = new ArrayList<String>();
		for (int i = 0; i <= this.sourceCode.length - 1; i++) {
			for (int j = 0; j < machineCommands.length - 1; j++) {
				if (this.sourceCode[i].matches(".*\\b(" + machineCommands[j]
						+ ")\\b.*")) {
					commands.add(this.sourceCode[i]);
					if (this.sourceCode[i].matches(".*\\b(mov)\\b.*")) {
						commands.add("");
					}
				}
			}
		}
		this.commands = commands.toArray(new String[commands.size()]);
	}

	/* Į sąrašą išsaugomi kintamieji ir jų reikšmės. */
	private void saveVariables() {
		for (int i = 0; i <= this.sourceCode.length - 1; i++) {
			if (this.sourceCode[i].matches(".*\\b(def)\\b.*")) {
				String[] definition = this.sourceCode[i].split(" def ");
				Variable variable = new Variable(definition[0],
						Integer.parseInt(definition[1], 16));
				this.variables.add(variable);
			}
		}
// 		Nereikia, nes kai CommandConverter vyksta atmintis dar nebūna sukurta
//		for (int i = 0; i <= this.variables.size() - 1; i++) {
//			vmm.setValue(cpu.getDS() + i, this.variables.get(i).getValue());
//		}
	}

	/* Į sąrašą sudedami visi JUMP, JA, JB, JE komandose rasti kintamieji. */
	private ArrayList<String> findJumpVariables() {
		ArrayList<String> variables = new ArrayList<String>();
		for (int i = 0; i <= this.sourceCode.length - 1; i++) {
			if (this.sourceCode[i].matches(".*\\b(jmp)\\b.*")
					|| this.sourceCode[i].matches(".*\\b(ja)\\b.*")
					|| this.sourceCode[i].matches(".*\\b(jb)\\b.*")
					|| this.sourceCode[i].matches(".*\\b(je)\\b.*")
					|| this.sourceCode[i].matches(".*\\b(jne)\\b.*")) {
				variables.add(this.sourceCode[i].split(" ")[1]);
			}
		}
		return variables;
	}

	/* Į „Label“ tipo elementų sąrašą įrašomi visi labeliai ir jų eilutės. */
	private void saveLabels(ArrayList<String> labels) {
		for (int i = 0; i <= this.sourceCode.length - 1; i++) {
			for (int j = 0; j <= labels.size() - 1; j++) {
				if (this.sourceCode[i].trim().equalsIgnoreCase(labels.get(j))) {
					for (int k = 0; k <= this.commands.length - 1; k++) {
						if (this.commands[k]
								.equalsIgnoreCase(this.sourceCode[i + 1])) {
							Label label = new Label(labels.get(j), k);
							this.labels.add(label);
						}
					}
				}
			}
		}
	}

	/* Komanduose esančius kintamuosius pakeičia adresais duomenų segmente. */
	private void replaceVarNameWithAddress() {
		for (int i = 0; i <= this.commands.length - 1; i++) {
			for (int j = 0; j <= this.variables.size() - 1; j++) {
				if (this.commands[i].matches(".*\\b("
						+ this.variables.get(j).getName() + ")\\b.*")) {
					this.commands[i] = this.commands[i].replace(this.variables
							.get(j).getName(), Integer.toString(j));
				}
			}
		}
	}

	/* Komanduose esančius labelius pakeičia adresais kodo segmente. */
	private void replaceLabelNameWithAddress() {
		for (int i = 0; i <= this.commands.length - 1; i++) {
			for (int j = 0; j <= this.labels.size() - 1; j++) {
				if (this.commands[i].matches(".*\\b("
						+ this.labels.get(j).getName() + ")\\b.*")) {
					this.commands[i] = this.commands[i].replace(this.labels
							.get(j).getName(), Integer.toString(this.labels
							.get(j).getValue()));
				}
			}
		}
	}

	/* Panaikina tuščias eilutes iš komandų masyvo. */
	private void removeEmptyLines() {
		ArrayList<String> commandsList = new ArrayList<String>(
				Arrays.asList(this.commands));
		commandsList.removeAll(Collections.singleton(""));
		this.commands = commandsList.toArray(new String[commandsList.size()]);
	}

	/* Tikrina pirminio kodo sintaksė. */
	private void validateSourceCode() throws Exception {
		ArrayList<String> variables = new ArrayList<String>();
		ArrayList<String> labels = new ArrayList<String>();
		String[] operands;
		String[] definition;
		String command;
		String arguments;
		boolean isCorrect = false;
		boolean isCommand = false;
		for (int i = 0; i <= this.sourceCode.length - 1; i++) {
			/*
			 * Nagrinėjamos pirminio kodo eilutės, sudarytos iš daugiau nei
			 * vieno žodžio (PUSH AX, MOV AX, FFFF ir t.t.), STOP, ADD, SUB
			 * neįeina.
			 */
			if (this.sourceCode[i].split(" ").length > 1) {
				/* Tikrinama kintamųjų apibrėžimo sritis. */
				if (this.sourceCode[i].matches(".*\\b(def)\\b.*")) {
					definition = this.sourceCode[i].split(" ");
					if (definition.length != 3) {
						throw new Exception(
								"Neatpažintas kintamojo apibrėžimas! (eilutės numeris: "
										+ i + ")");
					}
					for (int j = 0; j <= machineCommands.length - 1; j++) {
						if (definition[0].equals(machineCommands[j])) {
							throw new Exception(
									"Kintamasis negali sutapti su komandos (MOV, STO ir t.t.) pavadinimu! (eilutės numeris: "
											+ i + ")");
						}
					}
					variables.add(definition[0]);
					if (!definition[1].equals("def")) {
						throw new Exception(
								"Neatpažintas kintamojo apibrėžimas! (eilutės numeris: "
										+ i + ")");
					}
					if (!isNumeric(definition[2])) {
						throw new Exception(
								"Kintamojo reikšmė turi būti sveikas skaičius! (eilutės numeris: "
										+ i + ")");
					}
				}
				/*
				 * Tikrinamos kitos pirminio kodo eilutės, sudarytos iš daugiau
				 * nei vieno žodžio.
				 */
				else {
					command = this.sourceCode[i].split(" ")[0].trim();
					arguments = this.sourceCode[i].split(this.sourceCode[i]
							.split(" ")[0])[1].trim();
					for (int j = 0; j <= machineCommands.length - 1; j++) {
						isCorrect = false;
						if (command.equals(machineCommands[j])) {
							isCorrect = true;
							break;
						}
					}
					if (!isCorrect) {
						throw new Exception("Neatpažinta komanda (" + command
								+ ")! (eilutės numeris: " + i + ")");
					}
					/*
					 * Tikrinamos pirminio kodo eilutės, kuriose yra komandos,
					 * turinčios du operandus (MOV, STO, LOAD).
					 */
					if (arguments.split(",").length == 2) {
						operands = new String[2];
						operands[0] = arguments.split(",")[0].trim();
						operands[1] = arguments.split(",")[1].trim();
						if (command.equals("mov") || command.equals("sto")) {
							if (!operands[0].equals("ax")
									&& !operands[0].equals("bx")) {
								throw new Exception(
										"MOV ir STO komandų pirmasis operandas turi būti registras AX arba BX! (eilutės numeris: "
												+ i + ")");
							}
						} else if (command.equals("loa")) {
							if (!operands[1].equals("ax")
									&& !operands[1].equals("bx")) {
								throw new Exception(
										"LOA komandos antrasis argumentas turi būti registras AX arba BX! (eilutės numeris: "
												+ i + ")");
							}
						}
						if (command.equals("mov")) {
							if (!isNumeric(operands[1])) {
								throw new Exception(
										"MOV komandos antrasis argumentas turi būti skaičius! (eilutės numeris: "
												+ i + ")");
							}
						} else if (command.equals("sto")) {
							isCorrect = false;
							for (int j = 0; j <= variables.size() - 1; j++) {
								if (operands[1].equals(variables.get(j))) {
									isCorrect = true;
								}
							}
							if (!isCorrect) {
								throw new Exception("Kintamasis ("
										+ operands[1]
										+ ") neapibrėžtas! (eilutės numeris: "
										+ i + ")");
							}
						} else if (command.equals("loa")) {
							isCorrect = false;
							for (int j = 0; j <= variables.size() - 1; j++) {
								if (operands[0].equals(variables.get(j))) {
									isCorrect = true;
								}
							}
							if (!isCorrect) {
								throw new Exception("Kintamasis ("
										+ operands[1]
										+ ") neapibrėžtas! (eilutės numeris: "
										+ i + ")");
							}
						}
					}
					/*
					 * Tikrinamos kodo eilutės, turinčios vieną operandą (PUSH,
					 * POP ir t.t.).
					 */
					else {
						if (command.equals("push") || command.equals("pop")
								|| command.equals("outm")) {
							if (isNumeric(arguments))
								isCorrect = true;
							if (!isCorrect) {
								throw new Exception(
										"Poslinkis ("
												+ arguments
												+ ") turi būti apibrėžtas sveikuoju skaičiumi! (eilutės numeris: "
												+ i + ")");
							}
						}
						if (command.equals("outr")) {
							if (!arguments.equals("ax")
									&& !arguments.equals("bx")) {
								throw new Exception(
										"OUTR komandos operandas turi būti AX arba BX! (eilutės numeris: "
												+ i + ")");
							}
						} else if (command.equals("jmp")
								|| command.equals("ja") || command.equals("jb")
								|| command.equals("je")
								|| command.equals("jne")) {
							isCorrect = false;
							for (int j = 0; j <= labels.size() - 1; j++) {
								if (arguments.equals(labels.get(j))) {
									isCorrect = true;
								}
							}
							if (!isCorrect) {
								throw new Exception("Žymė (" + arguments
										+ ") neapibrėžta! (eilutės numeris: "
										+ i + ")");
							}
						}
					}
				}
			}
			/*
			 * Tikrinamos kodo eilutės, sudarytos iš vieno žodžio (ADD, SUB,
			 * STOP komandos, labelių pasižymėjimas.
			 */
			else {
				isCommand = false;
				for (int j = 0; j <= machineCommands.length - 1; j++) {
					if (this.sourceCode[i].equals(machineCommands[j])) {
						isCommand = true;
					}
				}
				if (!isCommand) {
					labels.add(this.sourceCode[i]);
				}
			}
		}
	}

}