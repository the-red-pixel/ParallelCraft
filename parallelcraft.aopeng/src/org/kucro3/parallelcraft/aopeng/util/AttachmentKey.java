package org.kucro3.parallelcraft.aopeng.util;

import javax.annotation.Nonnull;
import java.util.Objects;

public class AttachmentKey<T> {
    AttachmentKey(Class<?> type)
    {
        this.type = type;
    }

    public static <T> AttachmentKey<T> create(Class<T> type)
    {
        return new AttachmentKey<>(Objects.requireNonNull(type));
    }

    public @Nonnull Class<?> getType()
    {
        return type;
    }

    private final Class<?> type;
}
