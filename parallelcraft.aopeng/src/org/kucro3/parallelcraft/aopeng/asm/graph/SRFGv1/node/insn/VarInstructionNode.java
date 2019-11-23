package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

public class VarInstructionNode extends InstructionNode {
    public VarInstructionNode(int opcode, int operand)
    {
        super(opcode, VAR_INSN);

        this.operand = operand;
    }

    public int getOperand()
    {
        return operand;
    }

    public void setOperand(int operand)
    {
        this.operand = operand;
    }

    private int operand;
}
