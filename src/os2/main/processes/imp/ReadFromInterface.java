package os2.main.processes.imp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import os2.main.Core;
import os2.main.hardware.memory.RMMemory;
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
	private byte[] content;

	
	@Override
	public void nextStep() {
		Resource res;
		switch (this.step) {
		case (0):
			// Blokuojam kol sulaukiam resurso "Iš vartojo sąsajos
			res = Core.resourceList.searchResource("FROM_GUI");
			if (res != null) {
				this.fileName = (String) res.getInformation();
				this.step++;
			}
			break;
		case (1):
			// Nuskaitom failą
			try {
				this.content = Files.readAllBytes(Paths.get(this.fileName));
				this.step++;
			} catch (IOException e) {
				e.printStackTrace();
				Resource r = new Resource("PRINT");
				r.setInformation("Klaida: Failas: " + this.fileName + " neegzistuoja arba jo nepavyko atidaryti");
				Core.resourceList.addResource(r);
				this.step = 0;
			}			
			break;
		case (2):
			res = Core.resourceList.searchResource("SUPERVISOR");
			if (res != null) {
				if (res.getParent() == null) {
					res.setParent(this);
					this.step++;
				}
			}
			break;
		case (3):
			// Kopijuojam blokus į supervizorinę atmintį
			res = Core.resourceList.searchResource("SUPERVISOR");
			if (res != null) {
				if (res.getParent() == this) {
					res.removeParent();
					RMMemory.loadProgramToMemory(this.content);
					this.step++;
				}
			}
			break;
		case (4):
			// Sukuriamas resursas "Užduotis supervizorinėje atmintyje"
			Core.resourceList.addResource(new Resource("PROGRAM_IN_SUPERVISOR"));
			this.step = 0;
			break;
		}

	}

}
