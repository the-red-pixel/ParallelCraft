package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

public interface SRFGv1NodeTypes {
    public static final SRFNodeType INSTRUCTION = () -> "INSTRUCTION";

    public static final SRFNodeType STACK_BLANK = () -> "STACK_BLANK";

    public static final SRFNodeType STACK_RESTORE = () -> "STACK_RESTORE";

    public static final SRFNodeType THROWABLE_CAPTURE = () -> "THROWABLE_CAPTURE";

    public static final SRFNodeType ESCAPE = () -> "ESCAPE";
}
