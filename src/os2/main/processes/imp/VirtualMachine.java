package os2.main.processes.imp;

import os2.main.Core;
import os2.main.hardware.CPU;
import os2.main.hardware.memory.VMMemory;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.InterruptDescriptor;
import os2.main.resources.descriptors.VirtualMemoryDescriptor;
import os2.main.software.executor.InterruptChecker;
import os2.main.software.executor.ProgramExecutor;

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

    private VMMemory vmm = null;
    private ProgramExecutor exec = null;
    private Process parentOfVM = null;

    public VirtualMachine(VMMemory vmm, Process parent) {
        this.vmm = vmm;
        this.parentOfVM = parent;
    }

    @Override
    public void nextStep() {

        switch (this.step) {
            case (0):
                if (vmm != null) {
                    CPU.setMODE((byte) 0);
                    exec = new ProgramExecutor(vmm, parentOfVM);
                    this.changeStep(1);
                } else {
                    this.changeStep(0);
                }

                break;
            case (1):
                vmm.loadCPUState();
                if (exec.executeNext()) {
                    Resource interrupt = new Resource(ResourceType.INT);
                    interrupt.setParent(parentOfVM);
                    Core.resourceList.addResource(interrupt);
                    vmm.saveCPUState();
                    this.changeStep(2);
                } else {
                    vmm.saveCPUState();
                    this.changeStep(1);
                }

                break;
            case (2):
                Resource intFixed = Core.resourceList.searchChildResource(this, ResourceType.FROM_INT);
                if (intFixed != null) {
                    InterruptDescriptor intDes = (InterruptDescriptor) intFixed.getDescriptor();
                    if (intDes.getFixed() == true) {
                        this.changeStep(1);
                        Core.resourceList.deleteByInstance(intFixed);
                    } else {
                        this.changeStep(2);
                    }
                } else {
                    this.changeStep(2);
                }
                break;
            /* Tikriname ar interruptas sutvarkytas,
             jei ne laukiame, kol bus sutvarkytas,
             jei sutvakrytas tęsiame darbą 
             */
        }
    }
}
