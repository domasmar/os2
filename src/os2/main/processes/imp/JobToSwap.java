package os2.main.processes.imp;

import os2.main.Core;
import os2.main.hardware.ChannelsDevice.ChannelsDevice;
import os2.main.processes.Process;
import os2.main.resources.Resource;

/**
 * Šio proceso paskirtis – perkelti užduoties programos blokus iš supervizorinės atminties į
 * išorinę.
 * @author Tadas
 *
 */

public class JobToSwap extends Process {

	@Override
	public void nextStep() {
		int programAddressInSupMemory = -1;
		String programName;
		Resource res;
		switch (this.step) {
		case (0):
			// Blokuotas, laukiam resurso "Užduoties vygdymo parametrai
			// supervizorinėje atmintyje" resurso,
			res = Core.resourceList.searchResource("EXECUTION_PARAMETERS");
			if (res != null) {
				programAddressInSupMemory = (int) res.getInformation(); // išsisaugau programos pradžios supervizorinėje atmintyje adresą
				programName = (String) res.getInformation(); // reikalingas programos pavadinimas
				this.changeStep(1);
			}
			else {
				this.changeStep(0);
			}
			break;
		case (1):
			// Blokuotas laukiant "Išorinė atmintis" resurso
			res = Core.resourceList.searchResource("HDD");
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
			res = Core.resourceList.searchResource("CHANNELS_DEVICE");
			if (res != null) {
				if (res.getParent() == null || res.getParent() == this) {
					res.setParent(this);
					this.changeStep(this.step + 1);
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
			ChannelsDevice.SB = programAddressInSupMemory;
			ChannelsDevice.XCHG(); // įrašoma programą iš supervizorinės atminties į išorinę
			this.changeStep(4);
			break;
		case (4):
			// Atlaisvinamas "Kanalo įrenginys" resursas
			res = Core.resourceList.searchResource("CHANNELS_DEVICE");
			res.removeParent();
			this.changeStep(this.step + 1);
			break;
		case (5):
			// Sukuriamas "Užduotis bugne" resursas
			Core.resourceList.addResource(new Resource("PROGRAM_IN_HDD"));
			this.changeStep(0);
			break;
		}
	}

}
