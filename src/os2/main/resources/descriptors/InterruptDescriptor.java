package os2.main.resources.descriptors;

import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

/**
 *
 * @author Arturas
 */
public class InterruptDescriptor implements ResourceDescriptorInterface {

    public enum Type {
        END, PI, SI, TI, STI
    }

    private Type type;
    private byte value;
    private boolean fixed;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    public byte getValue() {
        return this.value;
    }
    
     public void setValue(byte value ) {
        this.value = value;
    }
     
     public void setFixed(boolean fixed){
         this.fixed = fixed;
     }
     
     public boolean getFixed(){
         return this.fixed;
     }

}
