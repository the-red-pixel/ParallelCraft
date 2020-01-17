package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.DifferentialVisitMeta;

import javax.annotation.Nonnull;

public class SRFGv1 {
    public SRFGv1(@Nonnull SRFBlockNode root)
    {
        this(root, new DifferentialVisitMeta());
    }

    public SRFGv1(@Nonnull SRFBlockNode root,
                  @Nonnull DifferentialVisitMeta visitMeta)
    {
        this.root = Predication.requireNonNull(root, "root");
        this.visitMeta = Predication.requireNonNull(visitMeta, "visitMeta");
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
        this.visitMeta = Predication.requireNonNull(visitMeta);
    }

    public void nextVisitMeta()
    {
        this.visitMeta = this.visitMeta.next();
    }

    private final SRFBlockNode root;

    private DifferentialVisitMeta visitMeta;
}
