package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

public class IincInstructionNode extends InstructionNode {
    public IincInstructionNode(int opcode,
                               int local,
                               int increment)
    {
        super(opcode, IINC_INSN);

        this.increment = increment;
        this.local = local;
    }

    public void setIncrement(int increment)
    {
        this.increment = increment;
    }

    public void setTargetLocal(int local)
    {
        this.local = local;
    }

    public int getIncrement()
    {
        return increment;
    }

    public int getTargetLocal()
    {
        return local;
    }

    private int increment;

    private int local;
}
