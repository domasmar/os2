package os2.main.software.executor;

import os2.main.Core;
import os2.main.hardware.memory.RMMemory;
import os2.main.resources.descriptors.InterruptDescriptor;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.InterruptDescriptor.Type;
import os2.main.resources.descriptors.LineToPrintDescriptor;
import os2.main.resources.descriptors.PrintDescriptor;

/**
 *
 * @author Arturas
 */
public class InterruptHandler {

    private InterruptDescriptor intDes;
    private Process parentOfVM;

    public InterruptHandler(InterruptDescriptor intDes, Process parentOfVM) {
        this.intDes = intDes;
        this.parentOfVM = parentOfVM;
    }

    public boolean fix() {
        if (intDes.getType() == Type.END) {
            return false;
            
        } else if (intDes.getType() == Type.PI) {
            return false;

        } else if (intDes.getType() == Type.SI) {
            Resource liRes = Core.resourceList.searchChildResource(parentOfVM, ResourceType.LI_TO_PR);
            LineToPrintDescriptor liDes = (LineToPrintDescriptor) liRes.getDescriptor();
            liDes.getLine();
            Resource r = new Resource(ResourceType.LI_IN_MEM);
            r.setDescriptor(RMMemory.loadStringToMemory(Integer.toString(liDes.getLine())));
            Core.resourceList.addResource(r);
            return true;

        } else if (intDes.getType() == Type.STI) {
            return false;

        } else if (intDes.getType() == Type.TI) {
            return false;

        }
        return false;
    }
}
