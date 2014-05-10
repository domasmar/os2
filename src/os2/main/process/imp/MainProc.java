package os2.main.process.imp;

import os2.main.process.Process;

/**
 * Šio proceso paskirtis – kurti ir naikinti procesus „JobGorvernor“.
 * @author domas
 *
 */
public class MainProc extends Process {

	@Override
	public void nextStep() {
		switch (this.step){
		case (0):
			// Blokuojam, laukiam resurso "Užduotis būge" resurso
			break;
		case (1):
			// Tikrinam vygdymo laiką
			// step = time > 0 ? 2 : 3
			break;
		case (2):
			// Kuriamas procesas JobGovernor ir 
			// resursas "Užduotis bugne" perduodamas jam
			break;
		case (3):
			// Naikinamas procesas "JobGovernor" sukūręs gautajį resursą
			break;

		}
	}

}
