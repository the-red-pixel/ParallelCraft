package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

public class ThrowableHandlers implements Iterable<ThrowableHandler> {
    public void pushFirst(@Nonnull ThrowableHandler handler)
    {
        handlers.addFirst(Objects.requireNonNull(handler));
    }

    public boolean popFirst()
    {
        return handlers.removeFirst() != null;
    }

    public void pushTail(@Nonnull ThrowableHandler handler)
    {
        handlers.addLast(Objects.requireNonNull(handler));
    }

    public boolean popTail()
    {
        return handlers.removeLast() != null;
    }

    public void clear()
    {
        handlers.clear();
    }

    public int size()
    {
        return handlers.size();
    }

    public boolean remove(@Nonnull ThrowableHandler handler)
    {
        return handlers.remove(handler);
    }

    @Override
    public @Nonnull Iterator<ThrowableHandler> iterator()
    {
        return handlers.iterator();
    }

    private final LinkedList<ThrowableHandler> handlers = new LinkedList<>();
}
