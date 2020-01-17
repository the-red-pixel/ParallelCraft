package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 线性表跳转指令节点。<br>
 *
 * 线性表跳转指令为：TABLESWITCH。
 */
public class TableSwitchInstructionNode extends InstructionNode {
    /**
     * 构造函数。
     *
     * @param opcode 指令码
     * @param min 线性表开始值
     * @param max 线性表结束值
     * @param defaultTarget 缺省跳转目标
     * @param targets 线性跳转目标表
     *
     * @throws NullPointerException 若 defaultTarget、targets 中存在 null 则抛出此错误
     */
    public TableSwitchInstructionNode(int opcode,
                                      int min,
                                      int max,
                                      @Nonnull SRFBlockNode defaultTarget,
                                      @Nonnull List<SRFBlockNode> targets)
    {
        super(opcode, TABLESWITCH_INSN);

        this.min = min;
        this.max = max;
        this.defaultTarget = Predication.requireNonNull(defaultTarget, "defaultTarget");
        this.targets = Predication.requireNonNull(targets, "targets");
    }

    /**
     * 构造函数。
     *
     * @param opcode 指令码
     * @param min 线性表开始值
     * @param max 线性表结束值
     * @param defaultTarget 缺省跳转目标
     * @param targets 线性跳转目标表
     *
     * @throws NullPointerException 若 defaultTarget、targets 中存在 null 则抛出此错误
     */
    public TableSwitchInstructionNode(int opcode,
                                      int min,
                                      int max,
                                      @Nonnull SRFBlockNode defaultTarget,
                                      @Nonnull SRFBlockNode... targets)
    {
        this(opcode, min, max, defaultTarget, toArrayList(targets));
    }


    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new TableSwitchInsnNode(
                getMin(),
                getMax(),
                require(getDefaultTarget(), blockLabelMap, createLabelIfAbsent),
                require(getTargets(), blockLabelMap, createLabelIfAbsent)));
    }

    private static List<SRFBlockNode> toArrayList(SRFBlockNode[] targets)
    {
        ArrayList<SRFBlockNode> list = new ArrayList<>(targets.length);

        list.addAll(List.of(targets));

        return list;
    }

    /**
     * 设定缺省跳转目标。
     *
     * @param defaultTarget 缺省跳转目标
     *
     * @throws NullPointerException 若 defaultTarget 为 null 则抛出此错误
     */
    public void setDefaultTarget(@Nonnull SRFBlockNode defaultTarget)
    {
        this.defaultTarget = Predication.requireNonNull(defaultTarget);
    }

    /**
     * 设定线性跳转目标表。
     *
     * @param targets 缺省跳转目标表
     *
     * @throws NullPointerException 若 targets 为 null 则抛出此错误
     */
    public void setTargets(List<SRFBlockNode> targets)
    {
        this.targets = Predication.requireNonNull(targets);
    }

    /**
     * 设定线性表结束值。
     *
     * @param max 线性表结束值
     */
    public void setMax(int max)
    {
        this.max = max;
    }

    /**
     * 设定线性表开始值。
     *
     * @param min 线性表开始值
     */
    public void setMin(int min)
    {
        this.min = min;
    }

    /**
     * 返回缺省跳转目标。
     *
     * @return 缺省跳转目标
     */
    public @Nonnull SRFBlockNode getDefaultTarget()
    {
        return defaultTarget;
    }

    /**
     * 返回线性跳转目标表。
     *
     * @return 线性跳转目标表
     */
    public @Nonnull List<SRFBlockNode> getTargets()
    {
        return targets;
    }

    /**
     * 返回线性表结束值。
     *
     * @return 线性表结束值
     */
    public int getMax()
    {
        return max;
    }

    /**
     * 返回线性表开始值。
     *
     * @return 线性表开始值
     */
    public int getMin()
    {
        return min;
    }

    private SRFBlockNode defaultTarget;

    private List<SRFBlockNode> targets;

    private int max;

    private int min;
}
