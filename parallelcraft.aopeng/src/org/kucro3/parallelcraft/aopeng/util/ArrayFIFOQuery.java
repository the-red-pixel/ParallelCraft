package org.kucro3.parallelcraft.aopeng.util;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;

public class ArrayFIFOQuery<T> implements FIFOQuery<T> {
    public ArrayFIFOQuery(@Nonnull T[] array)
    {
        this(array, false);
    }

    @SuppressWarnings("unchecked")
    public ArrayFIFOQuery(@Nonnull T[] array, boolean copy)
    {
        if (copy)
        {
            this.array = (T[]) new Object[array.length];
            System.arraycopy(array, 0, this.array, 0, array.length);
        }
        else
            this.array = array;

        this.size = array.length;
        this.ptr = 0;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public int remaining()
    {
        return size - ptr;
    }

    @Override
    public T peek()
    {
        if (ptr < array.length)
            return array[ptr];

        throw new NoSuchElementException();
    }

    @Override
    public T poll()
    {
        if (ptr < array.length)
            return array[ptr++];

        throw new NoSuchElementException();
    }

    private final int size;

    private int ptr;

    private final T[] array;
}
