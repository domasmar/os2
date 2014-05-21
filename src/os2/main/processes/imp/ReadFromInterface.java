package os2.main.processes.imp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import os2.main.Core;
import os2.main.hardware.memory.RMMemory;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.descriptors.FromGUIDescriptor;
import os2.main.resources.descriptors.PrintDescriptor;

/**
 * Šį procesą kuria ir naikina procesas „StartStop“. Proceso paskirtis – gavus
 * informaciją iš įvedimo srauto ir atlikus pirminį jos apdorojimą, atiduoti
 * informaciją tolesniam apdorojimui, kurį atliks procesas „JCL“.
 * 
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
				FromGUIDescriptor descriptor = (FromGUIDescriptor) res
						.getDescriptor();
				this.fileName = descriptor.getFileName();
				Core.resourceList.delete("FROM_GUI");
				this.changeStep(this.step + 1);
			} else {
				this.changeStep(0);
			}
			break;
		case (1):
			// Nuskaitom failą
			try {
				this.content = Files.readAllBytes(Paths.get(this.fileName));
				this.changeStep(this.step + 1);
			} catch (IOException e) {
				e.printStackTrace();
				Resource r = new Resource("PRINT");
				PrintDescriptor printDescriptor = new PrintDescriptor();
				printDescriptor.setMessage("Klaida: Failas: " + this.fileName
						+ " neegzistuoja arba jo nepavyko atidaryti");
				r.setDescriptor(printDescriptor);
				Core.resourceList.addResource(r);
				this.changeStep(0);
			}
			break;
		case (2):
			res = Core.resourceList.searchResource("SUPERVISOR");
			if (res != null) {
				if (res.getParent() == null) {
					res.setParent(this);
					this.changeStep(this.step + 1);
				} else {
					this.changeStep(2);
				}
			} else {
				this.changeStep(2);
			}
			break;
		case (3):
			// Kopijuojam blokus į supervizorinę atmintį
			res = Core.resourceList.searchResource("SUPERVISOR");
			if (res != null) {
				if (res.getParent() == this) {
					res.removeParent();
					if (RMMemory.loadProgramToMemory(this.content)) {
						this.changeStep(this.step + 1);
					} else {
						Resource r = new Resource("PRINT");
						PrintDescriptor printDescriptor = new PrintDescriptor();
						printDescriptor.setMessage("Klaida: Failas: "
								+ this.fileName
								+ " negali būti perkeltas į supervizorinę atmintį, nes yra per didelis");
						r.setDescriptor(printDescriptor);
						Core.resourceList.addResource(r);
						this.changeStep(0);
					}

				} else {
					this.changeStep(2);
				}
			} else {
				this.changeStep(2);
			}
			break;
		case (4):
			// Sukuriamas resursas "Užduotis supervizorinėje atmintyje"
			Core.resourceList
					.addResource(new Resource("PROGRAM_IN_SUPERVISOR"));
			this.changeStep(0);
			break;
		}

	}

}
