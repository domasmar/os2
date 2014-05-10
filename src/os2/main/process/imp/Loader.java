package os2.main.process.imp;

import os2.main.process.Process;

/**
 * Šio proceso paskirtis – išorinėje atmintyje esančius blokus 
 * perkelti į vartotojo atmintį.
 * @author domas
 *
 */
public class Loader extends Process {

	@Override
	public void nextStep() {
		switch (this.step) {
		case (0):
			// Blokuotas, laukiam resurso "Pakrovimo paketas"
			break;
		case (1):
			// Blokuotas, laukiam resurso "Kanalų įrenginys"
			break;
		case (2):
			// Nustatom kanalų įrenginio registrus ir vygdom komandą XCHG
			break;
		case (3):
			// Atlaisvinam "Kanalų įrenginys" resursą
			break;
		case (4):
			// Sukuriamas "Iš loader" resursas, skirtas JobGovernor procesui
			// sukūrusiam gautąjį "Pakrovimo paketo" resursą
			break;
		}
	}

}
