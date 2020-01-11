package org.kucro3.parallelcraft.aopeng.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Attachment {
    public @Nullable <T> T getAttachment(@Nonnull AttachmentKey<T> key)
    {
        return (T) attachmentMap.get(Objects.requireNonNull(key));
    }

    public boolean hasAttachment(@Nonnull AttachmentKey<?> key)
    {
        return attachmentMap.containsKey(Objects.requireNonNull(key));
    }

    public @Nonnull <T> T requireAttachment(@Nonnull AttachmentKey<T> key)
    {
        T object = getAttachment(key);

        if (object == null)
            throw new NoSuchElementException(key.toString());

        return object;
    }

    public @Nonnull <T, E extends Throwable> T requireAttachment(@Nonnull AttachmentKey<T> key,
                                                                 @Nonnull Supplier<E> exceptionSupplier)
            throws E
    {
        T object = getAttachment(key);

        if (object == null)
            throw exceptionSupplier.get();

        return object;
    }

    public void removeAttachment(@Nonnull AttachmentKey<?> key)
    {
        attachmentMap.remove(key);
    }

    public @Nullable <T> T putAttachment(@Nonnull AttachmentKey<T> key, @Nonnull T value)
    {
        if (key instanceof TypeAttributed)
            if (!((TypeAttributed) Objects.requireNonNull(key)).getType().isInstance(Objects.requireNonNull(value)))
                throw new ClassCastException();

        return (T) attachmentMap.put(key, value);
    }

    public <T> void setAttachment(@Nonnull AttachmentKey<T> key, @Nonnull T value)
    {
        putAttachment(key, value);
    }

    private final Map<AttachmentKey<?>, Object> attachmentMap = new ConcurrentHashMap<>();
}
