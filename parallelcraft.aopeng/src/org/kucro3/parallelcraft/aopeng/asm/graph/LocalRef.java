package org.kucro3.parallelcraft.aopeng.asm.graph;

import com.theredpixelteam.redtea.util.Predication;
import org.kucro3.parallelcraft.aopeng.asm.Local;

import javax.annotation.Nonnull;

public class LocalRef {
    public LocalRef(@Nonnull Local local)
    {
        this.local = Predication.requireNonNull(local);
    }

    public void setLocal(@Nonnull Local local)
    {
        this.local = Predication.requireNonNull(local);
    }

    public @Nonnull Local getLocal()
    {
        return local;
    }

    private Local local;
}
