package os2.main.processes.imp;

import os2.main.processes.Process;
import os2.main.processes.ProcessStatus;
import os2.main.processes.ProcessType;

/**
 * Šis procesas atsakingas už sistemos darbo pradžią ir pabaigą. Įjungus kompiuterį šis procesas
 * pasileidžia automatiškai. Šio proceso paskirtis – sisteminių procesų ir resursų kūrimas.
 * @author domas
 */
public class StartStop extends Process {

	public StartStop(Process parent) {
		this.type = ProcessType.SYSTEM;
	}

	@Override
	public void nextStep() {
		switch (step) {
		case 0:
			// Sisteminiu resursų inicializacija

			break;
		case 1:
			// Sisteminių permanentinių resursų inicializacija

			break;
		case 2:
			// Blokavimas laukiant "OS pabaiga" resurso
			this.status = ProcessStatus.BLOCKED;

			break;
		case 3:
			// Sisteminių procesų naikinimas

			break;
		case 4:
			// Sisteminių resursų naikinimas

			break;

		}
	}

}
