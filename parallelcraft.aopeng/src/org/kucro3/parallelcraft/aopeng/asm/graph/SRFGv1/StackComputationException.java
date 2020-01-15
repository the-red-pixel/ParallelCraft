package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import org.kucro3.parallelcraft.aopeng.util.Attachable;
import org.kucro3.parallelcraft.aopeng.util.Attachment;
import org.kucro3.parallelcraft.aopeng.util.AttachmentKey;
import org.kucro3.parallelcraft.aopeng.util.TypeAttributedAttachmentKey;

import javax.annotation.Nonnull;
import java.util.Objects;

public class StackComputationException extends RuntimeException implements Attachable {
    public StackComputationException(@Nonnull Cause cause)
    {
        this.cause = Objects.requireNonNull(cause, "cause");
    }

    public static @Nonnull StackComputationException underflow()
    {
        return new StackComputationException(new Cause.Underflow());
    }

    public static @Nonnull StackComputationException typeIncompatibility(@Nonnull StackElementType expected,
                                                                         @Nonnull StackElementType found)
    {
        return new StackComputationException(new Cause.TypeIncompatibility(expected, found));
    }

    public static @Nonnull StackComputationException categoryIncompatibility(int expected,
                                                                             int found)
    {
        return new StackComputationException(new Cause.CategoryIncompatibility(expected, found));
    }

    public @Nonnull Cause cause()
    {
        return cause;
    }

    // default implementation
    public @Nonnull String message()
    {
        return cause.toString();
    }

    @Override
    public @Nonnull String getMessage()
    {
        return message();
    }

    private final Cause cause;

    private final Attachment attachment = new Attachment();

    public static final AttachmentKey<SRFNode> ATTACHMENT_SOURCE_SRFG_NODE
            = TypeAttributedAttachmentKey.create(SRFNode.class);

    @Override
    public @Nonnull Attachment getAttachments()
    {
        return attachment;
    }

    public static interface CauseCategory
    {
        public String name();
    }

    public static final class CauseCategories
    {
        private CauseCategories()
        {
        }

        public static final CauseCategory STACK_UNDERFLOW = () -> "STACK_UNDERFLOW";

        public static final CauseCategory STACK_TYPE_INCOMPATIBILITY = () -> "STACK_TYPE_INCOMPATIBILITY";

        public static final CauseCategory STACK_CATEGORY_INCOMPATIBILITY = () -> "STACK_CATEGORY_INCOMPATIBILITY";
    }

    public static interface Cause
    {
        public @Nonnull CauseCategory getCategory();

        public static class Underflow implements Cause
        {
            @Override
            public @Nonnull CauseCategory getCategory()
            {
                return CauseCategories.STACK_UNDERFLOW;
            }

            @Override
            public @Nonnull String toString()
            {
                return "Stack underflow";
            }
        }

        public static class TypeIncompatibility implements Cause
        {
            public TypeIncompatibility(@Nonnull StackElementType expected,
                                        @Nonnull StackElementType found)
            {
                this.expected = Objects.requireNonNull(expected, "expected");
                this.found = Objects.requireNonNull(found, "found");
            }

            @Override
            public @Nonnull CauseCategory getCategory()
            {
                return CauseCategories.STACK_TYPE_INCOMPATIBILITY;
            }

            public @Nonnull StackElementType getExpectedType()
            {
                return expected;
            }

            public @Nonnull StackElementType getFoundType()
            {
                return found;
            }

            @Override
            public @Nonnull String toString()
            {
                return "Stack element type incompatibility. Expected: " + expected + ", found: " + found;
            }

            private final StackElementType expected, found;
        }

        public static class CategoryIncompatibility implements Cause
        {
            public CategoryIncompatibility(int expected, int found)
            {
                this.expected = expected;
                this.found = found;
            }

            @Override
            public @Nonnull CauseCategory getCategory()
            {
                return CauseCategories.STACK_CATEGORY_INCOMPATIBILITY;
            }

            public int getExpectedCategory()
            {
                return expected;
            }

            public int getFoundCategory()
            {
                return found;
            }

            public @Nonnull String toString()
            {
                return "Stack element category incompatibility. Expected " + expected + ", found: " + found;
            }

            private final int expected;

            private final int found;
        }
    }
}
