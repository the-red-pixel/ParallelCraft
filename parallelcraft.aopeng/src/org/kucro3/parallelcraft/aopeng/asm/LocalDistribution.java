package org.kucro3.parallelcraft.aopeng.asm;

import org.kucro3.parallelcraft.aopeng.util.Attachment;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LocalDistribution {
    public @Nullable Local getThis()
    {
        return localThis;
    }

    public boolean hasThis()
    {
        return localThis != null;
    }

    public @Nonnull Local distributeThis()
    {
        return localThis != null ? localThis : (localThis = new LocalThis());
    }

    public @Nullable Local getParam(@Nonnegative int relativeIndex)
    {
        if (relativeIndex < localParams.size())
            return localParams.get(relativeIndex);

        return null;
    }

    public int getParamCount()
    {
        return localParams.size();
    }

    public @Nonnull Local distributeParam()
    {
        LocalParam local = new LocalParam(localParams.size());

        localParams.add(local);

        return local;
    }

    public void distributeParam(@Nonnegative int count)
    {
        for (int i = 0; i < count; i++)
            distributeParam();
    }

    public @Nonnull Local distributeLocalVar()
    {
        return new LocalAllocation();
    }

    public @Nonnegative int size()
    {
        return (hasThis() ? 1 : 0) + localParams.size() + allocatedLocal.size();
    }

    private LocalThis localThis;

    private List<LocalParam> localParams = new ArrayList<>();

    private List<LocalAllocation> allocatedLocal = new ArrayList<>();

    abstract class LocalBase implements Local
    {
        @Override
        public @Nonnull Attachment getAttachments()
        {
            return attachment;
        }

        private final Attachment attachment = new Attachment();
    }

    class LocalThis extends LocalBase
    {
        @Override
        public int allocate()
        {
            return 0;
        }

        @Override
        public boolean allocated()
        {
            return true;
        }

        @Override
        public @Nonnull LocalDistribution getDistributor()
        {
            return LocalDistribution.this;
        }

        @Override
        public int getAbsoluteIndex()
        {
            return 0;
        }

        @Override
        public int getRelativeIndex()
        {
            return 0;
        }

        @Override
        public @Nonnull Source getSource()
        {
            return Source.THIS;
        }
    }

    class LocalParam extends LocalBase
    {
        LocalParam(int relativeIndex)
        {
            this.relativeIndex = relativeIndex;
        }

        int index()
        {
            return hasThis() ? (relativeIndex + 1) : relativeIndex;
        }

        @Override
        public int allocate()
        {
            return relativeIndex;
        }

        @Override
        public boolean allocated()
        {
            return true;
        }

        @Override
        public @Nonnull LocalDistribution getDistributor()
        {
            return LocalDistribution.this;
        }

        @Override
        public int getAbsoluteIndex()
        {
            return index();
        }

        @Override
        public int getRelativeIndex()
        {
            return relativeIndex;
        }

        @Override
        public @Nonnull Source getSource()
        {
            return Source.PARAM;
        }

        private final int relativeIndex;
    }

    class LocalAllocation extends LocalBase
    {
        @Override
        public int allocate()
        {
            if (allocated)
                return relativeIndex;

            relativeIndex = allocatedLocal.size();
            allocatedLocal.add(this);

            allocated = true;

            return relativeIndex;
        }

        int index()
        {
            return (hasThis() ? 1 : 0) + localParams.size() + relativeIndex;
        }

        @Override
        public boolean allocated()
        {
            return allocated;
        }

        @Override
        public @Nonnull LocalDistribution getDistributor()
        {
            return LocalDistribution.this;
        }

        @Override
        public int getAbsoluteIndex()
        {
            if (!allocated)
                return -1;

            return index();
        }

        @Override
        public int getRelativeIndex()
        {
            if (!allocated)
                return -1;

            return relativeIndex;
        }

        @Override
        public @Nonnull Source getSource()
        {
            return Source.ALLOCATION;
        }

        private boolean allocated = false;

        private int relativeIndex;
    }
}
