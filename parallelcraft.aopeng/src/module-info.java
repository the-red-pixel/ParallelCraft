module parallelcraft.aopeng {
    requires jsr305;
    requires redtea;
    requires org.objectweb.asm;
    requires org.objectweb.asm.tree;

    exports org.kucro3.parallelcraft.aopeng;
    exports org.kucro3.parallelcraft.aopeng.asm;
    exports org.kucro3.parallelcraft.aopeng.asm.graph;
    exports org.kucro3.parallelcraft.aopeng.asm.graph.manipulator;
    exports org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.result;
    exports org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;
    exports org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node;
    exports org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;
    exports org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1X;
    exports org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1X.node;
    exports org.kucro3.parallelcraft.aopeng.asm.graph.util;
    exports org.kucro3.parallelcraft.aopeng.asm.visitor;
    exports org.kucro3.parallelcraft.aopeng.util;
}