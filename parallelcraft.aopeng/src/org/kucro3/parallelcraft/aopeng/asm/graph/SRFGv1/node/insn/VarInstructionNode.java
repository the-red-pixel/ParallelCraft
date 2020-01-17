package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.VarInsnNode;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 本地变量操作指令节点。<br>
 *
 * 本地变量操作指令包括：ILOAD、LLOAD、FLOAD、DLOAD、ALOAD、ISTORE、LSTORE、FSTORE、DSTORE、ASTORE。
 *
 * @see InstructionNode
 */
public class VarInstructionNode extends InstructionNode {
    /**
     * 构造函数。
     *
     * @param opcode 指令码
     * @param target 本地变量索引
     *
     * @throws IllegalArgumentException 若 target 为负数则抛出此错误
     */
    public VarInstructionNode(int opcode,
                              @Nonnegative int target)
    {
        super(opcode, VAR_INSN);

        this.target = Predication.requireNonNegative(target);
    }

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new VarInsnNode(getOpcode(), getTarget()));
    }

    /**
     * 返回目标本地变量索引。
     *
     * @return 目标本地变量索引
     */
    public int getTarget()
    {
        return target;
    }

    /**
     * 设定目标本地变量索引。
     *
     * @param target 目标本地变量索引
     *
     * @throws IllegalArgumentException 若 target 为负数则抛出此错误
     */
    public void setTarget(int target)
    {
        this.target = Predication.requireNonNegative(target);
    }

    private int target;
}
