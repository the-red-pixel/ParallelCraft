package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

public class IntInstructionNode extends InstructionNode {
    public IntInstructionNode(int opcode, int operand)
    {
        super(opcode, INT_INSN);

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
