package os2.main.process.imp;

import os2.main.process.Process;

/**
 * Šio proceso paskirtis – perkelti užduoties programos blokus iš supervizorinės atminties į
 * išorinę.
 * @author domas
 *
 */
public class JobToSwap extends Process {

	@Override
	public void nextStep() {
		switch (this.step) {
		case (0):
			// Blokuotas, laukiam resurso "Užduoties vygdymo parametrai
			// supervizorinėje atmintyje" resurso
			break;
		case (1):
			// Blokuotas laukiant "Išorinė atmintis" resurso
			break;
		case (2):
			// Blokuotas laukiant "Kanalų įrenginys" resurso
			break;
		case (3):
			// Nustatinėjami kanalų įrenginio registra ir vygdoma komanda "XCHG"
			break;
		case (4):
			// Atlaisvinamas "Kanalo įrenginys" resursas
			break;
		case (5):
			// Sukuriamas "Užduotis bugne" resursas
			this.step = 0;
			break;
		}
	}

}
