package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.DifferentialVisitMeta;
import org.kucro3.parallelcraft.aopeng.asm.graph.DifferentialVisitable;
import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.LowerLimitedGraphNodeManipulator;
import org.kucro3.parallelcraft.aopeng.util.Attachable;
import org.kucro3.parallelcraft.aopeng.util.Attachment;

import javax.annotation.Nonnull;

/**
 * 栈相关流块节点。<br>
 *
 * 本实例作为栈相关流块的容器以及栈相关流图的构成部分。
 */
public class SRFBlockNode implements Node<SRFBlockNode>, DifferentialVisitable, Attachable {
    /**
     * 构造函数。
     *
     * @param flowBlock 栈相关流块
     *
     * @throws NullPointerException 若 flowBlock 为 null 则抛出此错误
     */
    public SRFBlockNode(@Nonnull SRFBlock flowBlock)
    {
        this.flowBlock = Predication.requireNonNull(flowBlock);
        this.manipulator = new LowerLimitedGraphNodeManipulator<>(1);
    }

    @Override
    public @Nonnull GraphNodeManipulator<SRFBlockNode> getManipulator()
    {
        return manipulator;
    }

    /**
     * 返回栈相关流块。
     *
     * @return 栈相关流块
     */
    public @Nonnull SRFBlock getBlock()
    {
        return flowBlock;
    }

    /**
     * 设定栈相关流块。
     *
     * @param flowBlock 栈相关流块
     *
     * @throws NullPointerException 若 flowBlock 为 null 则抛出此错误
     */
    public void setBlock(@Nonnull SRFBlock flowBlock)
    {
        this.flowBlock = Predication.requireNonNull(flowBlock);
    }

    @Override
    public int getVisitStamp()
    {
        return this.visitStamp;
    }

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

    private int visitStamp = DifferentialVisitMeta.DIFFERENTIAL_VISITABLE_NODE_INITIAL;

    private SRFBlock flowBlock;

    private final GraphNodeManipulator<SRFBlockNode> manipulator;

    private Attachment attachment;
}
