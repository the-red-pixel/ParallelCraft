package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFGv1NodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.TerminalGraphNodeManipulator;

/**
 * 栈消隐节点。<br>
 * 栈消隐节点为终端节点。
 */
public class StackBlankNode extends SRFNode {
    /**
     * 构造函数。
     */
    public StackBlankNode()
    {
        super(SRFGv1NodeTypes.STACK_BLANK, new TerminalGraphNodeManipulator<>(), true);
    }
}
