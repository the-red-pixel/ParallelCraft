package org.kucro3.parallelcraft.aopeng.asm.visitor;

import org.kucro3.parallelcraft.aopeng.asm.DifferentialBlockTable;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import javax.annotation.Nonnull;

public class DifferentialBlockTableVisitor extends MethodVisitor {
    public DifferentialBlockTableVisitor()
    {
        super(Opcodes.ASM6);
    }

    public void visitLabel(Label label)
    {
        builder.next(label);
    }

    public void visitJumpInsn(int insn, Label target)
    {
        builder.recordJumpTarget(target);
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type)
    {
        builder.recordTryCatch(start, end, handler, type);
    }

    public @Nonnull DifferentialBlockTable build()
    {
        return builder.build();
    }

    private final DifferentialBlockTable.Builder builder = DifferentialBlockTable.builder();
}
