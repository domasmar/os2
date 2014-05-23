package os2.main.processes.imp;

import os2.main.Core;
import os2.main.hardware.memory.RMMemory;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.PrintDescriptor;

/**
 * Šio proceso paskirtis – į išvedimo srautą pasiųsti 
 * kokioje nors atmintyje esantį pranešimą.
 * @author domas
 *
 */
public class PrintLine extends Process {

	@Override
	public void nextStep() {
		Resource res;
		switch (this.step) {
		case (0):
			// Blokuotas, laukiam "Eilutė atmintyje" resurso
			res = Core.resourceList.searchResource(ResourceType.LI_IN_MEM);
			if (res != null) {
				// LAIKINAI
				PrintDescriptor des = (PrintDescriptor) res.getDescriptor();
				System.out.println(des.getMessage());
				// 
				this.changeStep(1);
			}
			else {
				this.changeStep(0);
			}
			break;
		case (1):
			// Blokuotas, laukiam "Kanalų įrenginio" resurso
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
		case (2):
			// Nustatinėjami įrenginio registra ir įvygdoma komanda XCHG
			break;
		case (3):
			// Atlaisvinamas "Kanalų įrenginys" resursas
			break;
		}
	}

}
