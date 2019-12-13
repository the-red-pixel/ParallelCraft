package org.kucro3.parallelcraft.aopeng.util;

public class AttachmentKey<T> {
    AttachmentKey()
    {
    }

    public static <T> AttachmentKey<T> create()
    {
        return new AttachmentKey<>();
    }
}
