package os2.main.processes.imp;

import java.io.File;

import os2.main.Core;
import os2.main.processes.Process;
import os2.main.resources.Resource;

/**
 * Šį procesą kuria ir naikina procesas „StartStop“. Proceso paskirtis – gavus informaciją iš
 * įvedimo srauto ir atlikus pirminį jos apdorojimą, atiduoti informaciją tolesniam apdorojimui, kurį
 * atliks procesas „JCL“.
 * @author domas 
*/
public class ReadFromInterface extends Process {

	private String fileName;
	
	@Override
	public void nextStep() {

		switch (this.step) {
		case (0):
			// Blokuojam kol sulaukiam resurso "Iš vartojo sąsajos
			Resource res = Core.resourceList.searchResource("FROM_GUI");
			if (res != null) {
				this.fileName = (String) res.getInformation();
				this.step++;
			}
			break;
		case (1):
			// Nuskaitom failą
			
			break;
		case (2):
			// Blokuojam kol sulaukiam resurso "Supervizorinė atmintis"
			
			break;
		case (3):
			// Kopijuojam blokus į supervizorinę atmintį
			break;
		case (4):
			// Sukuriamas resursas "Užduotis supervizorinėje atmintyje"
			
			this.step = 0;
			break;
		}

	}

}
