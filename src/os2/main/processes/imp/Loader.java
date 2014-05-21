package os2.main.processes.imp;

import os2.main.Core;
import os2.main.hardware.ChannelsDevice.ChannelsDevice;
import os2.main.processes.Process;
import os2.main.resources.Resource;

/**
 * Šio proceso paskirtis – išorinėje atmintyje esančius blokus 
 * perkelti į vartotojo atmintį.
 * @author Tadas
 *
 */

public class Loader extends Process {

	@Override
	public void nextStep() {
		Resource res;
		String programName;
		int destinationAddress;
		switch (this.step) {
		case (0):
			// Blokuotas, laukiam resurso "Pakrovimo paketas"
			res = Core.resourceList.searchResource("LOADER_PACKET");
			if (res != null) {
				programName = (String) res.getInformation(); // gausiu iš Artūro programos pavadinimą
				destinationAddress = (int) res.getInformation(); // iš kur gauti vartotojo atminties adresą, kur rašyti programą?
				this.changeStep(1);
			} else {
				this.changeStep(0);
			}
			break;
		case (1):
			// Blokuotas, laukiam resurso "Kanalų įrenginys"
			res = Core.resourceList.searchResource("CHANNELS_DEVICE");
			if (res != null) {
				if (res.getParent() == null) {
					res.setParent(this);
					this.changeStep(2);
				} else {
					this.changeStep(1);
				}
			} else {
				this.changeStep(1);
			}
			break;
		case (2):
			// Nustatom kanalų įrenginio registrus ir vygdom komandą XCHG
			ChannelsDevice.ST = 3; // Šaltinis: išorinė atmintis
			ChannelsDevice.DT = 1; // Tikslas: vartotojo atmintis
			// ChannelsDevice.DB = vieta vartotojo atmintyje
			ChannelsDevice.XCHG();
			this.changeStep(3);
			break;
		case (3):
			// Atlaisvinam "Kanalų įrenginys" resursą
			res = Core.resourceList.searchResource("CHANNELS_DEVICE");
			res.removeParent();
			this.changeStep(4);
			break;
		case (4):
			// Sukuriamas "Iš loader" resursas, skirtas JobGovernor procesui
			// sukūrusiam gautąjį "Pakrovimo paketo" resursą
			Core.resourceList.addResource(new Resource("FROM_LOADER"));
			this.changeStep(0);
			break;
		}
	}

}
