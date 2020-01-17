package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * 无操作数指令节点。<br>
 *
 * 无操作数指令包括：NOP、ACONST_NULL、ICONST_M1、ICONST_0、ICONST_1、ICONST_2、ICONST_3、ICONST_4、ICONST_5、
 * LCONST_0、LCONST_1、FCONST_0、FCONST_1、FCONST_2、DCONST_0、DCONST_1、IALOAD、LALOAD、FALOAD、DALOAD、
 * AALOAD、BALOAD、CALOAD、SALOAD、IASTORE、LASTORE、FASTORE、DASTORE、AASTORE、BASTORE、CASTORE、SASTORE、
 * POP、POP2、DUP、DUP_X1、DUP_X2、DUP2、DUP2_X1、DUP2_X2、SWAP、IADD、LADD、FADD、DADD、ISUB、LSUB、FSUB、
 * DSUB、IMUL、LMUL、FMUL、DMUL、IDIV、LDIV、FDIV、DDIV、IREM、LREM、FREM、DREM、INEG、LNEG、FNEG、DNEG、
 * ISHL、LSHL、ISHR、LSHR、IUSHR、LUSHR、IAND、LAND、IOR、LOR、IXOR、LXOR、I2F、I2L、I2D、L2I、L2F、L2D、
 * F2I、F2L、F2D、D2I、D2L、D2F、I2B、I2C、I2S、LCMP、FCMPL、FCMPG、DCMPL、DCMPG、IRETURN、FRETURN、DRETURN、
 * ARETURN、RETURN、ARRAYLENGTH、ATHROW、MONITORENTER、MONITOREXIT。
 *
 * @see InstructionNode
 */
public class VoidInstructionNode extends InstructionNode {
    /**
     * 构造函数。
     *
     * @param opcode 指令码
     */
    public VoidInstructionNode(int opcode)
    {
        super(opcode, VOID_INSN);
    }

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new InsnNode(getOpcode()));
    }
}
