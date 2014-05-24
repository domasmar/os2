package os2.main.processes.imp;

import java.util.logging.Level;
import java.util.logging.Logger;
import os2.main.Core;
import os2.main.hardware.CPU;
import os2.main.hardware.memory.VMMemory;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.InterruptDescriptor;
import os2.main.resources.descriptors.VirtualMemoryDescriptor;
import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;
import os2.main.software.executor.InterruptChecker;
import os2.main.software.executor.InterruptIndicator;
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
    private Process parent;
    
    public VirtualMachine(Process parent){
        this.parent = parent;
    }
    
    private VirtualMachine() {

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
                    Stack stack = new Stack(vmm);
                    exec = new ProgramExecutor(vmm, stack);
                    this.changeStep(1);
                } else {
                    this.changeStep(0);
                }
                // Perjungiam procesorių į vartotojo režimą
                break;
            case (1):
                vmm.loadCPUState();
                exec.executeNext();
                InterruptDescriptor des = InterruptChecker.getInt();
                if (des != null) {
                    Resource inter = new Resource(ResourceType.INT);
                    inter.setParent(parent);
                    inter.setDescriptor(des);
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
                // Atslaivinamas resursas "Pertraukimas"
                break;

            case (3):

        }
    }
}
