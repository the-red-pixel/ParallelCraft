package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 跳转指令节点。<br>
 *
 * 跳转指令包括：IFEQ、IFNE、IFLT、IFGE、IFGT、IFLE、IF_ICMPEQ、IF_ICMPNE、IF_ICMPLT、IF_ICMPGE、
 * IF_ICMPGT、IF_ICMPLE、IF_ACMPEQ、IF_ACMPNE、GOTO、IFNULL、IFNONNULL。
 *
 * @see InstructionNode
 */
public class JumpInstructionNode extends InstructionNode {
    /**
     * 构造函数
     *
     * @param opcode 指令码
     * @param target 跳转目标
     *
     * @throws NullPointerException 若 target 为 null 则抛出此错误
     */
    public JumpInstructionNode(int opcode,
                               @Nonnull SRFBlockNode target)
    {
        super(opcode, JUMP_INSN);

        this.target = Predication.requireNonNull(target);
    }

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new JumpInsnNode(getOpcode(), require(getTarget(), blockLabelMap, createLabelIfAbsent)));
    }

    /**
     * 设定跳转目标。
     *
     * @param target 跳转目标
     *
     * @throws NullPointerException 若 target 为 null 则抛出此错误
     */
    public void setTarget(@Nonnull SRFBlockNode target)
    {
        this.target = Predication.requireNonNull(target);
    }

    /**
     * 返回跳转目标。
     *
     * @return 跳转目标
     */
    public @Nonnull SRFBlockNode getTarget()
    {
        return target;
    }

    private SRFBlockNode target;
}
