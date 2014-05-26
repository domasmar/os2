package os2.main.resources.descriptors;

import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

/**
 *
 * @author Arturas
 */
public class LineToPrintDescriptor implements ResourceDescriptorInterface{

    private int line;

    public int getLine() {
        return this.line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
