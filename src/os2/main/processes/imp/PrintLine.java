package os2.main.processes.imp;

import os2.main.Core;
import os2.main.hardware.ChannelsDevice.ChannelsDevice;
import os2.main.hardware.memory.RMMemory;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.PrintDescriptor;

/**
 * Šio proceso paskirtis – į išvedimo srautą pasiųsti 
 * kokioje nors atmintyje esantį pranešimą.
 * @author Tadas
 *
 */
public class PrintLine extends Process {
	
	private int startAddress;
	private int endAddress;

	@Override
	public void nextStep() {
		Resource res;
		switch (this.step) {
		case (0):
			// Blokuotas, laukiam "Eilutė atmintyje" resurso
			res = Core.resourceList.searchResource(ResourceType.LI_IN_MEM);
			if (res != null) {
				PrintDescriptor descriptor = (PrintDescriptor) res.getDescriptor();
				res.setParent(this);
				this.startAddress = descriptor.getStartAddress();
				this.endAddress = descriptor.getEndAddress();
				this.changeStep(1);
			}
			else {
				this.changeStep(0);
			}
			break;
		case (1):
			// Blokuotas, laukiam "Kanalų įrenginio" resurso
			res = Core.resourceList.searchResource(ResourceType.CH_DEV);
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
		case (2):
			// Nustatinėjami įrenginio registra ir įvygdoma komanda XCHG
			ChannelsDevice.ST = 2;
			ChannelsDevice.DT = 4;
			ChannelsDevice.startAddress = this.startAddress;
			ChannelsDevice.endAddress = this.endAddress;
			if (ChannelsDevice.XCHG()) {
				this.changeStep(3);
			} else {
				this.changeStep(2);
			}
			break;
		case (3):
			// Atlaisvinamas "Kanalų įrenginys" resursas
			res = Core.resourceList.searchResource(ResourceType.CH_DEV);
			res.removeParent();
			Core.resourceList.deleteChildResource(this, ResourceType.LI_IN_MEM);
			this.changeStep(0);
			break;
		}
	}

}
