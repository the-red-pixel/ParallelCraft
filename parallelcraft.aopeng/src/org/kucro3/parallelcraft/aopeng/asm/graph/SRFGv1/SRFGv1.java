package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.graph.DifferentialVisitMeta;

import javax.annotation.Nonnull;

/**
 * SRFG-v1 图对象。<br>
 *
 * 此实例持有一个 SRFG-v1 图的根节点以及节点访问信息，用来表示单个 SRFG-v1 图。
 */
public class SRFGv1 {
    /**
     * 构造函数。
     *
     * @param root 图根节点
     *
     * @throws NullPointerException 若 root 为 null 则抛出此错误
     */
    public SRFGv1(@Nonnull SRFBlockNode root)
    {
        this.root = Predication.requireNonNull(root);
        this.visitMeta = new DifferentialVisitMeta();
    }

    /**
     * 构造函数。
     *
     * @param root 图根节点
     * @param visitMeta 节点访问信息
     *
     * @throws NullPointerException 若 root、visitMeta 中存在 null 则抛出此错误
     */
    public SRFGv1(@Nonnull SRFBlockNode root,
                  @Nonnull DifferentialVisitMeta visitMeta)
    {
        this.root = Predication.requireNonNull(root, "root");
        this.visitMeta = Predication.requireNonNull(visitMeta, "visitMeta");
    }

    /**
     * 返回图根节点。
     *
     * @return 图根节点
     */
    public @Nonnull SRFBlockNode getRoot()
    {
        return root;
    }

    /**
     * 返回节点访问信息。
     *
     * @return 节点访问信息
     */
    public @Nonnull DifferentialVisitMeta getVisitMeta()
    {
        return visitMeta;
    }

    /**
     * 设定节点访问信息。
     *
     * @param visitMeta 节点访问信息
     *
     * @throws NullPointerException 若 visitMeta 为 null 则抛出此错误
     */
    public void setVisitMeta(@Nonnull DifferentialVisitMeta visitMeta)
    {
        this.visitMeta = Predication.requireNonNull(visitMeta);
    }

    /**
     * 将节点访问信息自增。
     */
    public void nextVisitMeta()
    {
        this.visitMeta = this.visitMeta.next();
    }

    private final SRFBlockNode root;

    private DifferentialVisitMeta visitMeta;
}
