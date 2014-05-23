package os2.main.processes.imp;

import java.util.ArrayList;

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

	private ArrayList vars;
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
				this.vars = cc.getVariables();
				Compiler compiler = new Compiler();
				this.byteCode = compiler.compile(commands);
				this.changeStep(2);
			} catch (Exception e) {
				Resource resource = new Resource(ResourceType.LI_IN_MEM);
				PrintDescriptor printDescriptor = new PrintDescriptor();
				printDescriptor.setMessage(e.getMessage());
				resource.setDescriptor(printDescriptor);
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
			execDescriptor.setAddress(byteCodeStart);
			execDescriptor.setVars(this.vars);
			Resource res = new Resource(ResourceType.EXEC_PAR);
			res.setDescriptor(execDescriptor);
			Core.resourceList.addResource(res);
			this.changeStep(0);
			break;
		}
	}
}
