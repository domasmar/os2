package os2.main.processes.imp;

import os2.main.Core;
import os2.main.hardware.memory.RMMemory;
import os2.main.hardware.memory.VMMemory;
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
                VMMemory memory = null;

                try {
                    memory = RMMemory.createVMMemory();
                } catch (RuntimeException e) {
                    this.changeStep(0);
                    break;
                }

                Resource memoryR = new Resource("VMMemory");
                memoryR.setParent(this);
                memoryR.setInformation(memory);
                this.changeStep(1);
                break;

            // Blokuotas, laukiam kol bus išskirta atmintis būsimai virtualiai mašinai
            case (1):
                Resource loaderPacket = new Resource("LoaderPacket");
                loaderPacket.setParent(this);
                //loaderPacket.setInformation(reikia patalpint Loaderiui objektą vm'o ir pavadinimą failo);
                Core.resourceList.addResource(loaderPacket);
                this.changeStep(2);
                break;

            // Sukuriamas resursas "Pakrovimo paketas"
            case (2):
                Resource fromLoader = Core.resourceList.searchResource("FromLoader"); // kurį FromLoader grąžina? Reikėtų visų, tada galima atsirinkt pagal inforamtion
                if (fromLoader != null) {
                    fromLoader.setParent(this);
                    this.changeStep(3);
                } else {
                    this.changeStep(2);
                }
                break;

            // Laukiam Loader proceso pagaminto resurso   
            case (3):
                Core.resourceList.delete("ExtMem");
                this.changeStep(4);  
                break;
                
            // Atlaisvinam "Išorinė atmintis" resursą
            case (4):
                Process vm = new VirtualMachine();
                //ProcessQueue.add(vm); turi but leidžiama įterpti i sąrašą
                //ka toliau?
                break;
                
            // Sukuriam procesą "VirtualMachine"
            case (5):
                Resource interrupt = Core.resourceList.searchResource("Int"); //atsirinkt kurį int.
                if (interrupt != null){
                    this.changeStep(6);
                }else{
                    this.changeStep(5);
                }              
                break;
                
            // Blokuotas, laukiam "Iš interrupt" resurso
            case (6):

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
