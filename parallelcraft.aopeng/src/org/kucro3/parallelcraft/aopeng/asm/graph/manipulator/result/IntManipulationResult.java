package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result;

import com.theredpixelteam.redtea.util.Predication;

import javax.annotation.Nonnull;

/**
 * 包含一个整数结果的图节点操作器的操作结果。<br>
 * 此类对于“通过”的操作结果进行了缓存操作，以达到更好的性能。<br>
 * 用户可以通过修改 {@link #PROPERTY_CACHE_UPPER_BOUND} 与 {@link #PROPERTY_CACHE_LOWER_BOUND} 中
 * 所表示的两个系统变量来调整缓存区的大小。<br>
 * 缓存区的上界与下界默认为 {@link #DEFAULT_CACHE_UPPER_BOUND} 与
 * {@link #DEFAULT_CACHE_LOWER_BOUND} 中所表示的值。<br>
 * <br>
 * 注：缓存区空间包含上界与下界，即其空间可表示为此集合： [LOWER_BOUND, UPPER_BOUND]
 */
public class IntManipulationResult extends ManipulationResult {
    IntManipulationResult(boolean passed,
                          String message,
                          int value)
    {
        super(passed, message);
        this.value = value;
    }

    /**
     * 返回一个包含整数结果的“通过”的操作结果。
     *
     * @param value 整数结果
     *
     * @return “通过”的操作结果
     */
    public static @Nonnull IntManipulationResult passed(int value)
    {
        IntManipulationResult object = acquireCache(value);

        if (object == null)
            object = new IntManipulationResult(true, null, value);

        return object;
    }

    /**
     * 返回一个包含错误消息与整数结果的“失败”的操作结果。
     *
     * @param message 错误消息
     * @param value 整数结果
     *
     * @return “失败”的操作结果
     *
     * @throws NullPointerException 如果 message 为 null 则抛出此错误
     */
    public static @Nonnull IntManipulationResult failed(@Nonnull String message,
                                                        int value)
    {
        return new IntManipulationResult(false, Predication.requireNonNull(message), value);
    }

    /**
     * 返回整数结果。
     *
     * @return 整数结果
     */
    public int getValue()
    {
        return value;
    }

    static IntManipulationResult acquireCache(int value)
    {
        int index = value + CACHE_DELTA;

        if (index < 0)
            return null;

        if (index < CACHE.length)
            return CACHE[index];

        return null;
    }

    private final int value;

    private static final IntManipulationResult[] CACHE;

    private static final int CACHE_DELTA;

    /**
     * 缓存区下界的系统变量名称
     */
    public static final String PROPERTY_CACHE_LOWER_BOUND = "trslice.graph.intresult.cache.lowerbound";

    /**
     * 缓存区上界的系统变量名称
     */
    public static final String PROPERTY_CACHE_UPPER_BOUND = "trslice.graph.intresult.cache.upperbound";

    /**
     * 默认的缓存区下界值
     */
    public static final int DEFAULT_CACHE_LOWER_BOUND = -1;

    /**
     * 默认的缓存区上界值
     */
    public static final int DEFAULT_CACHE_UPPER_BOUND = 16;

    static {
        int cacheLowerBound;
        int cacheUpperBound;

        try {
            cacheLowerBound = Integer.parseInt(System.getProperty(PROPERTY_CACHE_LOWER_BOUND));
        } catch (NumberFormatException e) {
            cacheLowerBound = DEFAULT_CACHE_LOWER_BOUND;
        }

        try {
            cacheUpperBound = Integer.parseInt(System.getProperty(PROPERTY_CACHE_UPPER_BOUND));
        } catch (NumberFormatException e) {
            cacheUpperBound = DEFAULT_CACHE_UPPER_BOUND;
        }

        CACHE_DELTA = -cacheLowerBound;

        int cacheSize = cacheUpperBound - cacheLowerBound + 1;

        CACHE = new IntManipulationResult[cacheSize];

        for (int i = 0; i < cacheSize; i++)
            CACHE[i] = new IntManipulationResult(true, null, cacheLowerBound + i);
    }
}
