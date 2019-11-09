package org.kucro3.parallelcraft.aopeng.util;

import javax.annotation.Nonnull;
import java.util.Objects;

public class IdentityFIFOQuery<T> implements FIFOQuery<T> {
    public IdentityFIFOQuery(@Nonnull FIFOQuery<T> query)
    {
        this.query = Objects.requireNonNull(query);
    }

    @Override
    public int size()
    {
        return query.size();
    }

    @Override
    public int remaining()
    {
        return query.remaining();
    }

    @Override
    public T peek()
    {
        return query.peek();
    }

    @Override
    public T poll()
    {
        return query.poll();
    }

    @Override
    public boolean compare(@Nonnull T object)
    {
        Objects.requireNonNull(object);

        if (!hasRemaining())
            return false;

        if (object == peek())
        {
            poll();
            return true;
        }

        return false;
    }

    private final FIFOQuery<T> query;
}
