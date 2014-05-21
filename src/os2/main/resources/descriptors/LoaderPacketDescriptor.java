package os2.main.resources.descriptors;

import os2.main.hardware.memory.VMMemory;
import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

/**
 *
 * @author Arturas
 */
public class LoaderPacketDescriptor implements ResourceDescriptorInterface {

    private VMMemory memory;
    private int[] filename;
    
    public VMMemory getMemory(){
        return this.memory;
    }
    
    public void setMemory(VMMemory memory){
        this.memory = memory;
    }
    
    public int[] getFilename(){
        return this.filename;
    }
    
    public void setFilename(int[] filename){
        this.filename = filename;
    }
}
