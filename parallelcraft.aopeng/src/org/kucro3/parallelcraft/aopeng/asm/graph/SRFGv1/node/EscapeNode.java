package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRF;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFGv1NodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.LimitedGraphNodeManipulator;

import javax.annotation.Nonnull;

/**
 * 逃逸节点。
 */
public class EscapeNode extends SRFNode {
    /**
     * 构造函数。
     *
     * @param target 目标栈相关流
     *
     * @throws NullPointerException 若 target 为 null 则抛出此错误
     */
    public EscapeNode(@Nonnull SRF target)
    {
        super(SRFGv1NodeTypes.ESCAPE, new LimitedGraphNodeManipulator<>(1, 1), true);

        this.target = Predication.requireNonNull(target);
    }

    /**
     * 返回目标栈相关流。
     *
     * @return 目标栈相关流
     */
    public @Nonnull SRF getTarget()
    {
        return target;
    }

    /**
     * 设定目标栈相关流。
     *
     * @param target 目标栈相关流
     *
     * @throws NullPointerException 若 target 为 null 则抛出此错误
     */
    public void setTarget(@Nonnull SRF target)
    {
        this.target = Predication.requireNonNull(target);
    }

    private SRF target;
}
