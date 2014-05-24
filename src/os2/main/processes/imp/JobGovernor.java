package os2.main.processes.imp;

import os2.main.Core;
import os2.main.hardware.memory.RMMemory;
import os2.main.hardware.memory.VMMemory;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.FilenameDescriptor;
import os2.main.resources.descriptors.LoaderPacketDescriptor;
import os2.main.resources.descriptors.ProgramInHDDDescriptor;
import os2.main.resources.descriptors.VirtualMemoryDescriptor;

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

    Process vm = null;
    VMMemory vmm = null;
    Resource progInHddRes = null;

    public JobGovernor(Resource progInHdd) {
        this.progInHddRes = progInHdd;
    }

    private JobGovernor() {

    }

    @Override
    public void nextStep() {

        switch (this.step) {
            case (0):
                try {
                    vmm = RMMemory.createVMMemory();
                } catch (RuntimeException e) {
                    this.changeStep(0);
                    break;
                }

                Resource memRes = new Resource(ResourceType.VIRT_MEM);
                memRes.setParent(this);

                VirtualMemoryDescriptor memDes = new VirtualMemoryDescriptor();
                memRes.setDescriptor(memDes);
                memDes.setMemory(vmm);

                Core.resourceList.addResource(memRes);
                this.changeStep(1);
                break;

            // Blokuotas, laukiam kol bus galima išskirti atminties būsimai virtualiai mašinai ir ją patalpiname į resursų sąrašą
            case (1):
                ProgramInHDDDescriptor progInHddDes = (ProgramInHDDDescriptor) progInHddRes.getDescriptor();           
                int[] filename = progInHddDes.getProgramName();

                Resource loaderPacket = new Resource(ResourceType.LOAD_PACK);
                loaderPacket.setParent(this);
                loaderPacket.setDescriptor(new LoaderPacketDescriptor());

                LoaderPacketDescriptor loadDes = (LoaderPacketDescriptor) loaderPacket.getDescriptor();
                loadDes.setMemory(vmm);
                loadDes.setFilename(filename);

                Core.resourceList.addResource(loaderPacket);
                this.changeStep(2);
                break;

            //Paimame virtualią atmintį iš resursų sąrašo, susirandame failo pavadinimą tarp resursų, 
            //kurį reikės paduot loader paketui ir sukūrę loader paketą jį įdedam į sąrašą               
            case (2):
                Resource fromLoader = Core.resourceList.searchChildResource(this, ResourceType.PACK_FROM_LOAD);
                if (fromLoader != null) {
                    this.changeStep(3);
                } else {
                    this.changeStep(2);
                }
                break;

            // Laukiam Loader proceso resurso(pranešimo apie darbo pabaigą)           
            case (3):
                Resource memRes = Core.resourceList.searchChildResource(this, ResourceType.VIRT_MEM);
                if (memRes == null) {
                    this.changeStep(3);
                    break;
                }
                vm = new VirtualMachine(this);
                memRes.setParent(vm);
                Core.processQueue.add(vm);
                this.changeStep(4);
                break;

            // Sukuriam procesą "VirtualMachine"
            case (4):
                Resource interrupt = Core.resourceList.searchChildResource(vm, ResourceType.INT);
                if (interrupt != null) {
                    this.changeStep(5);
                } else {
                    this.changeStep(4);
                }
                break;

            // Blokuotas, laukiam "Iš interrupt" resurso
            case (5):

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
