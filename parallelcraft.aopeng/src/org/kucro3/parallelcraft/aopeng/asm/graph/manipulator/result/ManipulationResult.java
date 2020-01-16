package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * 用于表示图节点操作器的操作结果。
 * 结果可为“通过”与“失败”，并且“失败”的结果必须包含错误消息。
 */
public class ManipulationResult {
    ManipulationResult(boolean passed,
                       String message)
    {
        this.passed = passed;
        this.message = message;
    }

    /**
     * 返回一个“通过”的操作结果。
     *
     * @return “通过”的操作结果
     */
    public static @Nonnull ManipulationResult passed()
    {
        return PASSED;
    }

    /**
     * 返回一个包含错误消息的“失败”的操作结果。
     *
     * @param message 错误消息
     * @return “失败”的操作结果
     * @throws NullPointerException 如果 message 为 null 则抛出此错误
     */
    public static @Nonnull ManipulationResult failed(@Nonnull String message)
    {
        return new ManipulationResult(false, Objects.requireNonNull(message));
    }

    /**
     * 返回此操作结果是否为“通过”。
     *
     * @return 此操作结果是否为“通过”
     */
    public boolean isPassed()
    {
        return passed;
    }

    /**
     * 返回此操作结果的错误消息。
     *
     * @return 此操作结果的错误消息，若没有则返回 null
     */
    public @Nullable String getMessage()
    {
        return message;
    }

    /**
     * 返回此操作结果的错误消息，结果一定不为 null。
     *
     * @return 此操作结果的错误消息
     * @throws IllegalStateException 如果此操作结果不包含错误消息则抛出此错误
     */
    public @Nonnull String requireMessage()
    {
        if (message == null)
            throw new IllegalStateException("Message not initialized");

        return message;
    }

    private final boolean passed;

    private final String message;

    private static final ManipulationResult PASSED = new ManipulationResult(true, null);
}
