package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFGv1NodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.HeadGraphNodeManipulator;

/**
 * 栈复位节点。<br>
 * 栈复位节点为头节点。
 */
public class StackRestoreNode extends SRFNode {
    /**
     * 构造函数。
     */
    public StackRestoreNode()
    {
        super(SRFGv1NodeTypes.STACK_RESTORE, new HeadGraphNodeManipulator<>(), true);
    }
}