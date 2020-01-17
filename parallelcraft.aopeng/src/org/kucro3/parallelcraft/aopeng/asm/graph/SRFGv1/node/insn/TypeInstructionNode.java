package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.TypeInsnNode;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 类型操作数指令节点。<br>
 *
 * 类型操作数为类的内部名称。类型操作数指令包括：NEW、ANEWARRAY、CHECKCAST、INSTANCEOF。
 *
 * @see InstructionNode
 */
public class TypeInstructionNode extends InstructionNode {
    public TypeInstructionNode(int opcode,
                               @Nonnull String type)
    {
        super(opcode, TYPE_INSN);

        this.type = Predication.requireNonNull(type);
    }

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new TypeInsnNode(getOpcode(), getTypeOperand()));
    }

    /**
     * 设定类型操作数。
     *
     * @param type 类型操作数
     */
    public void setTypeOperand(@Nonnull String type)
    {
        this.type = Predication.requireNonNull(type);
    }

    /**
     * 返回类型操作数。
     *
     * @return 类型操作数
     */
    public @Nonnull String getTypeOperand()
    {
        return type;
    }

    private String type;
}
