package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 自增指令节点。<br>
 *
 * 自增指令为 IINC。
 *
 * @see InstructionNode
 */
public class IincInstructionNode extends InstructionNode {
    /**
     * 构造函数。
     *
     * @param opcode 指令码，应为 IINC
     * @param local 自增操作目标本地变量的索引
     * @param increment 自增量
     *
     * @throws IllegalArgumentException 若 local 为负数则抛出此错误
     */
    public IincInstructionNode(int opcode,
                               @Nonnegative int local,
                               int increment)
    {
        super(opcode, IINC_INSN);

        this.increment = increment;
        this.local = Predication.requireNonNegative(local, "local");
    }

    /**
     * 设定自增量。
     *
     * @param increment 自增量
     */
    public void setIncrement(int increment)
    {
        this.increment = increment;
    }

    /**
     * 设定自增操作目标本地变量的索引。
     *
     * @param local 自增操作目标本地变量的索引
     */
    public void setTargetLocal(int local)
    {
        this.local = local;
    }

    /**
     * 返回自增量。
     *
     * @return 自增量
     */
    public int getIncrement()
    {
        return increment;
    }

    /**
     * 返回自增操作目标本地变量的索引。
     *
     * @return 自增操作目标本地变量的索引
     */
    public int getTargetLocal()
    {
        return local;
    }

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new IincInsnNode(getTargetLocal(), getIncrement()));
    }

    private int increment;

    private int local;
}
