package org.kucro3.parallelcraft.aopeng.asm.graph;

import org.kucro3.parallelcraft.aopeng.asm.Local;

import javax.annotation.Nonnull;
import java.util.Objects;

public class LocalRef {
    public LocalRef(@Nonnull Local local)
    {
        this.local = Objects.requireNonNull(local);
    }

    public void setLocal(@Nonnull Local local)
    {
        this.local = Objects.requireNonNull(local);
    }

    public @Nonnull Local getLocal()
    {
        return local;
    }

    private Local local;
}
