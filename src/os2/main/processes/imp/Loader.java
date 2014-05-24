package os2.main.processes.imp;

import os2.main.Core;
import os2.main.hardware.ChannelsDevice.ChannelsDevice;
import os2.main.hardware.memory.VMMemory;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
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

	@Override
	public void nextStep() {
		Resource res;
		switch (this.step) {
		case (0):
			// Blokuotas, laukiam resurso "Pakrovimo paketas"
			res = Core.resourceList.searchResource(ResourceType.LOAD_PACK);
			if (res != null) {
				LoaderPacketDescriptor descriptor = (LoaderPacketDescriptor) res.getDescriptor();
				res.setParent(this);
				this.programName = descriptor.getFilename();
				this.vmm = descriptor.getMemory();
				this.jg = (JobGovernor) res.getParent();
				this.changeStep(1);
			} else {
				this.changeStep(0);
			}
			break;
		case (1):
			// Blokuotas, laukiam resurso "Kanalų įrenginys"
			res = Core.resourceList.searchResource(ResourceType.CH_DEV);
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
			ChannelsDevice.XCHG();
			this.changeStep(3);
			break;
		case (3):
			// Atlaisvinam "Kanalų įrenginys" resursą
			res = Core.resourceList.searchResource(ResourceType.CH_DEV);
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
			Core.resourceList.deleteChildResource(this, ResourceType.LOAD_PACK);
			this.changeStep(0);
			break;
		}
	}

}
