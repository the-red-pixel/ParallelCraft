package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFBlockNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查找表跳转指令节点。<br>
 *
 * 查找表跳转指令为：LOOKUPSWITCH。
 */
public class LookupSwitchInstructionNode extends InstructionNode {
    /**
     * 构造函数。
     *
     * @param opcode 指令码
     * @param defaultTarget 缺省跳转目标
     * @param keys 查找表项
     * @param targets 查找表项跳转目标
     *
     * @throws NullPointerException 若 defaultTarget、keys、targets 中存在 null 则抛出此错误
     */
    public LookupSwitchInstructionNode(int opcode,
                                       @Nonnull SRFBlockNode defaultTarget,
                                       @Nonnull List<Integer> keys,
                                       @Nonnull List<SRFBlockNode> targets)
    {
        super(opcode, LOOKUPSWITCH_INSN);

        this.defaultTarget = Predication.requireNonNull(defaultTarget, "defaultTarget");
        this.keys = Predication.requireNonNull(keys, "keys");
        this.targets = Predication.requireNonNull(targets, "targets");
    }

    /**
     * 构造函数。
     *
     * @param opcode 指令码
     * @param defaultTarget 缺省跳转目标
     * @param keys 查找表项
     * @param targets 查找表项跳转目标
     *
     * @throws NullPointerException 若 defaultTarget、keys、targets 中存在 null 则抛出此错误
     */
    public LookupSwitchInstructionNode(int opcode,
                                       @Nonnull SRFBlockNode defaultTarget,
                                       @Nonnull int[] keys,
                                       @Nonnull SRFBlockNode[] targets)
    {
        this(opcode, defaultTarget, toArrayList(keys), toArrayList(targets));
    }

    @Override
    public void accept(@Nonnull InsnList insnList,
                       @Nonnull Map<SRFBlockNode, LabelNode> blockLabelMap,
                       boolean createLabelIfAbsent)
    {
        insnList.add(new LookupSwitchInsnNode(
                require(getDefaultTarget(), blockLabelMap, createLabelIfAbsent),
                toArray(getKeys()),
                require(getTargets(), blockLabelMap, createLabelIfAbsent)));
    }

    private static int[] toArray(List<Integer> list)
    {
        int[] arr = new int[list.size()];

        int i = 0;
        for (Integer val : list)
            arr[i++] = val;

        return arr;
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
     * 设定查找表项。
     *
     * @param keys 查找表项
     *
     * @throws NullPointerException 若 keys 为 null 则抛出此错误
     */
    public void setKeys(@Nonnull List<Integer> keys)
    {
        this.keys = Predication.requireNonNull(keys);
    }

    /**
     * 设定查找表项跳转目标。
     *
     * @param targets 查找表项跳转目标
     *
     * @throws NullPointerException 若 targets 为 null 则抛出此错误
     */
    public void setTargets(@Nonnull List<SRFBlockNode> targets)
    {
        this.targets = Predication.requireNonNull(targets);
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
     * 返回查找表项。
     *
     * @return 查找表项
     */
    public @Nonnull List<Integer> getKeys()
    {
        return keys;
    }

    /**
     * 返回查找表项跳转目标。
     *
     * @return 查找表项跳转目标
     */
    public @Nonnull List<SRFBlockNode> getTargets()
    {
        return targets;
    }

    private SRFBlockNode defaultTarget;

    private List<Integer> keys;

    private List<SRFBlockNode> targets;
}
