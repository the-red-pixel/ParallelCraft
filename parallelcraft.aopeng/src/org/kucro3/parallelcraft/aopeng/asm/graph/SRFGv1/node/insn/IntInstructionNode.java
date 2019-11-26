package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LabelNode;

import javax.annotation.Nonnull;
import java.util.Map;

public class IntInstructionNode extends InstructionNode {
    public IntInstructionNode(int opcode, int operand)
    {
        super(opcode, INT_INSN);

        this.operand = operand;
    }

    @Override
    public void accept(@Nonnull InsnList insnList, @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap)
    {
        insnList.add(new IntInsnNode(getOpcode(), getOperand()));
    }

    public int getOperand()
    {
        return operand;
    }

    public void setOperand(int operand)
    {
        this.operand = operand;
    }

    private int operand;
}
