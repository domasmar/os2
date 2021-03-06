package os2.main.software.executor;

import os2.main.hardware.CPU;
import os2.main.resources.descriptors.InterruptDescriptor;

/**
 *
 * @author Arturas
 */
public class InterruptChecker {

    public static InterruptDescriptor getInt() {
        InterruptDescriptor des = null;

        if (CPU.getEND() != 0) {
            des = new InterruptDescriptor();
            des.setType(InterruptDescriptor.Type.END);
            des.setValue(CPU.getEND());
            des.setFixed(false);
        } else if (CPU.getPI() != 0) {
            des = new InterruptDescriptor();
            des.setType(InterruptDescriptor.Type.PI);
            des.setValue(CPU.getPI());
            des.setFixed(false);
        } else if (CPU.getSI() != 0) {
            des = new InterruptDescriptor();
            des.setType(InterruptDescriptor.Type.SI);
            des.setValue(CPU.getSI());
            des.setFixed(false);
        } else if (CPU.getTI() != 0) {
            des = new InterruptDescriptor();
            des.setType(InterruptDescriptor.Type.TI);
            des.setValue(CPU.getTI());
            des.setFixed(false);
        } else if (CPU.getSTI() != 0) {
            des = new InterruptDescriptor();
            des.setType(InterruptDescriptor.Type.STI);
            des.setValue(CPU.getSTI());
            des.setFixed(false);
        }
        return des;
    }
}
