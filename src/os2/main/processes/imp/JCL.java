package os2.main.processes.imp;

import java.util.ArrayList;
import java.util.Iterator;

import os2.main.Core;
import os2.main.hardware.memory.RMMemory;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.ExecParamsDescriptor;
import os2.main.resources.descriptors.PrintDescriptor;
import os2.main.resources.descriptors.ProgramInSupervisorDescriptor;
import os2.main.software.commandsConverter.CommandsConverter;
import os2.main.software.commandsConverter.Variable;
import os2.main.software.executor.Compiler;

/**
 * Proceso „JCL“ paskirtis – gautus blokus iš „ReadFromInterface“ suskirstyti į
 * antraštės blokus ir programos blokus, ir atidavus juos procesui „JobToSwap“,
 * vėl blokuotis laukiant pranešimo iš „ReadFromInterface“.
 * 
 * @author domas
 * 
 */
public class JCL extends Process {

	// private Resource programResource;

	private int[] byteCode;
	private String programName;

	@Override
	public void nextStep() {
		switch (this.step) {
		case 0:
			if (Core.resourceList
					.searchResource(ResourceType.PROGRAM_IN_SUPERVISOR) != null) {
				this.changeStep(1);
			} else {
				this.changeStep(0);
			}
			break;
		case 1:
			// Tikrinamas programos validumas
			// Jei programa nekorektiška, gražiname atitinkamą klaidos pranešimą
			Resource r = Core.resourceList
					.searchResource(ResourceType.PROGRAM_IN_SUPERVISOR);
			r.setParent(this);
			ProgramInSupervisorDescriptor descriptor = (ProgramInSupervisorDescriptor) r
					.getDescriptor();
			this.programName = descriptor.getProgramName();
			Core.resourceList.deleteChildResource(this,
					ResourceType.PROGRAM_IN_SUPERVISOR);

			byte[] program = RMMemory.getByteProgramFromMemory(
					descriptor.getProgramBegin(), descriptor.getProgramEnd());

			String programCode = new String(program);
			try {
				CommandsConverter cc = new CommandsConverter(programCode);
				String[] commands = cc.getCommands();
				ArrayList<Variable> vars = cc.getVariables();
				Compiler compiler = new Compiler();
				this.byteCode = compiler.compile(commands);

				Iterator<Variable> varsIterator = vars.iterator();
				int[] intVars = new int[vars.size() + 1];
				int index = 0;
				while (varsIterator.hasNext()) {
					Variable v = varsIterator.next();
					intVars[index++] = v.getValue();
				}
				intVars[index] = RMMemory.VARS_SEPARATOR;
				this.byteCode = this.concat(intVars, this.byteCode);

				this.changeStep(2);
			} catch (Exception e) {
				Resource resource = new Resource(ResourceType.LI_IN_MEM);
				resource.setDescriptor(RMMemory.loadStringToMemory(e.getMessage()));
				Core.resourceList.addResource(resource);
				this.changeStep(0);
			}
			break;
		case 2:
			// Sukuriamas resursas
			// "Užduoties vygdymo parametrai supervizorinėje atmintyje"
			int byteCodeStart = RMMemory.loadProgramToMemory(this.byteCode);
			ExecParamsDescriptor execDescriptor = new ExecParamsDescriptor();
			execDescriptor.setProgramName(this.programName);
			execDescriptor.setStartAddress(byteCodeStart);
			execDescriptor.setEndAddress(byteCodeStart + this.byteCode.length
					+ 1);
			Resource res = new Resource(ResourceType.EXEC_PAR);
			res.setDescriptor(execDescriptor);
			Core.resourceList.addResource(res);
			this.changeStep(0);
			break;
		}
	}

	private int[] concat(int[] A, int[] B) {
		int aLen = A.length;
		int bLen = B.length;
		int[] C = new int[aLen + bLen];
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);
		return C;
	}
}
