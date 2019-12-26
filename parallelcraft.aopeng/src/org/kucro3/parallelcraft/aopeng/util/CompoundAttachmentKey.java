package org.kucro3.parallelcraft.aopeng.util;

import javax.annotation.Nonnull;
import java.util.Objects;

public class CompoundAttachmentKey<T> extends AttachmentKey<T> implements NameAttributed, TypeAttributed {
    CompoundAttachmentKey(String name, Class<T> type)
    {
        this.name = name;
        this.type = type;
    }

    public static <T> CompoundAttachmentKey<T> create(@Nonnull String name,
                                                      @Nonnull Class<T> type)
    {
        return new CompoundAttachmentKey<>(
                Objects.requireNonNull(name, "name"),
                Objects.requireNonNull(type, "type"));
    }

    @Override
    public @Nonnull String getName()
    {
        return name;
    }

    @Override
    public @Nonnull Class<T> getType()
    {
        return type;
    }

    @Override
    public @Nonnull String toString()
    {
        return getName();
    }

    private final String name;

    private final Class<T> type;
}
