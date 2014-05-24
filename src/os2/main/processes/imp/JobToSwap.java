package os2.main.processes.imp;

import java.util.ArrayList;

import os2.main.Core;
import os2.main.hardware.ChannelsDevice.ChannelsDevice;
import os2.main.hardware.HDD.Utilities;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.ExecParamsDescriptor;
import os2.main.resources.descriptors.ProgramInHDDDescriptor;

/**
 * Šio proceso paskirtis – perkelti užduoties programos blokus iš supervizorinės atminties į
 * išorinę.
 * @author Tadas
 *
 */

public class JobToSwap extends Process {
	
	private int[] programName;
	private int startAddress;
	private int endAddress;
	private ArrayList vars;

	@Override
	public void nextStep() {
		Resource res;
		switch (this.step) {
		case (0):
			// Blokuotas, laukiam resurso "Užduoties vykdymo parametrai
			// supervizorinėje atmintyje" resurso
			res = Core.resourceList.searchResource(ResourceType.EXEC_PAR);
			if (res != null) {
				ExecParamsDescriptor descriptor = (ExecParamsDescriptor) res.getDescriptor();
				this.startAddress = descriptor.getStartAddress();
				this.endAddress = descriptor.getEndAddress();
				this.vars = descriptor.getVars();
				try {
					this.programName = Utilities.getFilenameAsInts(descriptor.getProgramName());
				} catch (Exception e) {
					System.out.println("Nepavyko paversti programos pavadinimo į integerių masyvą!");
					e.printStackTrace();
				}
				this.changeStep(1);
			}
			else {
				this.changeStep(0);
			}
			break;
		case (1):
			// Blokuotas laukiant "Išorinė atmintis" resurso
			res = Core.resourceList.searchResource(ResourceType.EXT_MEM);
			if (res != null) {
				if (res.getParent() == null || res.getParent() == this) {
					res.setParent(this);
					this.changeStep(2);
				}
				else {
					this.changeStep(1);
				}
			}
			else {
				this.changeStep(1);
			}
			break;
		case (2):
			// Blokuotas laukiant "Kanalų įrenginys" resurso
			res = Core.resourceList.searchResource(ResourceType.CH_DEV);
			if (res != null) {
				if (res.getParent() == null || res.getParent() == this) {
					res.setParent(this);
					this.changeStep(3);
				}
				else {
					this.changeStep(2);
				}
			}
			else {
				this.changeStep(2);
			}
			break;
		case (3):
			// Nustatinėjami kanalų įrenginio registra ir vygdoma komanda "XCHG"
			ChannelsDevice.ST = 2; // Šaltinis: supervizorinė atmintis
			ChannelsDevice.DT = 3; // Tikslas: išorinė atmintis
			ChannelsDevice.startAddress = this.startAddress;
			ChannelsDevice.endAddress = this.endAddress;
			ChannelsDevice.programName = this.programName;
			if (ChannelsDevice.XCHG()) { // įrašoma programą iš supervizorinės atminties į išorinę
				this.changeStep(4);
			}
			else {
				this.changeStep(3);
			}
			break;
		case (4):
			// Atlaisvinamas "Kanalo įrenginys" resursas
			res = Core.resourceList.searchResource(ResourceType.CH_DEV);
			res.removeParent();
			this.changeStep(5);
			break;
		case (5):
			// Sukuriamas "Užduotis bugne" resursas
			ProgramInHDDDescriptor descriptor = new ProgramInHDDDescriptor();
			descriptor.setProgramName(this.programName);
			descriptor.setVars(this.vars);
			res = new Resource(ResourceType.PROGRAM_IN_HDD);
			res.setDescriptor(descriptor);
			Core.resourceList.addResource(res);
			Core.resourceList.delete(ResourceType.EXEC_PAR);
			this.changeStep(0);
			break;
		}
	}

}
