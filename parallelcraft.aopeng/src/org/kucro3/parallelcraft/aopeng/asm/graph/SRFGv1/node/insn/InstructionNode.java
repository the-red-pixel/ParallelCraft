package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFGv1NodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.NormalGraphNodeManipulator;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class InstructionNode extends SRFNode {
    protected InstructionNode(int opcode, int type)
    {
        this(opcode, type, new NormalGraphNodeManipulator<>());
    }

    protected InstructionNode(int opcode,
                              int type,
                              @Nonnull GraphNodeManipulator<SRFNode> manipulator)
    {
        super(SRFGv1NodeTypes.INSTRUCTION, manipulator);

        this.opcode = opcode;
        this.type = type;
    }

    public int getOpcode()
    {
        return opcode;
    }

    public int getInstructionType()
    {
        return type;
    }

    private final int type;

    private final int opcode;

    public static final int VOID_INSN = 0;

    public static final int INT_INSN = 1;

    public static final int VAR_INSN = 2;

    public static final int TYPE_INSN = 3;

    public static final int FIELD_INSN = 4;

    public static final int METHOD_INSN = 5;

    public static final int INVOKE_DYNAMIC_INSN = 6;

    public static final int JUMP_INSN = 7;

    public static final int LDC_INSN = 8;

    public static final int IINC_INSN = 9;

    public static final int TABLESWITCH_INSN = 10;

    public static final int LOOKUPSWITCH_INSN = 11;

    public static final int MULTIANEWARRAY_INSN = 12;
}
