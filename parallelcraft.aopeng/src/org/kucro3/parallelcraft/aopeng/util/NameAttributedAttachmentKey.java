package org.kucro3.parallelcraft.aopeng.util;

import javax.annotation.Nonnull;
import java.util.Objects;

public class NameAttributedAttachmentKey<T> extends AttachmentKey<T> implements NameAttributed {
    NameAttributedAttachmentKey(String name)
    {
        this.name = name;
    }

    public static <T> NameAttributedAttachmentKey<T> create(@Nonnull String name)
    {
        return new NameAttributedAttachmentKey<>(Objects.requireNonNull(name));
    }

    @Override
    public @Nonnull String getName()
    {
        return name;
    }

    @Override
    public @Nonnull String toString()
    {
        return getName();
    }

    private final String name;
}
