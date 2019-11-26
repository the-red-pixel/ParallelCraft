package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;

import javax.annotation.Nonnull;
import java.util.Map;

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

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap)
    {
        insnList.add(new IincInsnNode(getTargetLocal(), getIncrement()));
    }

    private int increment;

    private int local;
}
