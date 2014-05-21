package os2.main.resources.descriptors;

import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

/**
 *
 * @author Arturas
 */
public class FilenameDescriptor implements ResourceDescriptorInterface{
    private int[] filename;
    
    public int[] getFilename(){
        return this.filename;
    }
    
    public void setFilename(int[] filename){
        this.filename = filename;
    }

}
