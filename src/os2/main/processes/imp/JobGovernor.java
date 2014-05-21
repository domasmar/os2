package os2.main.processes.imp;

import os2.main.Core;
import os2.main.hardware.memory.RMMemory;
import os2.main.hardware.memory.VMMemory;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.FilenameDescriptor;
import os2.main.resources.descriptors.LoaderPacketDescriptor;
import os2.main.resources.descriptors.VirtualMemoryDescriptor;
import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

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
                VMMemory memory;
                try {
                    memory = RMMemory.createVMMemory();
                } catch (RuntimeException e) {
                    this.changeStep(0);
                    break;
                }

                Resource memoryR = new Resource(ResourceType.VIRT_MEM);
                memoryR.setParent(this);
                memoryR.setDescriptor(new VirtualMemoryDescriptor());
                
                VirtualMemoryDescriptor memDes = (VirtualMemoryDescriptor) memoryR.getDescriptor();
                memDes.setMemory(memory);
                
                Core.resourceList.addResource(memoryR);       
                this.changeStep(1);
                break;

            // Blokuotas, laukiam kol bus galima išskirti atminties būsimai virtualiai mašinai ir ją patalpiname į resursų sąrašą
            case (1):
                memory = null;
                memDes = (VirtualMemoryDescriptor) Core.resourceList.searchChildResource(this, ResourceType.PACK_FROM_LOAD).getDescriptor();
                memory = memDes.getMemory();
                if (memory == null){
                    this.changeStep(1);
                    break;
                }
                
                FilenameDescriptor nameDes = (FilenameDescriptor) Core.resourceList.searchResource(ResourceType.FILE_NAME).getDescriptor();
                if (nameDes == null){
                    this.changeStep(1);
                    break;
                }
                int[] filename = nameDes.getFilename();
                
                Resource loaderPacket = new Resource(ResourceType.LOAD_PACK);
                loaderPacket.setParent(this);
                loaderPacket.setDescriptor(new LoaderPacketDescriptor());
                
                LoaderPacketDescriptor loadDes = (LoaderPacketDescriptor) loaderPacket.getDescriptor();
                loadDes.setMemory(memory);
                loadDes.setFilename(filename);
                
                Core.resourceList.addResource(loaderPacket);
                this.changeStep(2);
                break;

                //Paimame virtualią atmintį iš resursų sąrašo, susirandame failo pavadinimą tarp resursų, 
                //kurį reikės paduot loader paketui ir sukūrę loader paketą jį įdedam į sąrašą
                
            case (2):
                Resource fromLoader = Core.resourceList.searchChildResource(this, ResourceType.PACK_FROM_LOAD); 
                if (fromLoader != null) {
                    fromLoader.setParent(this);
                    this.changeStep(3);
                } else {
                    this.changeStep(2);
                }
                break;

            // Laukiam Loader proceso resurso(pranešimo apie darbo pabaigą)   
            case (3):
                Core.resourceList.delete(ResourceType.EXT_MEM);
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
                Resource interrupt = Core.resourceList.searchResource(ResourceType.INT); //atsirinkt kurį int.
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
