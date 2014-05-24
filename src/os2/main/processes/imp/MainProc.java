package os2.main.processes.imp;

import os2.main.Core;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.ProgramInHDDDescriptor;

/**
 * Šio proceso paskirtis – kurti ir naikinti procesus „JobGorvernor“.
 * 
 * @author domas
 * 
 */
public class MainProc extends Process {

	private Resource resourceToDestroy;
	private int pidToDestroy;

	@Override
	public void nextStep() {
		Resource res;
		ProgramInHDDDescriptor des;
		switch (this.step) {
		case (0):
			// Blokuojam, laukiam resurso "Užduotis būge" resurso
			res = Core.resourceList.searchChildResource(null, ResourceType.PROGRAM_IN_HDD);
			if (res != null) {
				this.changeStep(1);
			} else {
				this.changeStep(0);
			}
			break;
		case (1):
			// Tikrinam vygdymo laiką
			// step = time > 0 ? 2 : 3
			res = Core.resourceList.searchChildResource(null, ResourceType.PROGRAM_IN_HDD);
			res.setParent(this);
			des = (ProgramInHDDDescriptor) res.getDescriptor();
			if (des.isFromJobToSwap()) {
				this.changeStep(2);
			} else {
				this.resourceToDestroy = res;
				this.pidToDestroy = resourceToDestroy.getParent().getPid();
				this.changeStep(3);
			}
			break;
		case (2):
			// Kuriamas procesas JobGovernor ir
			// resursas "Užduotis bugne" perduodamas jam
			res = Core.resourceList.searchChildResource(this,
					ResourceType.PROGRAM_IN_HDD);
			JobGovernor jg = new JobGovernor(res);
			res.setParent(jg);
			Core.processQueue.add(jg);
			this.changeStep(0);
			break;
		case (3):
			// Naikinamas procesas "JobGovernor" sukūręs gautajį resursą
			Core.resourceList.deleteByInstance(this.resourceToDestroy);
			Core.processQueue.delete(this.pidToDestroy);
			this.changeStep(0);
			break;

		}
	}

}
