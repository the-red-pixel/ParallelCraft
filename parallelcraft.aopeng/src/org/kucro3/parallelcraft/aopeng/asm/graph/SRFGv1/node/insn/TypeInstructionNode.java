package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import javax.annotation.Nonnull;
import java.util.Objects;

public class TypeInstructionNode extends InstructionNode {
    public TypeInstructionNode(int opcode,
                               @Nonnull String type)
    {
        super(opcode, TYPE_INSN);

        this.type = Objects.requireNonNull(type);
    }

    public void setTypeOperand(@Nonnull String type)
    {
        this.type = Objects.requireNonNull(type);
    }

    public @Nonnull String getTypeOperand()
    {
        return type;
    }

    private String type;
}
