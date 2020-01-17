package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result;

import com.theredpixelteam.redtea.util.Predication;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 包含一个对象结果（可能为 null）的图节点操作器的操作结果。
 *
 * @param <T> 对象结果的类型
 */
public class ObjectManipulationResult<T> extends ManipulationResult {
    ObjectManipulationResult(boolean passed,
                             String message,
                             T object)
    {
        super(passed, message);
        this.object = object;
    }

    /**
     * 返回一个包含 null 结果的“通过”的操作结果。
     *
     * @return “通过”的操作结果
     */
    public static @Nonnull ObjectManipulationResult<?> passed()
    {
        return PASSED_WITH_NULL_OBJECT;
    }

    /**
     * 返回一个包含非 null 的对象结果的“通过”的操作结果。
     *
     * @param object 对象结果
     * @param <T> 对象结果的类型
     *
     * @return “通过”的操作结果
     *
     * @throws NullPointerException object 为 null 则抛出此错误
     */
    public static @Nonnull <T> ObjectManipulationResult<T> passed(@Nonnull T object)
    {
        return new ObjectManipulationResult<>(true, null, Predication.requireNonNull(object));
    }

    /**
     * 返回一个包含错误消息及 null 结果的“失败”的操作结果。
     *
     * @param message 错误消息
     *
     * @return “失败”的操作结果
     *
     * @throws NullPointerException message 为 null 则抛出此错误
     */
    public static @Nonnull ObjectManipulationResult<?> failed(@Nonnull String message)
    {
        return new ObjectManipulationResult<>(false, Predication.requireNonNull(message), null);
    }

    /**
     * 返回一个包含错误消息及非 null 的对象结果的“失败”的操作结果。
     *
     * @param message 错误消息
     * @param object 对象结果
     * @param <T> 对象结果的类型
     *
     * @return “失败”的操作结果
     *
     * @throws NullPointerException message、object 中包含 null 时抛出此错误
     */
    public static @Nonnull <T> ObjectManipulationResult<T> failed(@Nonnull String message,
                                                                  @Nonnull T object)
    {
        return new ObjectManipulationResult<>(false,
                Predication.requireNonNull(message, "message"),
                Predication.requireNonNull(object, "object"));
    }

    /**
     * 返回对象结果。
     *
     * @return 对象结果，可能为 null
     */
    public @Nullable T getObject()
    {
        return object;
    }

    /**
     * 返回非 null 的对象结果。
     *
     * @return 对象结果
     *
     * @throws IllegalStateException 如果此操作结果不包含非 null 的对象结果则抛出此错误
     */
    public @Nonnull T requireObject()
    {
        if (object == null)
            throw new IllegalStateException("Object not initialized");

        return object;
    }

    private static final ObjectManipulationResult<?> PASSED_WITH_NULL_OBJECT
            = new ObjectManipulationResult<>(true, null, null);

    private final T object;
}
