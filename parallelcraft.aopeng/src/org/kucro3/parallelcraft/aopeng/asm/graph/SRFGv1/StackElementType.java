package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.StackComputation.Operator.*;

public enum StackElementType implements StackComputation.Computational {
    BYTE        ("B",   B,  1),
    CHAR        ("C",   C,  1),
    DOUBLE      ("D",   D,  2),
    FLOAT       ("F",   F,  1),
    INT         ("I",   I,  1),
    LONG        ("J",   J,  2),
    REFERENCE   ("L;",  L,  1),
    SHORT       ("S",   S,  1),
    BOOLEAN     ("Z",   Z,  1),
    ARRAY       ("[;",  AR, 1);

    private StackElementType(String tag, StackComputation.Operator operator, int category)
    {
        this.tag = tag;
        this.operator = operator;
        this.category = category;

        reg(this);
    }

    public @Nonnull
    String getTag()
    {
        return tag;
    }

    @Override
    public @Nonnull String toString()
    {
        return tag;
    }

    @Override
    public @Nonnull StackComputation.Operator toOperator()
    {
        return operator;
    }

    public int getCategory()
    {
        return category;
    }

    public static @Nullable
    StackElementType from(@Nonnull StackComputation.Operator operator)
    {
        if (operator.ordinal() < OP2T.length)
            return OP2T[operator.ordinal()];

        return null;
    }

    public static @Nullable StackElementType from(@Nonnull String descriptor)
    {
        switch (descriptor.charAt(0))
        {
            case 'B': return BYTE;
            case 'C': return CHAR;
            case 'D': return DOUBLE;
            case 'F': return FLOAT;
            case 'I': return INT;
            case 'J': return LONG;
            case 'L': return REFERENCE;
            case 'S': return SHORT;
            case 'Z': return BOOLEAN;
            case '[': return ARRAY;

            default:
                return null;
        }
    }

    static void reg(StackElementType type)
    {
        if (OP2T == null)
            OP2T = new StackElementType[10];

        OP2T[type.toOperator().ordinal()] = type;
    }

    private final int category;

    private final String tag;

    private final StackComputation.Operator operator;

    private static StackElementType[] OP2T;
}
