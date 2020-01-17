package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.StackBlankNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.StackRestoreNode;
import org.kucro3.parallelcraft.aopeng.util.Attachable;
import org.kucro3.parallelcraft.aopeng.util.Attachment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SRFBlock implements Attachable {
    public SRFBlock()
    {
        this.srfs = new ArrayList<>();
    }

    public @Nonnull List<SRF> getSRFs()
    {
        return srfs;
    }

    @Override
    public @Nonnull Attachment getAttachments()
    {
        if (attachment == null)
            return attachment = new Attachment();

        return attachment;
    }

    public @Nonnull ThrowableHandlers getThrowableHandlers()
    {
        return handlers;
    }

    public boolean isEmpty()
    {
        return srfs.isEmpty();
    }

    public @Nullable StackRestoreNode getStackRestoration()
    {
        return stackRestoration;
    }

    public boolean hasStackRestoration()
    {
        return stackRestoration != null;
    }

    public void setStackRestoration(@Nonnull StackRestoreNode stackRestoration)
    {
        this.stackRestoration = Predication.requireNonNull(stackRestoration);
    }

    public @Nullable StackBlankNode getStackBlank()
    {
        return stackBlank;
    }

    public boolean hasStackBlank()
    {
        return stackBlank != null;
    }

    public void setStackBlank(@Nonnull StackBlankNode stackBlank)
    {
        this.stackBlank = Predication.requireNonNull(stackBlank);
    }

    private StackRestoreNode stackRestoration;

    private StackBlankNode stackBlank;

    private final List<SRF> srfs;

    private final ThrowableHandlers handlers = new ThrowableHandlers();

    private Attachment attachment;
}