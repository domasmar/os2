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
    private Process parentOfVM;

    public VirtualMachine(Process parent) {
        this.parentOfVM = parent;
    }

    @Override
    public void nextStep() {

        switch (this.step) {
            case (0):
                Resource vm = Core.resourceList.searchChildResource(this, ResourceType.VIRT_MEM);
                if (vm != null) {
                    CPU.setMODE((byte) 0);
                    VirtualMemoryDescriptor des = (VirtualMemoryDescriptor) vm.getDescriptor();
                    vmm = des.getMemory();
                    exec = new ProgramExecutor(vmm);
                    this.changeStep(1);
                } else {
                    this.changeStep(0);
                }
                break;

            // Perjungiam procesorių į vartotojo režimą
            case (1):
                vmm.loadCPUState();
                exec.executeNext();
                InterruptDescriptor intDes = InterruptChecker.getInt();
                if (intDes != null) {
                    Resource inter = new Resource(ResourceType.INT);
                    inter.setParent(parentOfVM);
                    inter.setDescriptor(intDes);
                    Core.resourceList.addResource(inter);
                    this.changeStep(2);
                    vmm.saveCPUState();
                    break;
                } else {
                    this.changeStep(1);
                    vmm.saveCPUState();
                    break;
                }

            // Veikia tol kol įvyksta pertraukimas,
            // Išsaugom procesoriaus būseną
            // Sukuriamas resursas "Pertraukimas"
            case (2):
                Resource intFixed = Core.resourceList.searchChildResource(this, ResourceType.INT);
                if (intFixed != null) {
                    intDes = (InterruptDescriptor) intFixed.getDescriptor();
                    if (intDes.getFixed() == true) {
                        this.changeStep(1);
                    } else {
                        this.changeStep(2);
                    }
                } else {
                    this.changeStep(2);
                }
                break;
        }
    }
}
