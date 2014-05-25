package os2.main.processes.imp;

import os2.main.Core;
import os2.main.processes.Process;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.InterruptDescriptor;
import os2.main.software.executor.InterruptChecker;

/**
 * Šio proceso paskirtis – reaguoti į pertraukimus, kilusius virtualios mašinos
 * darbo metu.
 *
 * @author domas
 *
 */
public class Interrupt extends Process {

    private Resource interrupt;
    private Process jobGovernor;
    private InterruptDescriptor intDes;

    @Override
    public void nextStep() {
        switch (this.step) {
            case (0):
                interrupt = Core.resourceList.searchResource(ResourceType.INT);
                if (interrupt != null) {
                    this.changeStep(1);
                } else {
                    this.changeStep(0);
                }
                break;
            case (1):
                intDes = InterruptChecker.getInt();
                this.changeStep(2);
                break;
            case (2):
                jobGovernor = interrupt.getParent();
                this.changeStep(3);
                break;
            case (3):
                Resource fromInt = new Resource(ResourceType.FROM_INT);
                fromInt.setDescriptor(intDes);
                fromInt.setParent(jobGovernor);
                Core.resourceList.addResource(fromInt);
                this.changeStep(0);
                break;
        }
    }
}
