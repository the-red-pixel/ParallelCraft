package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import org.kucro3.parallelcraft.aopeng.util.Attachable;
import org.kucro3.parallelcraft.aopeng.util.Attachment;

import javax.annotation.Nonnull;
import java.util.Objects;

public class SRF implements Attachable {
    public SRF(@Nonnull SRFNode root)
    {
        this.root = Objects.requireNonNull(root);
    }

    @Override
    public @Nonnull Attachment getAttachments()
    {
        return attachment;
    }

    public @Nonnull
    SRFNode getRoot()
    {
        return root;
    }

    public void setRoot(@Nonnull SRFNode root)
    {
        this.root = Objects.requireNonNull(root);
    }

    private SRFNode root;

    private final Attachment attachment = new Attachment();
}
