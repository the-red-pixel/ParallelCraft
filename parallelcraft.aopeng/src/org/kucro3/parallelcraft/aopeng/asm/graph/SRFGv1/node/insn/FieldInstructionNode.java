package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 域操作指令节点。<br>
 *
 * 域操作指令包括：GETSTATIC，PUTSTATIC，GETFIELD，PUTFIELD。
 *
 * @see InstructionNode
 */
public class FieldInstructionNode extends InstructionNode {
    /**
     * 构造函数。
     *
     * @param opcode 指令码
     * @param owner 目标域所在的类的内部名称
     * @param name 目标域名称
     * @param descriptor 目标域的类型描述符
     *
     * @throws NullPointerException owner、name、descriptor 其中存在 null 时抛出此错误
     */
    public FieldInstructionNode(int opcode,
                                @Nonnull String owner,
                                @Nonnull String name,
                                @Nonnull String descriptor)
    {
        super(opcode, FIELD_INSN);

        this.descriptor = Predication.requireNonNull(descriptor, "descriptor");
        this.name = Predication.requireNonNull(name, "name");
        this.owner = Predication.requireNonNull(owner, "owner");
    }

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new FieldInsnNode(getOpcode(), getOwner(), getName(), getDescriptor()));
    }

    /**
     * 设定目标域的类型描述符。
     *
     * @param descriptor 目标域的类型描述符
     *
     * @throws NullPointerException descriptor 为 null 时抛出此错误
     */
    public void setDescriptor(@Nonnull String descriptor)
    {
        this.descriptor = Predication.requireNonNull(descriptor);
    }

    /**
     * 设定目标域的名称。
     *
     * @param name 目标域的名称
     *
     * @throws NullPointerException name 为 null 时抛出此错误
     */
    public void setName(@Nonnull String name)
    {
        this.name = Predication.requireNonNull(name);
    }

    /**
     * 设定目标域所在类的内部名称。
     *
     * @param owner 目标域所在类的内部名称
     *
     * @throws NullPointerException owner 为 null 时抛出此错误
     */
    public void setOwner(@Nonnull String owner)
    {
        this.owner = Predication.requireNonNull(owner);
    }

    /**
     * 返回目标域的类型描述符。
     *
     * @return 目标域的类型描述符
     */
    public @Nonnull String getDescriptor()
    {
        return descriptor;
    }

    /**
     * 返回目标域的名称。
     *
     * @return 目标域的名称
     */
    public @Nonnull String getName()
    {
        return name;
    }

    /**
     * 返回目标域所在类的内部名称。
     *
     * @return 目标域所在类的内部名称
     */
    public @Nonnull String getOwner()
    {
        return owner;
    }

    private String descriptor;

    private String name;

    private String owner;
}
