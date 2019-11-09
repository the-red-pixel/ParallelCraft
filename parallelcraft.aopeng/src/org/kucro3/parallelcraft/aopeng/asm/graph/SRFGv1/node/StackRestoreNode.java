package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFGv1NodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.HeadGraphNodeManipulator;

import javax.annotation.Nonnegative;

public class StackRestoreNode extends SRFNode {
    public StackRestoreNode()
    {
        super(SRFGv1NodeTypes.STACK_RESTORE, new HeadGraphNodeManipulator<>(), true);
    }
}