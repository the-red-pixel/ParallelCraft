package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.util.Attachable;
import org.kucro3.parallelcraft.aopeng.util.Attachment;

import javax.annotation.Nonnull;

/**
 * 栈相关流。<br>
 * 此实例为栈相关流的容器，持有栈相关流的根节点。
 */
public class SRF implements Attachable {
    /**
     * 构造函数。
     *
     * @param root 根节点
     *
     * @throws NullPointerException 若 root 为 null 则抛出此错误
     */
    public SRF(@Nonnull SRFNode root)
    {
        this.root = Predication.requireNonNull(root);
    }

    @Override
    public @Nonnull Attachment getAttachments()
    {
        if (attachment == null)
            return attachment = new Attachment();

        return attachment;
    }

    /**
     * 返回栈相关流的根节点。
     *
     * @return 根节点
     */
    public @Nonnull SRFNode getRoot()
    {
        return root;
    }

    /**
     * 设定栈相关流的根节点。
     *
     * @param root 根节点
     *
     * @throws NullPointerException 若 root 为 null 则抛出此错误
     */
    public void setRoot(@Nonnull SRFNode root)
    {
        this.root = Predication.requireNonNull(root);
    }

    private SRFNode root;

    private Attachment attachment;
}
