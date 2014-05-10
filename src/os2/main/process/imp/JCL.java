package os2.main.process.imp;

import os2.main.process.Process;

/**
 * Proceso „JCL“ paskirtis – gautus blokus iš „ReadFromInterface“ suskirstyti į antraštės blokus
 * ir programos blokus, ir atidavus juos procesui „JobToSwap“, vėl blokuotis laukiant pranešimo iš
 * „ReadFromInterface“.
 * @author domas
 *
 */
public class JCL extends Process {

	@Override
	public void nextStep() {
		switch(this.step) {
		case 0:
			// Blokuojamas procesas, laukiamas resursas "Užduotis supervizorinėje atmintyje"
			break;
		case 1:
			// Tikrinamas programos validumas
			// Jei programa nekorektiška, gražiname atitinkamą klaidos pranešimą
			break;
		case 2:
			// Sukuriamas resursas "Užduoties vygdymo parametrai supervizorinėje atmintyje"
			break;
		}
	}
}
