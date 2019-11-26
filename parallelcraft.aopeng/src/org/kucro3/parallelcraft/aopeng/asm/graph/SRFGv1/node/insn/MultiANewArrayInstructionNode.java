package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

public class MultiANewArrayInstructionNode extends InstructionNode {
    public MultiANewArrayInstructionNode(int opcode,
                                         @Nonnull String descriptor,
                                         @Nonnegative int dimension)
    {
        super(opcode, MULTIANEWARRAY_INSN);

        this.descriptor = Objects.requireNonNull(descriptor, "descriptor");
        this.dimension = dimension;
    }

    @Override
    public void accept(@Nonnull InsnList insnList, @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap)
    {
        insnList.add(new MultiANewArrayInsnNode(getDescriptor(), getDimension()));
    }

    public void setDescriptor(@Nonnull String descriptor)
    {
        this.descriptor = Objects.requireNonNull(descriptor);
    }

    public void setDimension(@Nonnegative int dimension)
    {
        this.dimension = dimension;
    }

    public @Nonnull String getDescriptor()
    {
        return descriptor;
    }

    public @Nonnegative int getDimension()
    {
        return dimension;
    }

    private String descriptor;

    private int dimension;
}
