package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1X.node;

import org.kucro3.parallelcraft.aopeng.asm.graph.LocalRef;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1X.SRFGv1XNodeTypes;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.LimitedGraphNodeManipulator;

import javax.annotation.Nonnull;

public class LocalStoreNode extends LocalNodeBase {
    public LocalStoreNode(@Nonnull LocalRef localRef)
    {
        super(SRFGv1XNodeTypes.LOCAL_STORE,
                new LimitedGraphNodeManipulator<>(1, 0),
                localRef);
    }
}
