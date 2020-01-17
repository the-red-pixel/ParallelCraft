package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LabelNode;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 单整数操作数指令节点。<br>
 *
 * 单整数操作指令包括：BIPUSH、SIPUSH、NEWARRAY。
 *
 * @see InstructionNode
 */
public class IntInstructionNode extends InstructionNode {
    /**
     * 构造函数。
     *
     * @param opcode 指令码
     * @param operand 整数操作数
     */
    public IntInstructionNode(int opcode, int operand)
    {
        super(opcode, INT_INSN);

        this.operand = operand;
    }

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new IntInsnNode(getOpcode(), getOperand()));
    }

    /**
     * 返回整数操作数。
     *
     * @return 整数操作数
     */
    public int getOperand()
    {
        return operand;
    }

    /**
     * 设定整数操作数。
     *
     * @param operand 整数操作数
     */
    public void setOperand(int operand)
    {
        this.operand = operand;
    }

    private int operand;
}
