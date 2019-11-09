package org.kucro3.parallelcraft.aopeng.util;

import javax.annotation.Nonnull;
import java.util.Objects;

public interface Query<T> {
    public int size();

    public int remaining();

    public T peek();

    public T poll();

    public default boolean test(@Nonnull T object)
    {
        if (compare(object))
        {
            poll();
            return true;
        }

        return false;
    }

    public default boolean compare(@Nonnull T object)
    {
        Objects.requireNonNull(object);

        if (!hasRemaining())
            return false;

        return peek().equals(object);
    }

    public default boolean hasRemaining()
    {
        return remaining() != 0;
    }
}
