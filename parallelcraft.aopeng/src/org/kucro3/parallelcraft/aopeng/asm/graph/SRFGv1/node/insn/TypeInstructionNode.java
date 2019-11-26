package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.TypeInsnNode;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

public class TypeInstructionNode extends InstructionNode {
    public TypeInstructionNode(int opcode,
                               @Nonnull String type)
    {
        super(opcode, TYPE_INSN);

        this.type = Objects.requireNonNull(type);
    }

    @Override
    public void accept(@Nonnull InsnList insnList, @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap)
    {
        insnList.add(new TypeInsnNode(getOpcode(), getTypeOperand()));
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
