package os2.main.processes.imp;

import os2.main.Core;
import os2.main.processes.Process;
import os2.main.resources.Resource;

/**
 * Šio proceso paskirtis – perkelti užduoties programos blokus iš supervizorinės atminties į
 * išorinę.
 * @author domas
 *
 */
public class JobToSwap extends Process {

	@Override
	public void nextStep() {
		Resource res;
		switch (this.step) {
		case (0):
			// Blokuotas, laukiam resurso "Užduoties vygdymo parametrai
			// supervizorinėje atmintyje" resurso,
			res = Core.resourceList.searchResource("EXECUTION_PARAMETERS");
			if (res != null) {
				this.changeStep(this.step + 1);
			}
			else {
				this.changeStep(0);
			}
			break;
		case (1):
			// Blokuotas laukiant "Išorinė atmintis" resurso
			res = Core.resourceList.searchResource("HDD");
			if (res != null) {
				this.changeStep(this.step + 1);
			}
			else {
				this.changeStep(1);
			}
			break;
		case (2):
			// Blokuotas laukiant "Kanalų įrenginys" resurso
			res = Core.resourceList.searchResource("CHANNELS_DEVICE");
			if (res != null) {
				this.changeStep(this.step + 1);
			}
			else {
				this.changeStep(2);
			}
			break;
		case (3):
			// Nustatinėjami kanalų įrenginio registra ir vygdoma komanda "XCHG"
			break;
		case (4):
			// Atlaisvinamas "Kanalo įrenginys" resursas
			break;
		case (5):
			// Sukuriamas "Užduotis bugne" resursas
			this.step = 0;
			break;
		}
	}

}
