package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 方法操作指令节点。<br>
 *
 * 方法操作指令包括：INVOKEVIRTUAL、INVOKESPECIAL、INVOKESTATIC、INVOKEINTERFACE。
 */
public class MethodInstructionNode extends InstructionNode {
    /**
     * 构造函数。
     *
     * @param opcode 指令码
     * @param owner 目标方法所在类的内部名称
     * @param name 目标方法名称
     * @param descriptor 目标方法描述符
     * @param itf 目标方法所在类是否为接口类型
     */
    public MethodInstructionNode(int opcode,
                                 @Nonnull String owner,
                                 @Nonnull String name,
                                 @Nonnull String descriptor,
                                 boolean itf)
    {
        super(opcode, METHOD_INSN);

        this.descriptor = Predication.requireNonNull(descriptor, "descriptor");
        this.name = Predication.requireNonNull(name, "name");
        this.owner = Predication.requireNonNull(owner, "owner");
        this.itf = itf;
    }

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new MethodInsnNode(getOpcode(), getOwner(), getName(), getDescriptor(), isInterface()));
    }

    /**
     * 设定目标方法描述符。
     *
     * @param descriptor 目标方法描述符
     *
     * @throws NullPointerException 若 descriptor 为 null 则抛出此错误
     */
    public void setDescriptor(@Nonnull String descriptor)
    {
        this.descriptor = Predication.requireNonNull(descriptor);
    }

    /**
     * 设定目标方法名称。
     *
     * @param name 目标方法名称
     *
     * @throws NullPointerException 若 name 为 null 则抛出此错误
     */
    public void setName(@Nonnull String name)
    {
        this.name = Predication.requireNonNull(name);
    }

    /**
     * 设定目标方法所在类的内部名称。
     *
     * @param owner 目标方法所在类的内部名称
     *
     * @throws NullPointerException 若 owner 为 null 则抛出此错误
     */
    public void setOwner(@Nonnull String owner)
    {
        this.owner = Predication.requireNonNull(owner);
    }

    /**
     * 设定目标方法所在类是否为接口类型。
     *
     * @param itf 目标方法所在类是否为接口类型
     */
    public void setInterface(boolean itf)
    {
        this.itf = itf;
    }

    /**
     * 返回目标方法的描述符。
     *
     * @return 目标方法描述符
     */
    public @Nonnull String getDescriptor()
    {
        return descriptor;
    }

    /**
     * 返回目标方法的名称。
     *
     * @return 目标方法名称
     */
    public @Nonnull String getName()
    {
        return name;
    }

    /**
     * 返回目标方法所在类的内部名称。
     *
     * @return 目标方法所在类内部名称
     */
    public @Nonnull String getOwner()
    {
        return owner;
    }

    /**
     * 返回目标方法所在类是否为接口类型。
     *
     * @return 目标方法所在类是否为接口类型
     */
    public boolean isInterface()
    {
        return itf;
    }

    private String descriptor;

    private String name;

    private String owner;

    private boolean itf;
}
