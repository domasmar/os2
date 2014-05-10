package os2.main.process.imp;

import os2.main.process.Process;

/**
 * Šio proceso paskirtis – į išvedimo srautą pasiųsti 
 * kokioje nors atmintyje esantį pranešimą.
 * @author domas
 *
 */
public class PrintLine extends Process {

	@Override
	public void nextStep() {
		switch (this.step) {
		case (0):
			// Blokuotas, laukiam "Eilutė atmintyje" resurso 
			break;
		case (1):
			// Blokuotas, laukiam "Kanalų įrenginio" resurso
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
