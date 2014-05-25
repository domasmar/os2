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

    public void executeNext() throws RuntimeException {
        if (CPU.getTIMER() == 0) {
            CPU.setTI((byte) 1);
            return;
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
            cmdAdd();
            return;
        }

        if (cmdInt == CommandBytecode.SUB) {
            cmdSub();
            return;
        }

        if (cmdInt == CommandBytecode.CMP) {
            cmdCmp();
            return;
        }

        if (cmdInt == CommandBytecode.STOP) {
            cmdStop();
            return;
        }

        if (cmdInt == CommandBytecode.MOV_AX) {
            valueInt = memory.get(CPU.getCS() + CPU.getIP() + 1);
            cmdMovAx(valueInt);
            return;
        }

        if (cmdInt == CommandBytecode.MOV_BX) {
            valueInt = memory.get(CPU.getCS() + CPU.getIP() + 1);
            cmdMovBx(valueInt);
            return;
        }

        if (cmdInt == CommandBytecode.LOA_AX) {
            cmdLoaAx(valueInt);
            return;
        }

        if (cmdInt == CommandBytecode.LOA_BX) {
            cmdLoaBx(valueInt);
            return;
        }

        if (cmdInt == CommandBytecode.STO_AX) {
            cmdStoAx(valueInt);
            return;
        }

        if (cmdInt == CommandBytecode.STO_BX) {
            cmdStoBx(valueInt);
            return;
        }

        if (cmdInt == CommandBytecode.PUSH) {
            cmdPush(valueInt);
            return;
        }

        if (cmdInt == CommandBytecode.POP) {
            cmdPop(valueInt);
            return;
        }

        if (cmdInt == CommandBytecode.JA) {
            cmdJa(valueInt);
            return;
        }

        if (cmdInt == CommandBytecode.JB) {
            cmdJb(valueInt);
            return;
        }

        if (cmdInt == CommandBytecode.JE) {
            cmdJe(valueInt);
            return;
        }

        if (cmdInt == CommandBytecode.JMP) {
            cmdJmp(valueInt);
            return;
        }

        if (cmdInt == CommandBytecode.JNE) {
            cmdJne(valueInt);
            return;
        }

        if (cmdInt == CommandBytecode.OUTR_AX) {
            cmdOutrAx();
            return;
        }

        if (cmdInt == CommandBytecode.OUTR_BX) {
            cmdOutrBx();
            return;
        }

        if (cmdInt == CommandBytecode.OUTM) {
            cmdOutM(valueInt);
            return;
        }
        CPU.setPI((byte) 2); //jei blogas operacijos kodas
        return;
    }

