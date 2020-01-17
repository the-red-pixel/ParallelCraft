package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 常量载入指令节点。<br>
 *
 * 常量载入指令为：LDC。
 *
 * @see InstructionNode
 */
public class LdcInstructionNode extends InstructionNode {
    /**
     * 构造函数。<br>
     * 常量类型应为如下类型之一：Integer、Float、Long、Double、String、Type、Handle、ConstantDynamic。
     *
     * @param opcode 指令码
     * @param constant 常量
     *
     * @throws NullPointerException 如果 constant 为 null 则抛出此错误
     */
    public LdcInstructionNode(int opcode,
                              @Nonnull Object constant)
    {
        super(opcode, LDC_INSN);

        this.constant = Predication.requireNonNull(constant);
    }

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new LdcInsnNode(getConstant()));
    }

    /**
     * 设定常量。<br>
     * 常量类型应为如下类型之一：Integer、Float、Long、Double、String、Type、Handle、ConstantDynamic。
     *
     * @param constant 常量
     *
     * @throws NullPointerException 如果 constant 为 null 则抛出此错误
     */
    public void setConstant(@Nonnull Object constant)
    {
        this.constant = Predication.requireNonNull(constant);
    }

    /**
     * 返回常量。
     *
     * @return 常量
     */
    public @Nonnull Object getConstant()
    {
        return constant;
    }

    private Object constant;
}
