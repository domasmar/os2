package os2.main.processes.imp;

import os2.main.Core;
import os2.main.hardware.CPU;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;

/**
 * Procesą „VirtualMachine“ kuria ir naikina procesas
 * „JobGorvernor“.„VirtualMachine“ paskirtis yra vykdyti vartotojo užduoties
 * programą. Procesų „VirtualMachine“ yra tiek pat, kiek ir procesų
 * „JobGorvernor“.
 *
 * @author domas
 *
 */
public class VirtualMachine extends Process {

    @Override
    public void nextStep() {
        switch (this.step) {
            case (0):   
                Resource vm = Core.resourceList.searchChildResource(this, ResourceType.VIRT_MEM);
                if (vm != null){
                    CPU.MODE = 0;
                    this.changeStep(1);
                } else {
                    this.changeStep(0);
                }
                
                // Perjungiam procesorių į vartotojo režimą
                break;
            case (1):
                
                
                
                
	        // Veikia tol kol įvyksta pertraukimas,
                // Išsaugom procesoriaus būseną
                // Sukuriamas resursas "Pertraukimas"
                break;
            case (2):
                // Atslaivinamas resursas "Pertraukimas"
                break;

            case (3):

        }

    }

}
