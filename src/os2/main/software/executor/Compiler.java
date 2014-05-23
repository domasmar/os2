package os2.main.software.executor;

import java.util.Arrays;

public class Compiler {

	public static final int MAX_CS_SIZE = 256;

	private CmdWithVar[] cmdWithVar = new CmdWithVar[MAX_CS_SIZE];

	private int programSize;

	public int[] compile(String[] commandsArray) throws Exception {
		int commandNr = 0;
		int commandsIntArray[] = new int[MAX_CS_SIZE];
		for (int i = 0; i < commandsArray.length; i++) {
			if (commandsArray[i].trim().equals("")) {
				continue;
			} else {
				CmdWithVar cmd = recognizeStringCommand(commandsArray[i], i);
				if (cmd == null) {
					throw new Exception("Komanda neatpažinta. Komanda: "
							+ commandsArray[i] + ". Eilute: " + i);
				} else {
					cmdWithVar[commandNr] = cmd;

					String first = this.fixBinaryNumber(cmd.commandOpc, 8);

					if ((cmd.command.equals(Command.MOV_AX))
							|| (cmd.command.equals(Command.MOV_BX))) {
						String second = this.fixBinaryNumber(cmd.variable, 32);
						commandsIntArray[commandNr] = Integer
								.parseInt(first, 2);
						commandsIntArray[commandNr + 1] = Integer.parseInt(
								addMinus(second, 32), 2);
						commandNr++;
					} else {
						String second = this.fixBinaryNumber(cmd.variable, 8);
						commandsIntArray[commandNr] = Integer.parseInt(first
								+ second, 2);
					}
					commandNr++;
				}
			}
		}

		this.setProgramSize(commandNr);
		int firstZero = MAX_CS_SIZE;
		for (int i = 0; i < commandsIntArray.length; i++) {
			if (commandsIntArray[i] == 0) {
				firstZero = i;
				break;
			}
		}
		return Arrays.copyOf(commandsIntArray, firstZero);
	}


	private String addMinus(String bin, int length) {
		if ((bin.length() == length) && (bin.startsWith("1"))) {
			bin = Integer.toBinaryString((Integer.parseInt(bin.replaceFirst("1", "0"), 2) ^ 0xFFFFFFFF) + 1);
			bin = bin.replaceFirst("1", "-");
		}		
		return bin;
	}

	private String fixBinaryNumber(int bin, int maxLength) {
		String stringBin = Integer.toBinaryString(bin);		
		if (stringBin.length() > maxLength) {
			stringBin = stringBin.substring(stringBin.length() - maxLength,
					stringBin.length());
		}		
		if (stringBin.length() < maxLength) {
			for (int i = stringBin.length(); i < maxLength ; i++){
				stringBin = "0" + stringBin;
			}
		}		
		return stringBin;
	}


	private class ValidResults {

		int value;
		boolean valid;

		public ValidResults() {
			this.valid = false;
		}

		public ValidResults(int value) {
			this.value = value;
			this.valid = true;
		}
	}


