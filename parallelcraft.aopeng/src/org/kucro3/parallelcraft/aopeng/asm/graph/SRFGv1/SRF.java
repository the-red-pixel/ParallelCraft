package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.util.Attachable;
import org.kucro3.parallelcraft.aopeng.util.Attachment;

import javax.annotation.Nonnull;

public class SRF implements Attachable {
    public SRF(@Nonnull SRFNode root)
    {
        this.root = Predication.requireNonNull(root);
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
        this.root = Predication.requireNonNull(root);
    }

    private SRFNode root;

    private final Attachment attachment = new Attachment();
}
