package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFGv1NodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.LimitedGraphNodeManipulator;

/**
 * 异常捕获节点。<br>
 * 异常捕获节点为头节点且只允许存在一个下路径。
 */
public class ThrowableCaptureNode extends SRFNode {
    /**
     * 构造函数。
     */
    public ThrowableCaptureNode()
    {
        super(SRFGv1NodeTypes.THROWABLE_CAPTURE, new LimitedGraphNodeManipulator<>(0, 1));
    }
}