package org.kucro3.parallelcraft.aopeng.util;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;

public class ArrayLIFOQuery<T> implements LIFOQuery<T> {
    public ArrayLIFOQuery(@Nonnull T[] array)
    {
        this(array, false);
    }

    @SuppressWarnings("unchecked")
    public ArrayLIFOQuery(@Nonnull T[] array, boolean copy)
    {
        if (copy)
        {
            this.array = (T[]) new Object[array.length];
            System.arraycopy(array, 0, this.array, 0, array.length);
        }
        else
            this.array = array;

        this.size = array.length;
        this.ptr = array.length - 1;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public int remaining()
    {
        return ptr + 1;
    }

    @Override
    public T peek()
    {
        if (ptr < 0)
            throw new NoSuchElementException();

        return array[ptr];
    }

    @Override
    public T poll()
    {
        if (ptr < 0)
            throw new NoSuchElementException();

        return array[ptr--];
    }

    private final int size;

    private int ptr;

    private final T[] array;
}
