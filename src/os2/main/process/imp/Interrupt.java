package os2.main.process.imp;

import os2.main.process.Process;

/**
 * Šio proceso paskirtis – reaguoti į pertraukimus, kilusius virtualios mašinos darbo metu.
 * @author domas
 *
 */
public class Interrupt extends Process {

	@Override
	public void nextStep() {
		switch (this.step) {
		case (0):
			// Blokuotas, laukiam resurso "Pertraukimas"
			break;
		case (1):
			// Pertraukimo tipo identifikavimas
			break;
		case (2):
			// "JobGorvernor" nustatymas atsakingo už pertraukimą
			break;
		case (3):
			// Sukuriamas "Iš interrupt" resursas skirtas konkrečiam "JobGovernor"
			break;
		}
	}

}
