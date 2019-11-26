package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

public class FieldInstructionNode extends InstructionNode {
    public FieldInstructionNode(int opcode,
                                @Nonnull String owner,
                                @Nonnull String name,
                                @Nonnull String descriptor)
    {
        super(opcode, FIELD_INSN);

        this.descriptor = Objects.requireNonNull(descriptor, "descriptor");
        this.name = Objects.requireNonNull(name, "name");
        this.owner = Objects.requireNonNull(owner, "owner");
    }

    @Override
    public void accept(@Nonnull InsnList insnList, @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap)
    {
        insnList.add(new FieldInsnNode(getOpcode(), getOwner(), getName(), getDescriptor()));
    }

    public void setDescriptor(@Nonnull String descriptor)
    {
        this.descriptor = Objects.requireNonNull(descriptor);
    }

    public void setName(@Nonnull String name)
    {
        this.name = Objects.requireNonNull(name);
    }

    public void setOwner(@Nonnull String owner)
    {
        this.owner = Objects.requireNonNull(owner);
    }

    public @Nonnull String getDescriptor()
    {
        return descriptor;
    }

    public @Nonnull String getName()
    {
        return name;
    }

    public @Nonnull String getOwner()
    {
        return owner;
    }

    private String descriptor;

    private String name;

    private String owner;
}