//******************************************************************************
    private void cmdMovAx(int variable) throws RuntimeException {
        CPU.setAX(variable);

        short nextCmdAddr = (short) (CPU.getIP() + 2);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = MOV_AX;
        lastCmd.variable = variable;
    }

    private void cmdMovBx(int variable) throws RuntimeException {
        CPU.setBX(variable);

        short nextCmdAddr = (short) (CPU.getIP() + 2);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = MOV_BX;
        lastCmd.variable = variable;
    }

    private void cmdLoaAx(int variable) throws RuntimeException {
        if (variable >= CPU.getSS()) {
            CPU.setPI((byte) 1);
            throw new RuntimeException("Adresas išlipa iš DS segmento!");
        }
        CPU.setAX(memory.get(variable));
        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = LOA_AX;
        lastCmd.variable = variable;
    }

    private void cmdLoaBx(int variable) throws RuntimeException {
        if (variable >= CPU.getSS()) {
            CPU.setPI((byte) 1);
            throw new RuntimeException("Adresas išlipa iš DS segmento!");
        }
        CPU.setBX(memory.get(variable));
        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = LOA_BX;
        lastCmd.variable = variable;
    }

    private void cmdStoAx(int variable) throws RuntimeException {
        if (variable >= CPU.getSS()) {
            CPU.setPI((byte) 1);
            throw new RuntimeException("Adresas išlipa iš DS segmento!");
        }
        memory.set(variable, CPU.getAX());

        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = STO_AX;
        lastCmd.variable = variable;
    }

    private void cmdStoBx(int variable) throws RuntimeException {
        if (variable >= CPU.getSS()) {
            CPU.setPI((byte) 1);
            throw new RuntimeException("Adresas išlipa iš DS segmento!");
        }
        memory.set(variable, CPU.getBX());
        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = STO_BX;
        lastCmd.variable = variable;
    }

    private void cmdPush(int variable) throws RuntimeException {
        if (variable >= CPU.getSS()) {
            CPU.setPI((byte) 1);
            throw new RuntimeException("Adresas išlipa iš DS segmento!");
        }

        try {
            int value = memory.get(CPU.getDS() + variable);
            memory.push(value);
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
        }

        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = PUSH;
        lastCmd.variable = variable;
    }

    private void cmdPop(int variable) throws RuntimeException {
        if (variable >= CPU.getSS()) {
            CPU.setPI((byte) 1);
            throw new RuntimeException("Adresas išlipa iš DS segmento!");
        }

        try {
            int value = memory.pop();
            memory.set(CPU.getDS() + variable, value);
        } catch (RuntimeException e) {
            CPU.setSTI((byte) 1);
        }

        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = POP;
        lastCmd.variable = variable;
    }

    private void cmdJa(int variable) throws RuntimeException {
        if (((CPU.getCS() + variable) >= 255) || (variable < 0)) {
            CPU.setPI((byte) 1);
            throw new RuntimeException("Jump komanda išlipa iš CS segmento!");
        }

        if (CPU.getC() == 1) {
            CPU.setIP((short) variable);
        } else {
            short nextCmdAddr = (short) (CPU.getIP() + 1);
            CPU.setIP(nextCmdAddr);
        }

        lastCmd.command = JA;
        lastCmd.variable = variable;
    }

    private void cmdJmp(int variable) throws RuntimeException {
        if (((CPU.getCS() + variable) >= 255) || (variable < 0)) {
            CPU.setPI((byte) 1);
            throw new RuntimeException("Jump komanda išlipa iš CS segmento!");
        }
        CPU.setIP((short) variable);

        lastCmd.command = JMP;
        lastCmd.variable = variable;
    }

    private void cmdJb(int variable) throws RuntimeException {
        if (((CPU.getCS() + variable) >= 255) || (variable < 0)) {
            CPU.setPI((byte) 1);
            throw new RuntimeException("Jump komanda išlipa iš CS segmento!");
        }

        if (CPU.getC() == 2) {
            CPU.setIP((short) variable);
        } else {
            short nextCmdAddr = (short) (CPU.getIP() + 1);
            CPU.setIP(nextCmdAddr);
        }
        lastCmd.command = JB;
        lastCmd.variable = variable;
    }

    private void cmdJe(int variable) throws RuntimeException {
        if (((CPU.getCS() + variable) >= 255) || (variable < 0)) {
            CPU.setPI((byte) 1);
            throw new RuntimeException("Jump komanda išlipa iš CS segmento!");
        }

        if (CPU.getC() == 0) {
            CPU.setIP((short) variable);
        } else {
            short nextCmdAddr = (short) (CPU.getIP() + 1);
            CPU.setIP(nextCmdAddr);
        }
        lastCmd.variable = variable;
    }

    private void cmdJne(int variable) throws RuntimeException {
        if (((CPU.getCS() + variable) >= 255) || (variable < 0)) {
            CPU.setPI((byte) 1);
            throw new RuntimeException("Jump komanda išlipa iš CS segmento!");
        }

        if (CPU.getC() == 1 || CPU.getC() == 2) {
            CPU.setIP((short) variable);
        } else {
            short nextCmdAddr = (short) (CPU.getIP() + 1);
            CPU.setIP(nextCmdAddr);
        }
        lastCmd.command = JNE;
        lastCmd.variable = variable;
    }

    private void cmdOutrAx() throws RuntimeException {
        CPU.setSI((byte) 2);
        CPU.setIOI((byte) 2);
        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = OUTR_AX;
        Resource line = new Resource(ResourceType.LI_TO_PR);
        LineToPrintDescriptor liDes = new LineToPrintDescriptor();
        liDes.setLine(CPU.getAX());
        line.setParent(parentOfVM);
        Core.resourceList.addResource(line);
    }

    private void cmdOutrBx() throws RuntimeException {
        CPU.setSI((byte) 2);
        CPU.setIOI((byte) 2);
        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = OUTR_BX;
        Resource line = new Resource(ResourceType.LI_TO_PR);
        LineToPrintDescriptor liDes = new LineToPrintDescriptor();
        liDes.setLine(CPU.getBX());
        line.setParent(parentOfVM);
        Core.resourceList.addResource(line);
    }

    private void cmdOutM(int variable) throws RuntimeException {
        CPU.setSI((byte) 2);
        CPU.setIOI((byte) 2);
        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = OUTM;
        lastCmd.variable = variable;
        Resource line = new Resource(ResourceType.LI_TO_PR);
        LineToPrintDescriptor liDes = new LineToPrintDescriptor();
        liDes.setLine(memory.get(variable));
        line.setParent(parentOfVM);
        Core.resourceList.addResource(line);
    }

    private void cmdAdd() throws RuntimeException {
        int firstEl = memory.get(CPU.getSS() + CPU.getSP());  //gal reikės pasinaudot stack metodais siekiant išvengti klaidų neapdorojimo
        int secondEl = memory.get(CPU.getSS() + CPU.getSP() - 1);
        int sum = firstEl + secondEl;
        memory.set(CPU.getSS() + CPU.getSP(), sum);

        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = ADD;
    }

    private void cmdSub() throws RuntimeException {
        int firstEl = memory.get(CPU.getSS() + CPU.getSP());
        int secondEl = memory.get(CPU.getSS() + CPU.getSP() - 1);
        int diff = firstEl - secondEl;
        memory.set(CPU.getSS() + CPU.getSP(), diff);

        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = SUB;
    }

    private void cmdCmp() throws RuntimeException {
        if ((memory.get(CPU.getSS() + CPU.getSP())) == (memory.get(CPU.getSS() + CPU.getSP() - 1))) {
            CPU.setC((byte) 0);
        }

        if ((memory.get(CPU.getSS() + CPU.getSP())) > (memory.get(CPU.getSS() + CPU.getSP() - 1))) {
            CPU.setC((byte) 1);
        }

        if ((memory.get(CPU.getSS() + CPU.getSP())) < (memory.get(CPU.getSS() + CPU.getSP() - 1))) {
            CPU.setC((byte) 2);
        }

        short nextCmdAddr = (short) (CPU.getIP() + 1);
        CPU.setIP(nextCmdAddr);
        lastCmd.command = CMP;
    }

    private void cmdStop() throws RuntimeException {
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
