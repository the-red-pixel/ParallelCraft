package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 多维数组分配指令节点。<br>
 *
 * 多维数组分配指令为：MULTIANEWARRAY。
 */
public class MultiANewArrayInstructionNode extends InstructionNode {
    /**
     * 构造函数。
     *
     * @param opcode 指令码
     * @param descriptor 数组类型描述符
     * @param dimension 分配维数
     *
     * @throws NullPointerException 若 descriptor 为 null 则抛出此错误
     * @throws IllegalArgumentException 若 dimension 小于 1 则抛出此错误
     */
    public MultiANewArrayInstructionNode(int opcode,
                                         @Nonnull String descriptor,
                                         @Nonnegative int dimension)
    {
        super(opcode, MULTIANEWARRAY_INSN);

        this.descriptor = Predication.requireNonNull(descriptor, "descriptor");
        this.dimension = Predication.requirePositive(dimension, "dimension");
    }

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new MultiANewArrayInsnNode(getDescriptor(), getDimension()));
    }

    /**
     * 设定数组类型描述符。
     *
     * @param descriptor 数组类型描述符
     *
     * @throws NullPointerException 若 descriptor 为 null 则抛出此错误
     */
    public void setDescriptor(@Nonnull String descriptor)
    {
        this.descriptor = Predication.requireNonNull(descriptor);
    }

    /**
     * 设定分配维数。
     *
     * @param dimension 分配维数
     *
     * @throws IllegalArgumentException 若 dimension 小于 1 则抛出此错误
     */
    public void setDimension(@Nonnegative int dimension)
    {
        this.dimension = Predication.requirePositive(dimension);
    }

    /**
     * 返回数组类型描述符。
     *
     * @return 数组类型描述符
     */
    public @Nonnull String getDescriptor()
    {
        return descriptor;
    }

    /**
     * 返回分配维数。
     *
     * @return 分配维数
     */
    public @Nonnegative int getDimension()
    {
        return dimension;
    }

    private String descriptor;

    private int dimension;
}
