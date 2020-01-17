package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.Handle;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LabelNode;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 动态调用指令节点。<br>
 *
 * 动态调用指令为：INVOKEDYNAMIC。
 */
public class InvokeDynamicInstructionNode extends InstructionNode {
    /**
     * 构造函数。<br>
     * 启动方法参数可且仅可为 Integer、Long、Float、Double、String、Type、Handle、ConstantDynamic
     * 中任意一种类型。
     *
     * @param opcode 指令码
     * @param name 动态调用方法名称
     * @param descriptor 动态调用方法描述符
     * @param bootstrapMethod 启动方法
     * @param bootstrapArguments 启动方法参数
     *
     * @throws NullPointerException 若 name、descriptor、bootstrapMethod、bootstrapArguments 中存在 null
     *      则抛出此错误
     */
    public InvokeDynamicInstructionNode(int opcode,
                                        @Nonnull String name,
                                        @Nonnull String descriptor,
                                        @Nonnull Handle bootstrapMethod,
                                        @Nonnull Object... bootstrapArguments)
    {
        super(opcode, INVOKE_DYNAMIC_INSN);

        this.name = Predication.requireNonNull(name, "name");
        this.descriptor = Predication.requireNonNull(descriptor, "descriptor");
        this.bootstrapMethod = Predication.requireNonNull(bootstrapMethod, "bootstrapMethod");
        this.bootstrapArguments = Predication.requireNonNull(bootstrapArguments, "bootstrapArguments");
    }

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new InvokeDynamicInsnNode(getName(), getDescriptor(), getBootstrapMethod(), getBootstrapArguments()));
    }

    /**
     * 设定启动方法。
     *
     * @param bootstrapMethod 启动方法
     *
     * @throws NullPointerException 若 bootstrapMethod 为 null 则抛出此错误
     */
    public void setBootstrapMethod(@Nonnull Handle bootstrapMethod)
    {
        this.bootstrapMethod = Predication.requireNonNull(bootstrapMethod);
    }

    /**
     * 设定启动方法参数。<br>
     * 启动方法参数可且仅可为 Integer、Long、Float、Double、String、Type、Handle、ConstantDynamic
     * 中任意一种类型。
     *
     * @param bootstrapArguments 启动方法参数
     *
     * @throws NullPointerException 若 bootstrapArguments 为 null 则抛出此错误
     */
    public void setBootstrapArguments(@Nonnull Object... bootstrapArguments)
    {
        this.bootstrapArguments = Predication.requireNonNull(bootstrapArguments);
    }

    /**
     * 设定动态调用方法描述符。
     *
     * @param descriptor 动态调用方法描述符
     *
     * @throws NullPointerException 若 descriptor 为 null 则抛出此错误
     */
    public void setDescriptor(@Nonnull String descriptor)
    {
        this.descriptor = Predication.requireNonNull(descriptor);
    }

    /**
     * 设定动态调用方法名称。
     *
     * @param name 动态调用方法名称
     *
     * @throws NullPointerException 若 name 为 null 则抛出此错误
     */
    public void setName(@Nonnull String name)
    {
        this.name = Predication.requireNonNull(name);
    }

    /**
     * 返回启动方法。
     *
     * @return 启动方法
     */
    public @Nonnull Handle getBootstrapMethod()
    {
        return bootstrapMethod;
    }

    /**
     * 返回启动参数。
     *
     * @return 启动参数
     */
    public @Nonnull Object[] getBootstrapArguments()
    {
        return bootstrapArguments;
    }

    /**
     * 返回动态调用方法描述符。
     *
     * @return 动态调用方法描述符
     */
    public @Nonnull String getDescriptor()
    {
        return descriptor;
    }

    /**
     * 返回动态调用方法名称。
     *
     * @return 动态调用方法名称
     */
    public @Nonnull String getName()
    {
        return name;
    }

    private Handle bootstrapMethod;

    private Object[] bootstrapArguments;

    private String descriptor;

    private String name;
}
