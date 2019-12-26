package org.kucro3.parallelcraft.aopeng.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
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

    public @Nonnull <T> T computeIfAbsent(@Nonnull AttachmentKey<T> key,
                                          @Nonnull Function<AttachmentKey<T>, T> computation)
    {
        if (key instanceof TypeAttributed)
            return (T) attachmentMap.computeIfAbsent(key,
                    (Function<? super AttachmentKey<?>, ?>) (k) -> {
                        T object = computation.apply((AttachmentKey<T>) k);

                        if (!((TypeAttributed) k).getType().isInstance(Objects.requireNonNull(object)))
                            throw new ClassCastException();

                        return object;
                    });
        else
            return (T) attachmentMap.computeIfAbsent(key, (Function<? super AttachmentKey<?>, ?>) computation);
    }

    public @Nullable <T> T computeIfPresent(@Nonnull AttachmentKey<T> key,
                                            @Nonnull BiFunction<AttachmentKey<T>, T, T> computation)
    {
        if (key instanceof TypeAttributed)
            return (T) attachmentMap.computeIfPresent(key,
                    (BiFunction<? super AttachmentKey, ? super Object, ?>) (k, v) -> {
                        T object = computation.apply((AttachmentKey<T>) k, (T) v);

                        if (!((TypeAttributed) k).getType().isInstance(Objects.requireNonNull(object)))
                            throw new ClassCastException();

                        return object;
                    });
        else
            return (T) attachmentMap.computeIfPresent(key,
                    (BiFunction<? super AttachmentKey<?>, ? super Object, ?>) (k, v) -> computation);
    }

    public <T> void setAttachment(@Nonnull AttachmentKey<T> key, @Nonnull T value)
    {
        putAttachment(key, value);
    }

    private final Map<AttachmentKey<?>, Object> attachmentMap = new WeakHashMap<>();
}
