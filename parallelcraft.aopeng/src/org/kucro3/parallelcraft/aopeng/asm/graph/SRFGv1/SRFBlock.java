package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.StackBlankNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.StackRestoreNode;
import org.kucro3.parallelcraft.aopeng.util.Attachable;
import org.kucro3.parallelcraft.aopeng.util.Attachment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * 栈相关流块。<br>
 * 此实例对应 SRFGv1 中的块，持有块中的所有栈相关流，并且记录一些关键信息。
 */
public class SRFBlock implements Attachable {
    /**
     * 构造函数。
     */
    public SRFBlock()
    {
        this.srfs = new ArrayList<>();
    }

    /**
     * 返回块内的所有栈相关流。
     *
     * @return 块内的所有栈相关流
     */
    public @Nonnull List<SRF> getSRFs()
    {
        return srfs;
    }

    @Override
    public @Nonnull Attachment getAttachments()
    {
        if (attachment == null)
            return attachment = new Attachment();

        return attachment;
    }

    /**
     * 返回此栈相关流块的所有异常处理记录。
     *
     * @return 此栈相关流块的所有异常处理记录
     */
    public @Nonnull ThrowableHandlers getThrowableHandlers()
    {
        return handlers;
    }

    /**
     * 返回此栈相关流块是否为空。
     *
     * @return 此栈相关流块是否为空
     */
    public boolean isEmpty()
    {
        return srfs.isEmpty();
    }

    /**
     * 返回此栈相关流块的栈复位节点。
     *
     * @return 此栈相关流块的栈复位节点，不存在则为 null
     */
    public @Nullable StackRestoreNode getStackRestoration()
    {
        return stackRestoration;
    }

    /**
     * 返回此栈相关流块是否存在栈复位节点。
     *
     * @return 此栈相关流块是否存在栈复位节点
     */
    public boolean hasStackRestoration()
    {
        return stackRestoration != null;
    }

    /**
     * 设定此栈相关流块的栈复位节点。
     *
     * @param stackRestoration 栈复位节点
     */
    public void setStackRestoration(@Nullable StackRestoreNode stackRestoration)
    {
        this.stackRestoration = stackRestoration;
    }

    /**
     * 返回此栈相关流块的栈消隐节点。
     *
     * @return 此栈相关流块的栈消隐节点，不存在则为 null
     */
    public @Nullable StackBlankNode getStackBlank()
    {
        return stackBlank;
    }

    /**
     * 返回此栈相关流块是否存在栈消隐节点。
     *
     * @return 此栈相关流块是否存在栈消隐节点
     */
    public boolean hasStackBlank()
    {
        return stackBlank != null;
    }

    /**
     * 设定此栈相关流块的栈消隐节点。
     *
     * @param stackBlank 栈消隐节点
     */
    public void setStackBlank(@Nullable StackBlankNode stackBlank)
    {
        this.stackBlank = stackBlank;
    }

    private StackRestoreNode stackRestoration;

    private StackBlankNode stackBlank;

    private final List<SRF> srfs;

    private final ThrowableHandlers handlers = new ThrowableHandlers();

    private Attachment attachment;
}