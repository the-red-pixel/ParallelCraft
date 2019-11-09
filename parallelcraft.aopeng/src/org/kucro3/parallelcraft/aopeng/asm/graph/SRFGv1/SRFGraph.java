package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SRFGraph {
    public SRFGraph(@Nonnull SRFBlockNode root)
    {
        this.root = Objects.requireNonNull(root);
    }

    public @Nonnull SRFBlockNode getRoot()
    {
        return root;
    }

    private final SRFBlockNode root;
}
