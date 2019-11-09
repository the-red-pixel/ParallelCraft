package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFGv1NodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.NormalGraphNodeManipulator;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;

import javax.annotation.Nonnull;
import java.util.Objects;

public class InstructionNode extends SRFNode {
    public InstructionNode(@Nonnull AbstractInsnNode node)
    {
        this(node, new NormalGraphNodeManipulator<>());
    }

    protected InstructionNode(@Nonnull AbstractInsnNode node,
                              @Nonnull GraphNodeManipulator<SRFNode> manipulator)
    {
        super(SRFGv1NodeTypes.INSTRUCTION, manipulator);

        this.node = Objects.requireNonNull(node);
    }

    public @Nonnull AbstractInsnNode getNode()
    {
        return node;
    }

    // isolated, not using insn list
    private final AbstractInsnNode node;
}
