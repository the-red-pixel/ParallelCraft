package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.VarInsnNode;

import javax.annotation.Nonnull;
import java.util.Map;

public class VarInstructionNode extends InstructionNode {
    public VarInstructionNode(int opcode, int operand)
    {
        super(opcode, VAR_INSN);

        this.operand = operand;
    }

    @Override
    public void accept(@Nonnull InsnList insnList, @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap)
    {
        insnList.add(new VarInsnNode(getOpcode(), getOperand()));
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
