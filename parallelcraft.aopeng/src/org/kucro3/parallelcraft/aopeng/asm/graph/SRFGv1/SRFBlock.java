package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import org.kucro3.parallelcraft.aopeng.asm.graph.VisitMeta;
import org.kucro3.parallelcraft.aopeng.util.Attachable;
import org.kucro3.parallelcraft.aopeng.util.Attachment;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SRFBlock implements Attachable {
    public SRFBlock(@Nonnull VisitMeta meta)
    {
        this.srfs = new ArrayList<>();
        this.meta = Objects.requireNonNull(meta);
    }

    boolean visited(SRFNode node)
    {
        return node.visitStamp >= meta.getVisitStamp();
    }

    void visit(SRFNode node)
    {
        node.visitStamp = meta.getVisitStamp();
    }

    public @Nonnull List<SRF> getSRFs()
    {
        return srfs;
    }

    public @Nonnull VisitMeta getGraphVisitMeta()
    {
        return meta;
    }

    @Override
    public @Nonnull Attachment getAttachments()
    {
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

    private final VisitMeta meta;

    private final List<SRF> srfs;

    private final ThrowableHandlers handlers = new ThrowableHandlers();

    private final Attachment attachment = new Attachment();
}