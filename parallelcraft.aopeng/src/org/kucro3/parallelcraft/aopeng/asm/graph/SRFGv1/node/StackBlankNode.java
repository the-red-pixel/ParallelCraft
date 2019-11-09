package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFGv1NodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.SRFStackElement;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.TerminalGraphNodeManipulator;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Objects;

public class StackBlankNode extends SRFNode {
    public StackBlankNode()
    {
        super(SRFGv1NodeTypes.STACK_BLANK, new TerminalGraphNodeManipulator<>(), true);
    }
}
