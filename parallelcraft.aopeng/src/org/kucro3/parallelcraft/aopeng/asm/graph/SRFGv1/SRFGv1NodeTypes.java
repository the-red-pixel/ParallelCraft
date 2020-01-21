package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

/**
 * SRFG-v1 图节点类型集合。
 */
public interface SRFGv1NodeTypes {
    /**
     * 指令节点类型
     */
    public static final SRFNodeType INSTRUCTION = () -> "INSTRUCTION";

    /**
     * 栈消隐节点类型
     */
    public static final SRFNodeType STACK_BLANK = () -> "STACK_BLANK";

    /**
     * 栈复位节点类型
     */
    public static final SRFNodeType STACK_RESTORE = () -> "STACK_RESTORE";

    /**
     * 异常捕获节点类型
     */
    public static final SRFNodeType THROWABLE_CAPTURE = () -> "THROWABLE_CAPTURE";

    /**
     * 逃逸节点类型
     */
    public static final SRFNodeType ESCAPE = () -> "ESCAPE";
}
