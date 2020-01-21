package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.DifferentialVisitMeta;
import org.kucro3.parallelcraft.aopeng.asm.graph.DifferentialVisitable;
import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;
import org.kucro3.parallelcraft.aopeng.util.Attachable;
import org.kucro3.parallelcraft.aopeng.util.Attachment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * SRFG-v1 图节点实例。
 */
// Stack Related Flow Graph
public abstract class SRFNode implements Node<SRFNode>, DifferentialVisitable, Attachable {
    /**
     * 构造函数。
     *
     * @param type 节点类型
     * @param manipulator 图节点操作器
     * @param bypass 保留项
     *
     * @throws NullPointerException 若 type、manipulator 中存在 null 则抛出此错误
     */
    protected SRFNode(@Nonnull SRFNodeType type,
                      @Nonnull GraphNodeManipulator<SRFNode> manipulator,
                      boolean bypass)
    {
        this.type = Predication.requireNonNull(type, "type");
        this.manipulator = Predication.requireNonNull(manipulator, "manipulator");
        this.bypass = bypass;
    }

    /**
     * 构造函数。
     *
     * @param type 节点类型
     * @param manipulator 图节点操作器
     *
     * @throws NullPointerException 若 type、manipulator 中存在 null 则抛出此错误
     */
    protected SRFNode(@Nonnull SRFNodeType type,
                      @Nonnull GraphNodeManipulator<SRFNode> manipulator)
    {
        this(type, manipulator, false);
    }

    /**
     * 返回节点类型。
     *
     * @return 节点类型
     */
    public @Nonnull SRFNodeType getType()
    {
        return type;
    }

    /**
     * 验证节点的有效性。
     *
     * @return 是否有效
     */
    public boolean verify()
    {
        return true;
    }

    /**
     * 返回此节点目前是否为头节点（即不存在已连接的上路径）。
     *
     * @return 目前此节点是否为头节点
     */
    public boolean isHead()
    {
        return manipulator.getUpperPaths().isEmpty();
    }

    /**
     * 返回此节点目前是否为尾节点（即不存在已连接的下路径）。
     *
     * @return 目前此节点是否为尾节点
     */
    public boolean isTerminal()
    {
        return manipulator.getLowerPaths().isEmpty();
    }

    /**
     * 保留项。
     *
     * @return 保留项
     */
    public boolean isBypass()
    {
        return bypass;
    }

    /**
     * 设定此节点的逃逸目标。
     *
     * @param escapeTarget 逃逸目标
     */
    public void setEscapeTarget(@Nullable SRF escapeTarget)
    {
        this.escapeTarget = escapeTarget;
    }

    /**
     * 返回此节点是否存在逃逸目标。
     *
     * @return 此节点是否存在逃逸目标
     */
    public boolean hasEscapeTarget()
    {
        return escapeTarget != null;
    }

    /**
     * 返回此节点的逃逸目标。
     *
     * @return 此节点的逃逸目标，不存在则为 null
     */
    public @Nullable SRF getEscapeTarget()
    {
        return escapeTarget;
    }

    /**
     * 返回此节点的逃逸目标。
     *
     * @return 此节点的逃逸目标
     *
     * @throws IllegalStateException 若此时此节点不存在逃逸目标则抛出此错误
     */
    public @Nonnull SRF requireEscapeTarget()
    {
        if (escapeTarget == null)
            throw new IllegalStateException("Escape target not initialized");

        return escapeTarget;
    }

    /**
     * 返回此节点的图节点操作器。
     *
     * @return 图节点操作器
     */
    @Override
    public @Nonnull GraphNodeManipulator<SRFNode> getManipulator()
    {
        return manipulator;
    }

    /**
     * 返回此节点的差分访问戳。
     *
     * @return 差分访问戳
     */
    @Override
    public int getVisitStamp()
    {
        return this.visitStamp;
    }

    /**
     * 设定此节点的差分访问戳。
     *
     * @param visitStamp 差分访问戳
     */
    @Override
    public void setVisitStamp(int visitStamp)
    {
        this.visitStamp = visitStamp;
    }

    @Override
    public @Nonnull Attachment getAttachments()
    {
        if (attachment == null)
            return attachment = new Attachment();

        return attachment;
    }

    private SRF escapeTarget;

    private Attachment attachment;

    protected final GraphNodeManipulator<SRFNode> manipulator;

    private final SRFNodeType type;

    private int visitStamp = DifferentialVisitMeta.DIFFERENTIAL_VISITABLE_NODE_INITIAL;

    private final boolean bypass;
}
