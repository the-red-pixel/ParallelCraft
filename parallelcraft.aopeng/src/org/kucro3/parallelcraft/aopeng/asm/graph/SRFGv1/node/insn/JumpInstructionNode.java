package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

public class JumpInstructionNode extends InstructionNode {
    public JumpInstructionNode(int opcode,
                               @Nonnull SRFBlockNode target)
    {
        super(opcode, JUMP_INSN);

        this.target = Objects.requireNonNull(target);
    }

    @Override
    public void accept(@Nonnull InsnList insnList, @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap)
    {
        insnList.add(new JumpInsnNode(getOpcode(), require(getTarget(), blockLabelMap)));
    }

    public void setTarget(@Nonnull SRFBlockNode target)
    {
        this.target = Objects.requireNonNull(target);
    }

    public @Nonnull SRFBlockNode getTarget()
    {
        return target;
    }

    private SRFBlockNode target;
}
