package os2.main.processes.imp;

import java.io.UnsupportedEncodingException;

import os2.main.Core;
import os2.main.hardware.ChannelsDevice.ChannelsDevice;
import os2.main.hardware.HDD.Utilities;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.descriptors.ExecParamsDescriptor;

/**
 * Šio proceso paskirtis – perkelti užduoties programos blokus iš supervizorinės atminties į
 * išorinę.
 * @author Tadas
 *
 */

public class JobToSwap extends Process {
	
	private int[] programName;
	private int addressInSupMemory;

	@Override
	public void nextStep() {
		Resource res;
		switch (this.step) {
		case (0):
			// Blokuotas, laukiam resurso "Užduoties vykdymo parametrai
			// supervizorinėje atmintyje" resurso,
			res = Core.resourceList.searchResource("EXECUTION_PARAMETERS");
			if (res != null) {
				ExecParamsDescriptor descriptor = (ExecParamsDescriptor) res.getDescriptor();
				this.addressInSupMemory = descriptor.getAddress();
				try {
					this.programName = Utilities.getFilenameAsInts(descriptor.getProgramName());
				} catch (UnsupportedEncodingException e) {
					System.out.println("Nepavyko paversti programos pavadinimo į integerių masyvą!");
					e.printStackTrace();
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
			ChannelsDevice.SB = this.addressInSupMemory;
			ChannelsDevice.programName = this.programName;
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
