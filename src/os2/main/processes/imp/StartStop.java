package os2.main.processes.imp;

import os2.main.Core;
import os2.main.processes.Process;
import os2.main.processes.ProcessStatus;
import os2.main.processes.ProcessType;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;

/**
 * Šis procesas atsakingas už sistemos darbo pradžią ir pabaigą. Įjungus kompiuterį šis procesas
 * pasileidžia automatiškai. Šio proceso paskirtis – sisteminių procesų ir resursų kūrimas.
 * @author domas
 */
public class StartStop extends Process {

	public StartStop() {
		this.type = ProcessType.SYSTEM;
	}

	@Override
	public void nextStep() {
		switch (step) {
		case 0:
			// Sisteminiu resursų inicializacija
			Core.resourceList.addResource(new Resource(ResourceType.EXT_MEM));
			Core.resourceList.addResource(new Resource(ResourceType.USER_MEM));
			Core.resourceList.addResource(new Resource(ResourceType.CPU));
			Core.resourceList.addResource(new Resource(ResourceType.SUP_MEM));
			Core.resourceList.addResource(new Resource(ResourceType.CH_DEV));
			this.changeStep(this.step + 1);
			break;
		case 1:
			// Sisteminių permanentinių procesų inicializacija
			Core.processQueue.add(new ReadFromInterface());
			Core.processQueue.add(new JCL());
			Core.processQueue.add(new JobToSwap());
			Core.processQueue.add(new Loader());
			Core.processQueue.add(new MainProc());
			Core.processQueue.add(new Interrupt());
			Core.processQueue.add(new PrintLine());
			this.changeStep(this.step + 1);
			break;
		case 2:
			// Blokavimas laukiant "OS pabaiga" resurso
			if (Core.resourceList.searchResource(ResourceType.END) != null) {
				this.changeStep(this.step + 1);
			} else {
				this.changeStep(2);
			}			
			break;
		case 3:
			// Sisteminių procesų naikinimas
			Core.processQueue.deleteAll();
			this.changeStep(this.step + 1);
			break;
		case 4:
			// Sisteminių resursų naikinimas
			Core.resourceList.deleteAll();
			Core.running = false;
			break;
		}
	}

}
