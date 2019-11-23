package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import javax.annotation.Nonnull;
import java.util.Objects;

public class LdcInstructionNode extends InstructionNode {
    public LdcInstructionNode(int opcode,
                              @Nonnull Object constant)
    {
        super(opcode, LDC_INSN);

        this.constant = Objects.requireNonNull(constant);
    }

    public void setConstant(@Nonnull Object constant)
    {
        this.constant = Objects.requireNonNull(constant);
    }

    public @Nonnull Object getConstant()
    {
        return constant;
    }

    private Object constant;
}
