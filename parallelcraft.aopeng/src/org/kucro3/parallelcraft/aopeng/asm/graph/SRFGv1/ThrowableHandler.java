package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ThrowableHandler {
    public ThrowableHandler(@Nonnull String handlingType)
    {
        setHandlingType(handlingType);
    }

    public ThrowableHandler(@Nonnull SRFBlockNode handler,
                            @Nonnull String handlingType)
    {
        setHandler(handler);
        setHandlingType(handlingType);
    }

    public @Nonnull SRFBlockNode getHandler()
    {
        if (!hasHandler())
            throw new IllegalStateException("Exception handler not initialized");

        return handler;
    }

    public @Nonnull String getHandlingType()
    {
        return handlingType;
    }

    public void initHandler(@Nonnull SRFBlockNode handler)
    {
        if (hasHandler())
            throw new IllegalStateException("Exception handler already initialized");

        setHandler(handler);
    }

    public boolean hasHandler()
    {
        return this.handler != null;
    }

    public void setHandler(@Nonnull SRFBlockNode handler)
    {
        this.handler = Objects.requireNonNull(handler, "handler");
    }

    public void setHandlingType(@Nonnull String handlingType)
    {
        this.handlingType = Objects.requireNonNull(handlingType, "handlingType");
    }

    private SRFBlockNode handler;

    private String handlingType;
}
