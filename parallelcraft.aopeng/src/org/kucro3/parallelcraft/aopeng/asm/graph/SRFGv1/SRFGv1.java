package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import org.kucro3.parallelcraft.aopeng.asm.graph.DifferentialVisitMeta;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SRFGv1 {
    public SRFGv1(@Nonnull SRFBlockNode root)
    {
        this(root, new DifferentialVisitMeta());
    }

    public SRFGv1(@Nonnull SRFBlockNode root,
                  @Nonnull DifferentialVisitMeta visitMeta)
    {
        this.root = Objects.requireNonNull(root, "root");
        this.visitMeta = Objects.requireNonNull(visitMeta, "visitMeta");
    }

    public @Nonnull SRFBlockNode getRoot()
    {
        return root;
    }

    public @Nonnull DifferentialVisitMeta getVisitMeta()
    {
        return visitMeta;
    }

    public void setVisitMeta(@Nonnull DifferentialVisitMeta visitMeta)
    {
        this.visitMeta = Objects.requireNonNull(visitMeta);
    }

    private final SRFBlockNode root;

    private DifferentialVisitMeta visitMeta;
}
