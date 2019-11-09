package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import com.theredpixelteam.redtea.util.Pair;
import org.objectweb.asm.Opcodes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.StackComputation.Operator.*;

public final class StackComputation implements Opcodes {
    private StackComputation()
    {
    }

    // null if opcode not supported
    public static @Nullable Pair<Operator[], Operator[]> get(int opcode)
    {
        if (opcode < 0 || opcode > 255)
            return null;

        return INSNS[opcode];
    }

    // Pair:
    //  first:  consuming operator
    //  second: producing operator
    private static final Pair<Operator[], Operator[]>[] INSNS;

    private static Operator[] o(Operator... operators)
    {
        return operators;
    }

    private static Pair<Operator[], Operator[]> p(Operator[] a, Operator[] b)
    {
        return Pair.of(a, b);
    }

    static {
        @SuppressWarnings("unchecked")
        Pair<Operator[], Operator[]>[] insns =
                (Pair<Operator[], Operator[]>[]) new Pair[256];

        //                        consuming                 producing
        //                          <-- top
        insns[NOP]              = p(o(),                    o());

        insns[ACONST_NULL]      = p(o(),                    o(L));
        insns[ICONST_M1]        = p(o(),                    o(I));
        insns[ICONST_0]         = p(o(),                    o(I));
        insns[ICONST_1]         = p(o(),                    o(I));
        insns[ICONST_2]         = p(o(),                    o(I));
        insns[ICONST_3]         = p(o(),                    o(I));
        insns[ICONST_4]         = p(o(),                    o(I));
        insns[ICONST_5]         = p(o(),                    o(I));
        insns[LCONST_0]         = p(o(),                    o(J));
        insns[LCONST_1]         = p(o(),                    o(J));
        insns[FCONST_0]         = p(o(),                    o(F));
        insns[FCONST_1]         = p(o(),                    o(F));
        insns[FCONST_2]         = p(o(),                    o(F));
        insns[DCONST_0]         = p(o(),                    o(D));
        insns[DCONST_1]         = p(o(),                    o(D));
        insns[BIPUSH]           = p(o(),                    o(B));
        insns[SIPUSH]           = p(o(I),                   o());
        insns[LDC]              = p(o(),                    o(PR1));

        insns[ILOAD]            = p(o(),                    o(I));
        insns[LLOAD]            = p(o(),                    o(J));
        insns[FLOAD]            = p(o(),                    o(F));
        insns[DLOAD]            = p(o(),                    o(D));
        insns[ALOAD]            = p(o(),                    o(L));

        insns[IALOAD]           = p(o(I,   AR),             o(I));
        insns[LALOAD]           = p(o(I,   AR),             o(J));
        insns[FALOAD]           = p(o(I,   AR),             o(F));
        insns[DALOAD]           = p(o(I,   AR),             o(D));
        insns[AALOAD]           = p(o(I,   AR),             o(L));
        insns[BALOAD]           = p(o(I,   AR),             o(B));
        insns[CALOAD]           = p(o(I,   AR),             o(C));
        insns[SALOAD]           = p(o(I,   AR),             o(I));

        insns[ISTORE]           = p(o(I),                   o());
        insns[LSTORE]           = p(o(J),                   o());
        insns[FSTORE]           = p(o(F),                   o());
        insns[DSTORE]           = p(o(D),                   o());
        insns[ASTORE]           = p(o(L),                   o());

        insns[IASTORE]          = p(o(I,   I,   AR),        o());
        insns[LASTORE]          = p(o(J,   I,   AR),        o());
        insns[FASTORE]          = p(o(F,   I,   AR),        o());
        insns[DASTORE]          = p(o(D,   I,   AR),        o());
        insns[AASTORE]          = p(o(L,   I,   AR),        o());
        insns[BASTORE]          = p(o(B,   I,   AR),        o());
        insns[CASTORE]          = p(o(C,   I,   AR),        o());
        insns[SASTORE]          = p(o(S,   I,   AR),        o());

        insns[POP]              = p(o(SX1),                 o());
        insns[POP2]             = p(o(SX2),                 o());
        insns[DUP]              = p(o(SX1),                 o(X0,  X0));
        insns[DUP_X1]           = p(o(SX1, SX1),            o(X0,  X1,  X0));
        insns[DUP_X2]           = p(o(SX1, SX2),            o(X0,  X1,  X0));
        insns[DUP2]             = p(o(SX2),                 o(X0,  X0));
        insns[DUP2_X1]          = p(o(SX2, SX1),            o(X0,  X1,  X0));
        insns[DUP2_X2]          = p(o(SX2, SX2),            o(X0,  X1,  X0));
        insns[SWAP]             = p(o(SX1, SX1),            o(X1,  X0));

        insns[IADD]             = p(o(I,   I),              o(I));
        insns[LADD]             = p(o(J,   J),              o(J));
        insns[FADD]             = p(o(F,   F),              o(F));
        insns[DADD]             = p(o(D,   D),              o(D));
        insns[ISUB]             = p(o(I,   I),              o(I));
        insns[LSUB]             = p(o(J,   J),              o(J));
        insns[FSUB]             = p(o(F,   F),              o(F));
        insns[DSUB]             = p(o(D,   D),              o(D));
        insns[IMUL]             = p(o(I,   I),              o(I));
        insns[LMUL]             = p(o(J,   J),              o(J));
        insns[FMUL]             = p(o(F,   F),              o(F));
        insns[DMUL]             = p(o(D,   D),              o(D));
        insns[IDIV]             = p(o(I,   I),              o(I));
        insns[LDIV]             = p(o(J,   J),              o(J));
        insns[FDIV]             = p(o(F,   F),              o(F));
        insns[DDIV]             = p(o(D,   D),              o(D));
        insns[IREM]             = p(o(I,   I),              o(I));
        insns[LREM]             = p(o(J,   J),              o(J));
        insns[FREM]             = p(o(F,   F),              o(F));
        insns[DREM]             = p(o(D,   D),              o(D));
        insns[INEG]             = p(o(I),                   o(I));
        insns[LNEG]             = p(o(J),                   o(J));
        insns[FNEG]             = p(o(F),                   o(F));
        insns[DNEG]             = p(o(D),                   o(D));
        insns[ISHL]             = p(o(I,   I),              o(I));
        insns[LSHL]             = p(o(I,   J),              o(J));
        insns[ISHR]             = p(o(I,   I),              o(I));
        insns[LSHR]             = p(o(I,   J),              o(J));
        insns[IUSHR]            = p(o(I,   I),              o(I));
        insns[LUSHR]            = p(o(I,   J),              o(J));
        insns[IAND]             = p(o(I,   I),              o(I));
        insns[LAND]             = p(o(J,   J),              o(J));
        insns[IOR]              = p(o(I,   I),              o(I));
        insns[LOR]              = p(o(J,   J),              o(J));
        insns[IXOR]             = p(o(I,   I),              o(I));
        insns[LXOR]             = p(o(J,   J),              o(J));

        insns[IINC]             = p(o(),                    o());

        insns[I2L]              = p(o(I),                   o(J));
        insns[I2F]              = p(o(I),                   o(F));
        insns[I2D]              = p(o(I),                   o(D));
        insns[L2I]              = p(o(J),                   o(I));
        insns[L2F]              = p(o(J),                   o(F));
        insns[L2D]              = p(o(J),                   o(D));
        insns[F2I]              = p(o(F),                   o(I));
        insns[F2L]              = p(o(F),                   o(J));
        insns[F2D]              = p(o(F),                   o(D));
        insns[D2I]              = p(o(D),                   o(I));
        insns[D2L]              = p(o(D),                   o(J));
        insns[D2F]              = p(o(D),                   o(F));
        insns[I2B]              = p(o(I),                   o(B));
        insns[I2C]              = p(o(I),                   o(C));
        insns[I2S]              = p(o(I),                   o(S));

        insns[LCMP]             = p(o(J,   J),              o(I));
        insns[FCMPL]            = p(o(F,   F),              o(I));
        insns[FCMPG]            = p(o(F,   F),              o(I));
        insns[DCMPL]            = p(o(D,   D),              o(I));
        insns[DCMPG]            = p(o(D,   D),              o(I));

        insns[IFEQ]             = p(o(I),                   o());
        insns[IFNE]             = p(o(I),                   o());
        insns[IFLT]             = p(o(I),                   o());
        insns[IFGE]             = p(o(I),                   o());
        insns[IFGT]             = p(o(I),                   o());
        insns[IFLE]             = p(o(I),                   o());

        insns[IF_ICMPEQ]        = p(o(I,   I),              o());
        insns[IF_ICMPNE]        = p(o(I,   I),              o());
        insns[IF_ICMPLT]        = p(o(I,   I),              o());
        insns[IF_ICMPGE]        = p(o(I,   I),              o());
        insns[IF_ICMPGT]        = p(o(I,   I),              o());
        insns[IF_ICMPLE]        = p(o(I,   I),              o());
        insns[IF_ACMPEQ]        = p(o(L,   L),              o());
        insns[IF_ACMPNE]        = p(o(L,   L),              o());

        // *Note: JSR and RET instruction is no longer supported in JVM 7+

        insns[GOTO]             = p(o(),                    o());
//      insns[JSR]              = p(o(),                    o(ADR));
//      insns[RET]              = p(o(),                    o());

        insns[TABLESWITCH]      = p(o(I),                   o());
        insns[LOOKUPSWITCH]     = p(o(I),                   o());

        insns[IRETURN]          = p(o(I),                   o());
        insns[LRETURN]          = p(o(J),                   o());
        insns[FRETURN]          = p(o(F),                   o());
        insns[DRETURN]          = p(o(D),                   o());
        insns[ARETURN]          = p(o(L),                   o());
        insns[RETURN]           = p(o(),                    o());

        insns[GETSTATIC]        = p(o(),                    o(PR1));
        insns[PUTSTATIC]        = p(o(PR1),                 o());
        insns[GETFIELD]         = p(o(L),                   o(PR1));
        insns[PUTFIELD]         = p(o(PR1, L),              o());

        insns[INVOKEVIRTUAL]    = p(o(L,   VPR),            o(PR1));
        insns[INVOKESPECIAL]    = p(o(L,   VPR),            o(PR1));
        insns[INVOKESTATIC]     = p(o(VPR),                 o(PR1));
        insns[INVOKEINTERFACE]  = p(o(L,   VPR),            o(PR1));
        insns[INVOKEDYNAMIC]    = p(o(VPR),                 o(PR1));

        insns[NEW]              = p(o(),                    o(L));
        insns[NEWARRAY]         = p(o(I),                   o(AR));
        insns[ANEWARRAY]        = p(o(I),                   o(AR));
        insns[ARRAYLENGTH]      = p(o(AR),                  o(I));
        insns[ATHROW]           = p(o(L),                   o());
        insns[CHECKCAST]        = p(o(L),                   o(L));
        insns[INSTANCEOF]       = p(o(L),                   o(I));
        insns[MONITORENTER]     = p(o(L),                   o());
        insns[MONITOREXIT]      = p(o(L),                   o());

        insns[IFNONNULL]        = p(o(L),                   o());
        insns[IFNULL]           = p(o(L),                   o());

        insns[MULTIANEWARRAY]   = p(o(VPR),                 o(AR));

        INSNS = insns;
    }

    public enum Operator implements Computational
    {
        B,  // byte
        C,  // char
        D,  // double
        F,  // float
        I,  // int
        J,  // long
        L,  // object
        S,  // short
        Z,  // boolean
        AR, // array
//      ADR,// jump address, generated by JSR instruction
        X0, // stack <- [0]
        X1, // stack <- [1]
        SX1,// 1 stack slot
        SX2,// 2 stack slot
        PR1,// param depending, containing 1 element(s)
        VPR;// param depending, variable

        @Override
        public @Nonnull Operator toOperator()
        {
            return this;
        }
    }

    public interface Computational
    {
        public @Nonnull Operator toOperator();
    }
}
