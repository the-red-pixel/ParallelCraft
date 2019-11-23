package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LookupSwitchInstructionNode extends InstructionNode {
    public LookupSwitchInstructionNode(int opcode,
                                       @Nonnull SRFBlockNode defaultTarget,
                                       @Nonnull List<Integer> keys,
                                       @Nonnull List<SRFBlockNode> targets)
    {
        super(opcode, LOOKUPSWITCH_INSN);

        this.defaultTarget = Objects.requireNonNull(defaultTarget, "defaultTarget");
        this.keys = Objects.requireNonNull(keys, "keys");
        this.targets = Objects.requireNonNull(targets, "targets");
    }

    public LookupSwitchInstructionNode(int opcode,
                                       @Nonnull SRFBlockNode defaultTarget,
                                       @Nonnull int[] keys,
                                       @Nonnull SRFBlockNode[] targets)
    {
        this(opcode, defaultTarget, toArrayList(keys), toArrayList(targets));
    }

    private static List<SRFBlockNode> toArrayList(SRFBlockNode[] targets)
    {
        ArrayList<SRFBlockNode> list = new ArrayList<>(targets.length);

        list.addAll(List.of(targets));

        return list;
    }

    private static List<Integer> toArrayList(int[] keys)
    {
        ArrayList<Integer> list = new ArrayList<>(keys.length);

        for (int v : keys)
            list.add(v);

        return list;
    }

    public void setDefaultTarget(@Nonnull SRFBlockNode defaultTarget)
    {
        this.defaultTarget = Objects.requireNonNull(defaultTarget);
    }

    public void setKeys(@Nonnull List<Integer> keys)
    {
        this.keys = Objects.requireNonNull(keys);
    }

    public void setTargets(@Nonnull List<SRFBlockNode> targets)
    {
        this.targets = Objects.requireNonNull(targets);
    }

    public @Nonnull SRFBlockNode getDefaultTarget()
    {
        return defaultTarget;
    }

    public @Nonnull List<Integer> getKeys()
    {
        return keys;
    }

    public @Nonnull List<SRFBlockNode> getTargets()
    {
        return targets;
    }

    private SRFBlockNode defaultTarget;

    private List<Integer> keys;

    private List<SRFBlockNode> targets;
}
