package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TableSwitchInstructionNode extends InstructionNode {
    public TableSwitchInstructionNode(int opcode,
                                      int min,
                                      int max,
                                      @Nonnull SRFBlockNode defaultTarget,
                                      @Nonnull List<SRFBlockNode> targets)
    {
        super(opcode, TABLESWITCH_INSN);

        this.min = min;
        this.max = max;
        this.defaultTarget = Objects.requireNonNull(defaultTarget, "defaultTarget");
        this.targets = Objects.requireNonNull(targets, "targets");
    }

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

    public void setDefaultTarget(@Nonnull SRFBlockNode defaultTarget)
    {
        this.defaultTarget = Objects.requireNonNull(defaultTarget);
    }

    public void setTargets(List<SRFBlockNode> targets)
    {
        this.targets = Objects.requireNonNull(targets);
    }

    public void setMax(int max)
    {
        this.max = max;
    }

    public void setMin(int min)
    {
        this.min = min;
    }

    public @Nonnull SRFBlockNode getDefaultTarget()
    {
        return defaultTarget;
    }

    public @Nonnull List<SRFBlockNode> getTargets()
    {
        return targets;
    }

    public int getMax()
    {
        return max;
    }

    public int getMin()
    {
        return min;
    }

    private SRFBlockNode defaultTarget;

    private List<SRFBlockNode> targets;

    private int max;

    private int min;
}
