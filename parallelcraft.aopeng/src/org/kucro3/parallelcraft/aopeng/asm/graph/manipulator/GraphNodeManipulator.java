package org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.IntManipulationResult;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result.ManipulationResult;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * 图节点操作器。对于图节点及路径的增删改都由此实例管理并处理。
 *
 * @param <T> 节点类型
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface GraphNodeManipulator<T extends Node<T>> {
    /**
     * 返回此节点操作器对于上路径的数量是否有限制。
     *
     * @return 对于上路径的数量是否有限制
     */
    public default boolean hasUpperLimit()
    {
        return false;
    }

    /**
     * 返回此节点操作器的上路径最大数量限制。
     *
     * @return 若有限制则返回限制的上路径最大数量，否则总返回 -1。
     */
    public default int getUpperLimit()
    {
        return -1;
    }

    /**
     * 返回包含所有已连接的上路径的列表。
     *
     * @return 包含所有已连接上路径的列表，此列表是不可修改的
     */
    public @Nonnull List<Path<T>> getUpperPaths();

    /**
     * 返回指定序号的上路径。
     *
     * @param index 指定序号
     *
     * @return 指定序号的上路径
     *
     * @throws IndexOutOfBoundsException 如果不存在指定序号的上路径则会抛出此错误
     */
    public default @Nonnull Path<T> getUpperPath(int index)
    {
        return getUpperPaths().get(index);
    }

    /**
     * 返回已连接的序号最小（即 0）的上路径。
     *
     * @return 序号最小的上路径
     *
     * @throws IndexOutOfBoundsException 如果不存在任何已连接的上路径则会抛出此错误
     */
    public default @Nonnull Path<T> peekUpperPath()
    {
        return getUpperPath(0);
    }

    /**
     * 返回已连接的序号最小（即 0）的上节点。
     *
     * @return 序号最小的上节点
     *
     * @throws IndexOutOfBoundsException 如果不存在任何已连接的上节点则会抛出此错误
     */
    public default @Nonnull T peekUpperNode()
    {
        return peekUpperPath().getUpperNode();
    }

    /**
     * 返回已连接的上路径的数量。
     *
     * @return 已连接的上路径的数量
     */
    public default int getUpperPathCount()
    {
        return getUpperPaths().size();
    }

    /**
     * 返回是否存在已连接的上路径。
     *
     * @return 是否存在已连接的上路径
     */
    public default boolean hasUpperPath()
    {
        return getUpperPathCount() > 0;
    }

    /**
     * 返回此节点操作器对于下路径的数量是否有限制。
     *
     * @return 对于下路径的数量是否有限制
     */
    public default boolean hasLowerLimit()
    {
        return false;
    }

    /**
     * 返回此节点操作器的最大下路径数量限制。
     *
     * @return 若有限制则返回限制的下路径最大数量，否则总返回 -1。
     */
    public default int getLowerLimit()
    {
        return -1;
    }

    /**
     * 返回包含所有已连接的下路径的列表。
     *
     * @return 包含所有已连接下路径的列表，此列表是不可修改的
     */
    public @Nonnull List<Path<T>> getLowerPaths();

    /**
     * 返回指定序号的下路径。
     *
     * @param index 指定序号
     *
     * @return 指定序号的下路径
     *
     * @throws IndexOutOfBoundsException 如果不存在指定序号的下路径则抛出此错误
     */
    public default @Nonnull Path<T> getLowerPath(int index)
    {
        return getLowerPaths().get(index);
    }

    /**
     * 返回已连接的序号最小（即 0）的下路径。
     *
     * @return 序号最小的下路径
     *
     * @throws IndexOutOfBoundsException 如果不存在已连接的下路径则抛出此错误
     */
    public default @Nonnull Path<T> peekLowerPath()
    {
        return getLowerPath(0);
    }

    /**
     * 返回已连接的序号最小（即 0）的下节点。
     *
     * @return 序号最小的下节点
     *
     * @throws IndexOutOfBoundsException 如果不存在已连接的下节点则抛出此错误
     */
    public default @Nonnull T peekLowerNode()
    {
        return peekLowerPath().getLowerNode();
    }

    /**
     * 返回已连接的下路径的数量。
     *
     * @return 已连接的下路径的数量
     */
    public default int getLowerPathCount()
    {
        return getLowerPaths().size();
    }

    /**
     * 返回是否存在已连接的下路径。
     *
     * @return 是否存在已连接的下路径
     */
    public default boolean hasLowerPath()
    {
        return getLowerPathCount() > 0;
    }

    /**
     * 将此节点操作器管理的节点从图中移除。<br>
     * 只有在上路径与下路径的数量相同时此操作才可执行。
     * 若执行，此操作会更改已连接的路径对象内记录的内容。
     *
     * @return 操作结果
     *
     * @throws IllegalStateException 若在修改路径连接关系时出错则会抛出此错误
     */
    public @Nonnull ManipulationResult unlink();

    /**
     * 将指定序号的上路径设置为指定路径。<br>
     * 此操作不会对指定路径对象 path 内记录的内容做任何更改，
     * 对路径对象 path 内记录的内容的相应修改操作应在此方法以外完成。
     * 如果指定序号正好为已连接上路径的数量，则会尝试向尾部加入一个路径。
     *
     * @param ordinal 指定序号
     * @param path 指定上路径
     *
     * @return 操作结果
     *
     * @throws IndexOutOfBoundsException 如果指定序号大于已连接上路径的数量则会抛出此错误
     * @throws IllegalArgumentException 如果指定序号小于 0 则会抛出此错误
     * @throws NullPointerException 如果 path 为 null 则抛出此错误
     */
    public @Nonnull ManipulationResult setUpperPath(@Nonnegative int ordinal, @Nonnull Path<T> path);

    /**
     * 将指定上路径放入序号为 0 的位置，所有之前已经连接的路径的序号都会加 1。<br>
     * 此操作会更改已连接的上路径对象内记录的序号，但不会更改指定上路径对象 path 内记录的内容，
     * 对上路径对象 path 内记录的内容的相应修改操作应在此方法以外完成。
     *
     * @param path 指定上路径
     *
     * @return 操作结果
     *
     * @throws NullPointerException 如果 path 为 null 则抛出此错误
     */
    // add the path to the head
    public @Nonnull ManipulationResult pushUpperPath(@Nonnull Path<T> path);

    /**
     * 将序号最小（即 0）的上路径移除，移除后所有之前已经连接的上路径的序号都会减 1。<br>
     * 此操作会更改已连接的上路径对象内记录的序号，但不会更改被移除的上路径内记录的内容。
     *
     * @return 被移除的上路径
     * @throws java.util.NoSuchElementException 如果不存在任何已连接的上路径则抛出此错误
     */
    // remove the first path
    public @Nonnull Path<T> popUpperPath();

    /**
     * 将指定上路径放入序号最大的位置。<br>
     * 此操作不会更改指定上路径对象 path 内记录的内容，
     * 对上路径对象 path 内记录的内容的相应修改操作应在此方法以外完成。
     *
     * @param path 指定上路径
     *
     * @return 操作结果，操作成功时结果内数值为指定上路径对象 path 加入后的序号，否则为 -1
     *
     * @throws NullPointerException 如果 path 为 null 则抛出此错误
     */
    // add the path to the tail
    public @Nonnull IntManipulationResult appendUpperPath(@Nonnull Path<T> path);

    /**
     * 将序号最大的上路径移除。<br>
     * 此操作不会更改被移除的上路径内记录的内容。
     *
     * @return 被移除的上路径
     *
     * @throws java.util.NoSuchElementException 如果不存在任何已连接的上路径则抛出此错误
     */
    // remove the last path
    public @Nonnull Path<T> truncateUpperPath();

    /**
     * 清除所有的上路径。<br>
     * 此操作不会更改任何一个被清除的上路径内记录的内容。
     */
    public void clearUpperPaths();

    /**
     * 将指定序号的下路径设置为指定路径。<br>
     * 此操作不会对指定路径对象 path 内记录的内容做任何更改，
     * 对路径对象 path 内记录的内容的相应修改操作应在此方法以外完成。
     * 如果指定序号正好为已连接下路径的数量，则会尝试向尾部加入一个路径。
     *
     * @param ordinal 指定序号
     * @param path 指定下路径
     *
     * @return 操作结果
     *
     * @throws IndexOutOfBoundsException 如果指定序号大于已连接下路径的数量则会抛出此错误
     * @throws IllegalArgumentException 如果指定序号小于 0 则会抛出此错误
     * @throws NullPointerException 如果 path 为 null 则抛出此错误
     */
    public @Nonnull ManipulationResult setLowerPath(@Nonnegative int ordinal, @Nonnull Path<T> path);

    /**
     * 将指定下路径放入序号为 0 的位置，所有之前已经连接的路径的序号都会加 1。<br>
     * 此操作会更改已连接的下路径对象内记录的序号，但不会更改指定下路径对象 path 内记录的内容，
     * 对下路径对象 path 内记录的内容的相应修改操作应在此方法以外完成。
     *
     * @param path 指定下路径
     *
     * @return 操作结果
     *
     * @throws NullPointerException 如果 path 为 null 则抛出此错误
     */
    // add the path to the head
    public @Nonnull ManipulationResult pushLowerPath(@Nonnull Path<T> path);

    /**
     * 将序号最小（即 0）的下路径移除，移除后所有之前已经连接的下路径的序号都会减 1。<br>
     * 此操作会更改已连接的下路径对象内记录的序号，但不会更改被移除的下路径内记录的内容。
     *
     * @return 被移除的下路径
     *
     * @throws java.util.NoSuchElementException 如果不存在任何已连接的下路径则抛出此错误
     */
    // remove the first path
    public @Nonnull Path<T> popLowerPath();

    /**
     * 将指定下路径放入序号最大的位置。<br>
     * 此操作不会更改指定下路径对象 path 内记录的内容，
     * 对下路径对象 path 内记录的内容的相应修改操作应在此方法以外完成。
     *
     * @param path 指定下路径
     *
     * @return 操作结果，操作成功时结果内数值为指定下路径对象 path 加入后的序号，否则为 -1
     *
     * @throws NullPointerException 如果 path 为 null 则抛出此错误
     */
    // add the path to the tail
    public @Nonnull IntManipulationResult appendLowerPath(@Nonnull Path<T> path);

    /**
     * 将序号最大的下路径移除。<br>
     * 此操作不会更改被移除的下路径内记录的内容。
     *
     * @return 被移除的下路径
     *
     * @throws java.util.NoSuchElementException 如果不存在任何已连接的下路径则抛出此错误
     */
    // remove the last path
    public @Nonnull Path<T> truncateLowerPath();

    /**
     * 清除所有的下路径。<br>
     * 此操作不会更改任何一个被清除的下路径内记录的内容。
     */
    public void clearLowerPaths();
}
