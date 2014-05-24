package os2.main.processes.imp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import os2.main.Core;
import os2.main.hardware.memory.RMMemory;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.FromGUIDescriptor;
import os2.main.resources.descriptors.PrintDescriptor;
import os2.main.resources.descriptors.ProgramInSupervisorDescriptor;

/**
 * Šį procesą kuria ir naikina procesas „StartStop“. Proceso paskirtis – gavus
 * informaciją iš įvedimo srauto ir atlikus pirminį jos apdorojimą, atiduoti
 * informaciją tolesniam apdorojimui, kurį atliks procesas „JCL“.
 * 
 * @author domas
 */
public class ReadFromInterface extends Process {

	private String fileName;
	private int[] content;

	private int programStart;
	private int programEnd;

	@Override
	public void nextStep() {
		Resource res;
		switch (this.step) {
		case (0):
			// Blokuojam kol sulaukiam resurso "Iš vartojo sąsajos
			res = Core.resourceList
					.searchResource(ResourceType.PROGRAM_FROM_FLASH);
			if (res != null) {
				FromGUIDescriptor descriptor = (FromGUIDescriptor) res
						.getDescriptor();
				this.fileName = descriptor.getFileName();
				Core.resourceList.delete(ResourceType.PROGRAM_FROM_FLASH);
				this.changeStep(this.step + 1);
			} else {
				this.changeStep(0);
			}
			break;
		case (1):
			// Nuskaitom failą
			try {
				byte byteContent[] = Files.readAllBytes(Paths
						.get(this.fileName));
				this.content = new int[byteContent.length];
				for (int i = 0; i < byteContent.length; i++) {
					this.content[i] = (new Byte(byteContent[i])).intValue();
				}
				this.changeStep(this.step + 1);
			} catch (IOException e) {
				e.printStackTrace();
				Resource r = new Resource(ResourceType.LI_IN_MEM);
				r.setDescriptor(RMMemory.loadStringToMemory("Klaida: Failas: "
						+ this.fileName
						+ " neegzistuoja arba jo nepavyko atidaryti"));
				Core.resourceList.addResource(r);
				this.changeStep(0);
			}
			break;
		case (2):
			res = Core.resourceList.searchResource(ResourceType.SUP_MEM);
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
			res = Core.resourceList.searchResource(ResourceType.SUP_MEM);
			if (res != null) {
				if (res.getParent() == this) {
					res.removeParent();
					this.programStart = RMMemory
							.loadProgramToMemory(this.content);

					if (programStart > 0) {
						this.programEnd = this.programStart
								+ this.content.length + 1;
						this.changeStep(this.step + 1);
					} else {
						Resource r = new Resource(ResourceType.LI_IN_MEM);
						r.setDescriptor(RMMemory
								.loadStringToMemory("Klaida: Failas: "
										+ this.fileName
										+ " negali būti perkeltas į supervizorinę atmintį, nes yra per didelis"));
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
			Resource r = new Resource(ResourceType.PROGRAM_IN_SUPERVISOR);
			ProgramInSupervisorDescriptor descriptor = new ProgramInSupervisorDescriptor();
			descriptor.setProgramBegin(this.programStart);
			descriptor.setProgramEnd(this.programEnd);
			String[] name = this.fileName.split("/");
			descriptor.setProgramName(name[name.length - 1]);
			r.setDescriptor(descriptor);
			Core.resourceList.addResource(r);
			this.changeStep(0);
			break;
		}

	}

}
