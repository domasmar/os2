package os2.main.processes.imp;

import os2.main.Core;
import os2.main.processes.Process;
import os2.main.resources.Resource;

/**
 * Proceso „JobGorvernor“ paskirtis – kurti, naikinti ir padėti procesui
 * „VirtualMachine“ atlikti savo darbą, t. y. atlikti veiksmus, kurių
 * „VirtualMachine“, procesoriui dirbant vartotojo režimu, nesugeba atlikti.
 * Vienas „JobGorvernor“ aptarnauja vieną virtualią mašiną.
 *
 * @author domas
 *
 */
public class JobGovernor extends Process {

    @Override
    public void nextStep() {
        switch (this.step) {
            case (0):
                Resource memory = Core.resourceList.searchResource("Memory");
                if (memory != null) {
                    this.changeStep(1);
                } else {
                    this.changeStep(0);
                }
                // Blokuotas, laukiam "Vartotojo atmintis" resurso
                break;
            case (1):
                Core.resourceList.addResource(new Resource("loaderpacket"));
                
                // Sukuriamas resursas "Pakrovimo paketas"
                break;
            case (2):
                // Laukiam Loader proceso pabaigos  
                break;
            case (3):
                // Atlaisvinam "Išorinė atmintis" resursą
                break;
            case (5):
                // Laukiamas resursas "Vartotojo atmintis" 
                break;
            case (6):
                // Sukuriam procesą "VirtualMachine"
                break;
            case (7):
			// Blokuotas, laukiam "Iš interrupt" resurso
                // Apdorojam
                break;
            case (8):
			// Stabdom VirtualMachine
                // Apdorojam pertraukimą
                // Jeigu pabaigos pertraukimas
                // Naikinam VirtualMachine
                // Jeigu spausdinimo pertraukimas
                // Į išvedimo srautą siunčiami blokų adresai kuriuos naikinti
                // Tęsiam VirtualMachine procesą
                // this.step = 7
                break;
        }
    }

}
