package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFGv1NodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.LimitedGraphNodeManipulator;

public class ThrowableCaptureNode extends SRFNode {
    public ThrowableCaptureNode()
    {
        super(SRFGv1NodeTypes.THROWABLE_CAPTURE, new LimitedGraphNodeManipulator<>(0, 1));
    }
}