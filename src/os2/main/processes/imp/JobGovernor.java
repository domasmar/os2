package os2.main.processes.imp;

import os2.main.Core;
import os2.main.hardware.memory.RMMemory;
import os2.main.hardware.memory.VMMemory;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.InterruptDescriptor;
import os2.main.resources.descriptors.LoaderPacketDescriptor;
import os2.main.resources.descriptors.ProgramInHDDDescriptor;
import os2.main.software.executor.InterruptHandler;

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

    private Process vmProc = null;
    private VMMemory vmm = null;
    private Resource progInHddRes = null;
    private Resource memRes = null;
    private Resource intRes = null;
    private Resource fromLoader = null;

    public JobGovernor(Resource progInHdd) {
        this.progInHddRes = progInHdd;
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
                this.changeStep(1);
                
                break;
            case (1):
                ProgramInHDDDescriptor progInHddDes = (ProgramInHDDDescriptor) progInHddRes.getDescriptor();
                int[] filename = progInHddDes.getProgramName();

                Resource loaderPacket = new Resource(ResourceType.LOAD_PACK);
                loaderPacket.setParent(this);
                LoaderPacketDescriptor loadDes = new LoaderPacketDescriptor();
                loaderPacket.setDescriptor(loadDes);
                loadDes.setMemory(vmm);
                loadDes.setFilename(filename);

                Core.resourceList.addResource(loaderPacket);
                this.changeStep(2);
                
                break;
            case (2):
                fromLoader = Core.resourceList.searchChildResource(this, ResourceType.PACK_FROM_LOAD);
                if (fromLoader != null) {
                    this.changeStep(3);
                } else {
                    this.changeStep(2);
                }
                
                break;
            case (3):
                vmProc = new VirtualMachine(vmm, this);
                Core.processQueue.add(vmProc);
                this.changeStep(4);
                
                break;
            case (4):
                intRes = Core.resourceList.searchChildResource(this, ResourceType.FROM_INT);
                if (intRes != null) {
                    this.changeStep(5);
                } else {
                    this.changeStep(4);
                }
                
                break;
            case (5):
                InterruptDescriptor intDes = (InterruptDescriptor) intRes.getDescriptor();
                InterruptHandler handler = new InterruptHandler(intDes, this);
                if (handler.fix()) {
                    intDes.setFixed(true);
                    VirtualMachine vmProcAsVM = (VirtualMachine) vmProc;
                    vmProcAsVM.setStep(1);
                    Core.resourceList.deleteByInstance(intRes);
                    this.changeStep(4);
                    break;
                } else {
                    Core.processQueue.delete(vmProc.getPid());
                    ProgramInHDDDescriptor des = (ProgramInHDDDescriptor) progInHddRes.getDescriptor();
                    des.setFromJobToSwap(false);
                    Core.resourceList.deleteByInstance(memRes);
                    Core.resourceList.deleteByInstance(fromLoader);
                    this.changeStep(6);
                }
                
                Core.resourceList.deleteByInstance(intRes);
                break;
            case (6):
                this.changeStep(6);
                
                break;
        }
    }
}
