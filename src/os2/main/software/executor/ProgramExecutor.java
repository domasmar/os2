package os2.main.software.executor;

import os2.main.Core;
import os2.main.hardware.*;
import os2.main.hardware.memory.VMMemory;
import static os2.main.software.executor.Command.*;
import os2.main.processes.Process;
import os2.main.hardware.CPU.*;
import os2.main.resources.Resource;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.LineToPrintDescriptor;
import os2.main.resources.descriptors.interfaces.ResourceDescriptorInterface;

/**
 *
 * @author Arturas
 */
public class ProgramExecutor {

    private VMMemory memory;
    private CmdWithVar lastCmd;
    private Process parentOfVM;

    public ProgramExecutor(VMMemory virtualMemory, Process parentOfVM) {
        this.memory = virtualMemory;
        this.lastCmd = new CmdWithVar();
        this.parentOfVM = parentOfVM;
    }

    public boolean executeNext() throws RuntimeException {
        if (CPU.getTIMER() == 0) {
            CPU.setTI((byte) 1);
            return true;
        } else {
            CPU.setTIMER(CPU.getTIMER() - 1);
        }
        int word = memory.get(CPU.getCS() + CPU.getIP());
        String bits = intToBits(word);
        String cmdBits = bits.substring(0, 8);
        String valueBits = bits.substring(8, 16);
        int cmdInt = (byte) Integer.parseInt(cmdBits, 2);
        int valueInt = Integer.parseInt(valueBits, 2);

        if (cmdInt == CommandBytecode.ADD) {
            return cmdAdd();
        }

        if (cmdInt == CommandBytecode.SUB) {
            return cmdSub();
        }

        if (cmdInt == CommandBytecode.CMP) {
            return cmdCmp();
        }

        if (cmdInt == CommandBytecode.STOP) {
            cmdStop();
            return true;
        }

        if (cmdInt == CommandBytecode.MOV_AX) {
            valueInt = memory.get(CPU.getCS() + CPU.getIP() + 1);
            cmdMovAx(valueInt);
            return false;
        }

        if (cmdInt == CommandBytecode.MOV_BX) {
            valueInt = memory.get(CPU.getCS() + CPU.getIP() + 1);
            cmdMovBx(valueInt);
            return false;
        }

        if (cmdInt == CommandBytecode.LOA_AX) {
            return cmdLoaAx(valueInt);
        }

        if (cmdInt == CommandBytecode.LOA_BX) {
            return cmdLoaBx(valueInt);
        }

        if (cmdInt == CommandBytecode.STO_AX) {
            return cmdStoAx(valueInt);
        }

        if (cmdInt == CommandBytecode.STO_BX) {
            return cmdStoBx(valueInt);
        }

        if (cmdInt == CommandBytecode.PUSH) {
            return cmdPush(valueInt);
        }

        if (cmdInt == CommandBytecode.POP) {
            return cmdPop(valueInt);
        }

        if (cmdInt == CommandBytecode.JA) {
            return cmdJa(valueInt);
        }

        if (cmdInt == CommandBytecode.JB) {
            return cmdJb(valueInt);
        }

        if (cmdInt == CommandBytecode.JE) {
            return cmdJe(valueInt);
        }

        if (cmdInt == CommandBytecode.JMP) {
            return cmdJmp(valueInt);
        }

        if (cmdInt == CommandBytecode.JNE) {
            return cmdJne(valueInt);
        }

        if (cmdInt == CommandBytecode.OUTR_AX) {
            cmdOutrAx();
            return true;
        }

        if (cmdInt == CommandBytecode.OUTR_BX) {
            cmdOutrBx();
            return true;
        }

        if (cmdInt == CommandBytecode.OUTM) {
            cmdOutM(valueInt);
            return true;
        }
        CPU.setPI((byte) 2); //jei blogas operacijos kodas
        return true;
    }

//******************************************************************************
    private void cmdMovAx(int variable) {
        CPU.setAX(variable);

        short nextCmdAddr = (short) (CPU.getIP() + 2);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = MOV_AX;
        lastCmd.variable = variable;
    }

    private void cmdMovBx(int variable) {
        CPU.setBX(variable);

        short nextCmdAddr = (short) (CPU.getIP() + 2);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = MOV_BX;
        lastCmd.variable = variable;
    }

    private boolean cmdLoaAx(int variable) {
        if (variable >= CPU.getSS()) {
            CPU.setPI((byte) 1);
            return true;
        }
        CPU.setAX(memory.get(variable));
        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = LOA_AX;
        lastCmd.variable = variable;
        return false;
    }

    private boolean cmdLoaBx(int variable) {
        if (variable >= CPU.getSS()) {
            CPU.setPI((byte) 1);
            return true;
        }
        CPU.setBX(memory.get(variable));
        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = LOA_BX;
        lastCmd.variable = variable;
        return false;
    }

    private boolean cmdStoAx(int variable) {
        if (variable >= CPU.getSS()) {
            CPU.setPI((byte) 1);
            return true;
        }
        memory.set(variable, CPU.getAX());

        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = STO_AX;
        lastCmd.variable = variable;
        return false;
    }

    private boolean cmdStoBx(int variable) {
        if (variable >= CPU.getSS()) {
            CPU.setPI((byte) 1);
            return true;
        }
        memory.set(variable, CPU.getBX());
        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = STO_BX;
        lastCmd.variable = variable;
        return false;
    }

    private boolean cmdPush(int variable) {
        if (variable >= CPU.getSS()) {
            CPU.setPI((byte) 1);
            return true;
        }

        try {
            int value = memory.get(CPU.getDS() + variable);
            memory.push(value);
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }

        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = PUSH;
        lastCmd.variable = variable;
        return false;
    }

