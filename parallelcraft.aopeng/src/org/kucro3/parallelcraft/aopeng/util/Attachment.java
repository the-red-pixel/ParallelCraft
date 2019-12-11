package org.kucro3.parallelcraft.aopeng.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

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

    public void removeAttachment(@Nonnull AttachmentKey<?> key)
    {
        attachmentMap.remove(key);
    }

    public @Nullable <T> T putAttachment(@Nonnull AttachmentKey<T> key, @Nonnull T value)
    {
        if (!Objects.requireNonNull(key).getType().isInstance(Objects.requireNonNull(value)))
            throw new ClassCastException();

        return (T) attachmentMap.put(key, value);
    }

    public <T> void setAttachment(@Nonnull AttachmentKey<T> key, @Nonnull T value)
    {
        putAttachment(key, value);
    }

    private final Map<AttachmentKey<?>, Object> attachmentMap = new WeakHashMap<>();
}
