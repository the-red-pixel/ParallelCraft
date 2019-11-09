package org.kucro3.parallelcraft.aopeng.asm;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

// copy on write
public class StackTable {
    StackTable(StackElement[] elements)
    {
        this.elements = elements;
    }

    public static @Nonnull StackTable empty()
    {
        return EMPTY;
    }

    public @Nonnull StackElement[] peek(@Nonnegative int count)
    {
        if (count < 0)
            throw new IllegalArgumentException("count cannot be negative");

        if (count > elements.length)
            throw new IllegalArgumentException("Peeking " + count + " element(s)," +
                    " but the stack is only " + elements.length + " in depth.");

        StackElement[] modified = new StackElement[elements.length - count];

        System.arraycopy(elements, 0, modified, 0, modified.length);

        return modified;
    }

    public @Nonnull StackElement peek()
    {
        if (elements.length == 0)
            throw new IllegalArgumentException("Peeking empty stack");

        return elements[0];
    }

    public @Nonnull StackTable pop()
    {
        if (elements.length == 0)
            throw new IllegalStateException("Poping empty stack");

        if (elements.length == 1)
            return EMPTY;

        int size = this.elements.length - 1;

        StackElement[] elements = new StackElement[size];

        System.arraycopy(this.elements, 1, elements, 0, size);

        return new StackTable(elements);
    }

    public @Nonnull StackTable pop(@Nonnegative int count)
    {
        if (count < 0)
            throw new IllegalArgumentException("count cannot be negative");

        int d = elements.length - count;

        if (d < 0)
            throw new IllegalArgumentException("stack underflow");

        if (d == 0)
            return EMPTY;

        StackElement[] elements = new StackElement[d];

        System.arraycopy(this.elements, count, elements, 0, d);

        return new StackTable(elements);
    }

    public @Nonnull StackTable push(@Nonnull StackElement element)
    {
        StackElement[] elements = new StackElement[this.elements.length + 1];

        elements[this.elements.length] = element;

        return new StackTable(elements);
    }

    public @Nonnull StackTable push(@Nonnull StackElement... elements)
    {
        int len = elements.length + this.elements.length;

        StackElement[] merged = new StackElement[len];

        System.arraycopy(this.elements, 0, merged, elements.length, merged.length);
        System.arraycopy(elements, 0, merged, 0, elements.length);

        return new StackTable(merged);
    }

    public @Nonnull StackElement get(@Nonnegative int index)
    {
        return elements[index];
    }

    public @Nonnegative int size()
    {
        return elements.length;
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(elements);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof StackTable))
            return false;

        StackTable object = (StackTable) obj;

        if (object.elements.length != elements.length)
            return false;

        for (int i = 0; i < elements.length; i++)
            if (!object.elements[i].equals(elements[i]))
                return false;

        return true;
    }

    private final StackElement[] elements;

    private static final StackTable EMPTY = new StackTable(new StackElement[0]);

    public static class StackElement
    {
        public StackElement(@Nonnegative int index,
                            @Nonnull Type type)
        {
            this.index = index;
            this.type = Objects.requireNonNull(type);
        }

        public @Nonnull Type getType()
        {
            return type;
        }

        public @Nonnegative int getIndex()
        {
            return index;
        }

        @Override
        public int hashCode()
        {
            return type.hashCode();
        }

        @Override
        public boolean equals(Object obj)
        {
            if (!(obj instanceof StackElement))
                return false;

            StackElement object = (StackElement) obj;

            if (!object.type.equals(type))
                return false;

            return object.index == index;
        }

        private final Type type;

        private final int index;
    }
}
