package os2.main.resources.descriptors;

import os2.main.hardware.memory.VMMemory;
import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

/**
 *
 * @author Arturas
 */
public class VirtualMemoryDescriptor implements ResourceDescriptorInterface {
    private VMMemory memory;
    
    public VMMemory getMemory(){
        return this.memory;
    }
    
    public void setMemory(VMMemory memory){
        this.memory = memory;
    }
    
}