	private CmdWithVar recognizeStringCommand(String strCommand, int row) {
		CmdWithVar cmd = new CmdWithVar();
		cmd.row = row;

		// Vieno baito komandos
		if (strCommand.equalsIgnoreCase("add")) {
			cmd.commandOpc = CommandBytecode.ADD;
			cmd.command = Command.ADD;
			return cmd;
		}

		if (strCommand.equalsIgnoreCase("sub")) {
			cmd.commandOpc = CommandBytecode.SUB;
			cmd.command = Command.SUB;
			return cmd;
		}

		if (strCommand.equalsIgnoreCase("cmp")) {
			cmd.commandOpc = CommandBytecode.CMP;
			cmd.command = Command.CMP;
			return cmd;
		}

		if (strCommand.equalsIgnoreCase("stop")) {
			cmd.commandOpc = CommandBytecode.STOP;
			cmd.command = Command.STOP;
			return cmd;
		}

		String opc = strCommand;
		String remainder = "";

		for (int i = 0; i < strCommand.length(); i++) {
			if (strCommand.charAt(i) == ' ') {
				opc = strCommand.substring(0, i).trim();
				if (opc.length() != strCommand.length()) {
					remainder = strCommand.substring(i, strCommand.length())
							.trim();
				}
				break;
			}
		}

		// Visos be parametru komandos patikrintos
		// jeigu nera parametru,
		// reiskias kitos komandos netiks
		if (remainder.equals("")) {
			return null;
		}

		if (remainder.contains(",")) {
			String first = "";
			String second = "";
			try {
				first = remainder.split(",")[0].trim();
				second = remainder.split(",")[1].trim();
			} catch (Exception e) {
				return null;
			}
			if (opc.equalsIgnoreCase("loa")) {
				if (second.equalsIgnoreCase("ax")) {
					if (loadRemainderIfValid(first, cmd, 2)) {
						cmd.commandOpc = CommandBytecode.LOA_AX;
						cmd.command = Command.LOA_AX;
						return cmd;
					}
				}

				if (second.equalsIgnoreCase("bx")) {
					if (loadRemainderIfValid(first, cmd, 2)) {
						cmd.commandOpc = CommandBytecode.LOA_BX;
						cmd.command = Command.LOA_BX;
						return cmd;
					}
				}
			}

			if (opc.equalsIgnoreCase("mov")) {
				if (first.equalsIgnoreCase("ax")) {
					if (loadRemainderIfValid(second, cmd, 16)) {
						cmd.commandOpc = CommandBytecode.MOV_AX;
						cmd.command = Command.MOV_AX;
						return cmd;
					}
				}

				if (first.equalsIgnoreCase("bx")) {
					if (loadRemainderIfValid(second, cmd, 16)) {
						cmd.commandOpc = CommandBytecode.MOV_BX;
						cmd.command = Command.MOV_BX;
						return cmd;
					}
				}
			}

			if (opc.equalsIgnoreCase("sto")) {
				if (first.equalsIgnoreCase("ax")) {
					if (loadRemainderIfValid(second, cmd, 2)) {
						cmd.commandOpc = CommandBytecode.STO_AX;
						cmd.command = Command.STO_AX;
						return cmd;
					}
				}

				if (first.equalsIgnoreCase("bx")) {
					if (loadRemainderIfValid(second, cmd, 2)) {
						cmd.commandOpc = CommandBytecode.STO_BX;
						cmd.command = Command.STO_BX;
						return cmd;
					}
				}
			}

		} else {
			// 1 parametro komandos
			if (opc.equalsIgnoreCase("push")) {
				if (loadRemainderIfValid(remainder, cmd, 2)) {
					cmd.commandOpc = CommandBytecode.PUSH;
					cmd.command = Command.PUSH;
					return cmd;
				}
			}

			if (opc.equalsIgnoreCase("pop")) {
				if (loadRemainderIfValid(remainder, cmd, 2)) {
					cmd.commandOpc = CommandBytecode.POP;
					cmd.command = Command.POP;
					return cmd;
				}
			}

			if (opc.equalsIgnoreCase("ja")) {
				if (loadRemainderIfValid(remainder, cmd, 2)) {
					cmd.commandOpc = CommandBytecode.JA;
					cmd.command = Command.JA;
					return cmd;
				}
			}

			if (opc.equalsIgnoreCase("jb")) {
				if (loadRemainderIfValid(remainder, cmd, 2)) {
					cmd.commandOpc = CommandBytecode.JB;
					cmd.command = Command.JB;
					return cmd;
				}
			}

			if (opc.equalsIgnoreCase("je")) {
				if (loadRemainderIfValid(remainder, cmd, 2)) {
					cmd.commandOpc = CommandBytecode.JE;
					cmd.command = Command.JE;
					return cmd;
				}
			}

			if (opc.equalsIgnoreCase("jne")) {
				if (loadRemainderIfValid(remainder, cmd, 2)) {
					cmd.commandOpc = CommandBytecode.JNE;
					cmd.command = Command.JNE;
					return cmd;
				}
			}

			if (opc.equalsIgnoreCase("jmp")) {
				if (loadRemainderIfValid(remainder, cmd, 2)) {
					cmd.commandOpc = CommandBytecode.JMP;
					cmd.command = Command.JMP;
					return cmd;
				}
			}

			if (opc.equalsIgnoreCase("outm")) {
				if (loadRemainderIfValid(remainder, cmd, 2)) {
					cmd.commandOpc = CommandBytecode.OUTM;
					cmd.command = Command.OUTM;
					return cmd;
				}
			}

			if (opc.equalsIgnoreCase("outr")) {
				if (remainder.equalsIgnoreCase("ax")) {
					cmd.commandOpc = CommandBytecode.OUTR_AX;
					cmd.command = Command.OUTR_AX;
					return cmd;
				}
				if (remainder.equalsIgnoreCase("bx")) {
					cmd.commandOpc = CommandBytecode.OUTR_BX;
					cmd.command = Command.OUTR_BX;
					return cmd;
				}
			}
		}
		return null;
	}

	private boolean loadRemainderIfValid(String remainder, CmdWithVar cmd,
			int length) {
		ValidResults vr = this.isValidHex(remainder, length);
		if (vr.valid) {
			cmd.variable = vr.value;
			return true;
		} else {
			return false;
		}
	}
        

	private ValidResults isValidHex(String remainder, int length) {
		if (remainder.length() <= length) {
			try {
				Integer.parseInt(remainder, 16);
			} catch (Exception e) {
				return new ValidResults();
			}
			return new ValidResults(Integer.parseInt(remainder, 16));
		}
		return new ValidResults();
	}


//	public String toString() {
//		return "";
//	}

	public int getProgramSize() {
		return programSize;
	}


	public void setProgramSize(int programSize) {
		this.programSize = programSize;
	}

}
