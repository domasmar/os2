package os2.main.software.executor;

import os2.main.resources.descriptors.InterruptDescriptor;
import os2.main.processes.Process;

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

        return true;
    }
}
