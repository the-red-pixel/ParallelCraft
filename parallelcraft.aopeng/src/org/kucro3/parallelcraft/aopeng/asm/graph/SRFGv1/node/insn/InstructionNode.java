package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFGv1NodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.NormalGraphNodeManipulator;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * 指令节点。
 */
public abstract class InstructionNode extends SRFNode {
    /**
     * 构造函数，默认使用一般图节点操作器。<br>参见 {@link NormalGraphNodeManipulator}。
     *
     * @param opcode 指令码
     * @param type 指令类型
     */
    protected InstructionNode(int opcode, int type)
    {
        this(opcode, type, new NormalGraphNodeManipulator<>());
    }

    /**
     * 构造函数。
     *
     * @param opcode 指令码
     * @param type 指令类型
     * @param manipulator 图节点操作器
     */
    protected InstructionNode(int opcode,
                              int type,
                              @Nonnull GraphNodeManipulator<SRFNode> manipulator)
    {
        super(SRFGv1NodeTypes.INSTRUCTION, manipulator);

        this.opcode = opcode;
        this.type = type;
    }

    /**
     * 将此指令节点所表示的指令加入到指定指令列表中。<br>
     * 在必要的时候（通常是指令存在跳转操作）会查询所提供的 blockLabelMap 中的 SRF块 与标签的
     * 映射关系，并且在此种情况下标签是必要的，否则无法表示跳转目标。若 createLabelIfAbsent 为 true，
     * 则会在相应的映射关系不存在时构造一个新的标签，并且将其加入到映射中。
     *
     * @param insnList 指令列表
     * @param blockLabelMap 块与对于标签的映射
     * @param createLabelIfAbsent 是否在 blockLabelMap 中没有找到相应的标签映射时构造一个新的标签映射
     * @throws IllegalArgumentException 如果 createLabelIfAbsent 为 false 且没有在 blockLabelMap 中
     *      找到相应的标签映射则会抛出此错误
     */
    public abstract void accept(@Nonnull InsnList insnList,
                                @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                                boolean createLabelIfAbsent);


    static LabelNode require(SRFBlockNode key,
                             Map<SRFBlockNode, LabelNode> blockLabelMap,
                             boolean createLabelIfAbsent)
    {
        LabelNode label = createLabelIfAbsent
                ? blockLabelMap.computeIfAbsent(key, (unused) -> new LabelNode())
                : blockLabelMap.get(key);

        if (label == null)
            throw new IllegalArgumentException("unmapped block target");

        return label;
    }

    static LabelNode[] require(List<SRFBlockNode> keyList,
                               Map<SRFBlockNode, LabelNode> blockLabelMap,
                               boolean createLabelIfAbsent)
    {
        LabelNode[] labels = new LabelNode[keyList.size()];

        int i = 0;
        for (SRFBlockNode key : keyList)
            labels[i++] = require(key, blockLabelMap, createLabelIfAbsent);

        return labels;
    }

    /**
     * 返回指令码。
     *
     * @return 指令码
     */
    public int getOpcode()
    {
        return opcode;
    }

    /**
     * 返回指令类型。
     *
     * @return 指令类型
     */
    public int getInstructionType()
    {
        return type;
    }

    private final int type;

    private final int opcode;

    /**
     * 无操作数指令类型
     */
    public static final int VOID_INSN = 0;

    /**
     * 单整数操作数指令类型
     */
    public static final int INT_INSN = 1;

    /**
     * 本地变量操作指令类型
     */
    public static final int VAR_INSN = 2;

    /**
     * 类型操作数指令类型
     */
    public static final int TYPE_INSN = 3;

    /**
     * 域操作指令类型
     */
    public static final int FIELD_INSN = 4;

    /**
     * 方法操作指令类型
     */
    public static final int METHOD_INSN = 5;

    /**
     * 动态调用指令类型
     */
    public static final int INVOKE_DYNAMIC_INSN = 6;

    /**
     * 跳转指令类型
     */
    public static final int JUMP_INSN = 7;

    /**
     * 常量载入指令类型
     */
    public static final int LDC_INSN = 8;

    /**
     * 自增指令类型
     */
    public static final int IINC_INSN = 9;

    /**
     * 线性表跳转类型
     */
    public static final int TABLESWITCH_INSN = 10;

    /**
     * 查找表跳转类型
     */
    public static final int LOOKUPSWITCH_INSN = 11;

    /**
     * 多维数组分配指令类型
     */
    public static final int MULTIANEWARRAY_INSN = 12;
}
