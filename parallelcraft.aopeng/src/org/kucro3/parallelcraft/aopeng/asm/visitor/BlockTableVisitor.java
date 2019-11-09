package org.kucro3.parallelcraft.aopeng.asm.visitor;

import org.kucro3.parallelcraft.aopeng.asm.BlockTable;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.LabelNode;

import javax.annotation.Nonnull;
import java.util.Objects;

public class BlockTableVisitor extends MethodVisitor {
    public BlockTableVisitor(@Nonnull BlockTable blockTable)
    {
        this(Opcodes.ASM7, null, blockTable);
    }

    public BlockTableVisitor(int api,
                             @Nonnull BlockTable blockTable)
    {
        this(api, null, blockTable);
    }

    public BlockTableVisitor(int api,
                             MethodVisitor mv,
                             @Nonnull BlockTable blockTable)
    {
        super(api, mv);
        this.blockTable = Objects.requireNonNull(blockTable);
    }

    @Override
    public void visitJumpInsn(int insn, Label label)
    {
        this.blockTable.addStartLabel((label.info instanceof LabelNode) ? ((LabelNode) label.info).getLabel() : label);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type)
    {
        this.blockTable.addTryCatchRecord(new BlockTable.TryCatchRecord(start, end, handler, type));
    }

    public @Nonnull BlockTable getBlockTable()
    {
        return blockTable;
    }

    protected final BlockTable blockTable;
}
