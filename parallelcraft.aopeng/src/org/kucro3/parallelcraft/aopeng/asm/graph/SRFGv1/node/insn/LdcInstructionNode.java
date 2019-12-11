package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

public class LdcInstructionNode extends InstructionNode {
    public LdcInstructionNode(int opcode,
                              @Nonnull Object constant)
    {
        super(opcode, LDC_INSN);

        this.constant = Objects.requireNonNull(constant);
    }

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new LdcInsnNode(getConstant()));
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