    private boolean cmdPop(int variable) {
        if (variable >= CPU.getSS()) {
            CPU.setPI((byte) 1);
            return true;
        }

        try {
            int value = memory.pop();
            memory.set(CPU.getDS() + variable, value);
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }

        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = POP;
        lastCmd.variable = variable;
        return false;
    }

    private boolean cmdJa(int variable) {
        if (((CPU.getCS() + variable) >= 255) || (variable < 0)) {
            CPU.setPI((byte) 1);
            return true;
        }

        if (CPU.getC() == 1) {
            CPU.setIP((short) variable);
        } else {
            short nextCmdAddr = (short) (CPU.getIP() + 1);
            CPU.setIP(nextCmdAddr);
        }

        lastCmd.command = JA;
        lastCmd.variable = variable;
        return false;
    }

    private boolean cmdJmp(int variable) {
        if (((CPU.getCS() + variable) >= 255) || (variable < 0)) {
            CPU.setPI((byte) 1);
            return true;
        }
        CPU.setIP((short) variable);

        lastCmd.command = JMP;
        lastCmd.variable = variable;
        return false;
    }

    private boolean cmdJb(int variable) {
        if (((CPU.getCS() + variable) >= 255) || (variable < 0)) {
            CPU.setPI((byte) 1);
            return true;
        }

        if (CPU.getC() == 2) {
            CPU.setIP((short) variable);
        } else {
            short nextCmdAddr = (short) (CPU.getIP() + 1);
            CPU.setIP(nextCmdAddr);
        }
        lastCmd.command = JB;
        lastCmd.variable = variable;
        return false;
    }

    private boolean cmdJe(int variable) {
        if (((CPU.getCS() + variable) >= 255) || (variable < 0)) {
            CPU.setPI((byte) 1);
            return true;
        }

        if (CPU.getC() == 0) {
            CPU.setIP((short) variable);
        } else {
            short nextCmdAddr = (short) (CPU.getIP() + 1);
            CPU.setIP(nextCmdAddr);
        }
        lastCmd.variable = variable;
        return false;
    }

    private boolean cmdJne(int variable) {
        if (((CPU.getCS() + variable) >= 255) || (variable < 0)) {
            CPU.setPI((byte) 1);
            return true;
        }

        if (CPU.getC() == 1 || CPU.getC() == 2) {
            CPU.setIP((short) variable);
        } else {
            short nextCmdAddr = (short) (CPU.getIP() + 1);
            CPU.setIP(nextCmdAddr);
        }
        lastCmd.command = JNE;
        lastCmd.variable = variable;
        return false;
    }

    private void cmdOutrAx() {
        CPU.setSI((byte) 2);
        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);  
        Resource line = new Resource(ResourceType.LI_TO_PR);
        LineToPrintDescriptor liDes = new LineToPrintDescriptor();
        liDes.setLine(CPU.getAX());
        line.setParent(parentOfVM);
        line.setDescriptor(liDes);
        Core.resourceList.addResource(line);
        lastCmd.command = OUTR_AX;
    }

    private void cmdOutrBx() {
        CPU.setSI((byte) 2);
        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        Resource line = new Resource(ResourceType.LI_TO_PR);
        LineToPrintDescriptor liDes = new LineToPrintDescriptor();
        liDes.setLine(CPU.getBX());
        line.setParent(parentOfVM);
        line.setDescriptor(liDes);
        Core.resourceList.addResource(line);
        lastCmd.command = OUTR_BX;
    }

    private void cmdOutM(int variable) {
        CPU.setSI((byte) 2);
        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        Resource line = new Resource(ResourceType.LI_TO_PR);
        LineToPrintDescriptor liDes = new LineToPrintDescriptor();
        liDes.setLine(memory.get(CPU.getDS() + variable));
        line.setParent(parentOfVM);
        line.setDescriptor(liDes);
        Core.resourceList.addResource(line);
        lastCmd.command = OUTM;
        lastCmd.variable = variable;
    }

    private boolean cmdAdd() {
        int firstEl;
        int secondEl;
        
        try {
            firstEl = memory.pop();
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }

        try {
            secondEl = memory.pop();
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }

        try {
            memory.push(secondEl);
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }

        try {
            memory.push(firstEl + secondEl);
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }

        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = ADD;
        return false;
    }

    private boolean cmdSub() {
        int firstEl;
        int secondEl;

        try {
            firstEl = memory.pop();
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }

        try {
            secondEl = memory.pop();
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }

        try {
            memory.push(secondEl);
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }

        try {
            memory.push(firstEl - secondEl);
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }

        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = ADD;
        return false;
    }

    private boolean cmdCmp() {
        int firstEl;
        int secondEl;

        try {
            firstEl = memory.pop();
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }

        try {
            secondEl = memory.pop();
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }
        
         try {
            memory.push(secondEl);
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }

        try {
            memory.push(firstEl);
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
            return true;
        }
        
        
        if (firstEl == secondEl) {
            CPU.setC((byte) 0);
        }

        if (firstEl > secondEl) {
            CPU.setC((byte) 1);
        }

        if (firstEl < secondEl) {
            CPU.setC((byte) 2);
        }

        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = CMP;
        return false;
    }

    private void cmdStop() {
        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        CPU.setEND((byte) 1);
        lastCmd.command = STOP;
    }

    private String intToBits(int a) {
        String bits = Integer.toBinaryString(a);
        if (bits.length() == 15) {
            bits = "0" + bits;
        }

        int length = bits.length();
        int diffLength = 32 - length;
        for (int i = 0; i < diffLength; i++) {
            bits = bits + "0";
        }
        return bits;
    }

    public String getLastCommand() {
        return this.lastCmd.command + " " + this.lastCmd.variable;
    }
}
