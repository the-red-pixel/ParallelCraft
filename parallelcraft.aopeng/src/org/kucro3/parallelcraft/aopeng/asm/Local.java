package org.kucro3.parallelcraft.aopeng.asm;

import org.kucro3.parallelcraft.aopeng.util.Attachable;
import org.kucro3.parallelcraft.aopeng.util.Attachment;
import org.kucro3.parallelcraft.aopeng.util.AttachmentKey;
import org.objectweb.asm.Label;

import javax.annotation.Nonnull;

public interface Local extends Attachable {
    // return relative index
    public int allocate();

    public boolean allocated();

    public @Nonnull LocalDistribution getDistributor();

    public int getRelativeIndex();

    public int getAbsoluteIndex();

    public @Nonnull Source getSource();

    public @Nonnull Attachment getAttachments();

    public static enum Source
    {
        THIS,
        PARAM,
        ALLOCATION
    }

    public static final AttachmentKey<String> ATTACHMENT_NAME = AttachmentKey.create(String.class);

    public static final AttachmentKey<Label> ATTACHMENT_START_LABEL = AttachmentKey.create(Label.class);

    public static final AttachmentKey<Label> ATTACHMENT_END_LABEL = AttachmentKey.create(Label.class);
}
