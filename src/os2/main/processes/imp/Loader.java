package os2.main.processes.imp;

import java.util.ArrayList;

import os2.main.Core;
import os2.main.hardware.ChannelsDevice.ChannelsDevice;
import os2.main.hardware.memory.VMMemory;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.descriptors.FromLoaderDescriptor;
import os2.main.resources.descriptors.LoaderPacketDescriptor;

/**
 * Šio proceso paskirtis – išorinėje atmintyje esančius blokus 
 * perkelti į vartotojo atmintį.
 * @author Tadas
 *
 */

public class Loader extends Process {
	
	private VMMemory vmm;
	private int[] programName;
	private JobGovernor jg;
	private ArrayList vars;

	@Override
	public void nextStep() {
		Resource res;
		switch (this.step) {
		case (0):
			// Blokuotas, laukiam resurso "Pakrovimo paketas"
			res = Core.resourceList.searchResource("LOADER_PACKET");
			if (res != null) {
				LoaderPacketDescriptor descriptor = (LoaderPacketDescriptor) res.getDescriptor();
				this.programName = descriptor.getFilename();
				this.vmm = descriptor.getMemory();
				this.vars = descriptor.getVars();
				this.jg = (JobGovernor) res.getParent();
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
			// Nustatom kanalų įrenginio registrus ir vykdom komandą XCHG
			ChannelsDevice.ST = 3; // Šaltinis: išorinė atmintis
			ChannelsDevice.DT = 1; // Tikslas: vartotojo atmintis
			ChannelsDevice.programName = this.programName;
			ChannelsDevice.vmm = this.vmm;
			ChannelsDevice.vars = this.vars;
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
			res = new Resource("FROM_LOADER");
			res.setParent(this.jg);
			FromLoaderDescriptor descriptor = new FromLoaderDescriptor();
			descriptor.setMessage("Programa " + this.programName + " iš išorinės atminties perkelta į vartotojo");
			res.setDescriptor(descriptor);
			Core.resourceList.addResource(new Resource("FROM_LOADER"));
			this.changeStep(0);
			break;
		}
	}

}
