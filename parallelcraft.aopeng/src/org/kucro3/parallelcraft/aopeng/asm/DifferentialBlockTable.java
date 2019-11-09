package org.kucro3.parallelcraft.aopeng.asm;

import org.kucro3.parallelcraft.aopeng.util.Attachable;
import org.kucro3.parallelcraft.aopeng.util.Attachment;
import org.objectweb.asm.Label;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class DifferentialBlockTable {
    public DifferentialBlockTable(@Nonnull List<LabelRecordCollection> records)
    {
        this.records = Objects.requireNonNull(records);
    }

    public @Nonnull LabelRecordQuery createQuery()
    {
        return new LabelRecordQuery(records);
    }

    public static @Nonnull Builder builder()
    {
        return new Builder();
    }

    public @Nonnull List<LabelRecordCollection> getRecords()
    {
        return Collections.unmodifiableList(records);
    }

    private final List<LabelRecordCollection> records;

    public static abstract class LabelRecord implements Attachable
    {
        LabelRecord(@Nonnull LabelRecordType type)
        {
            this.type = type;
        }

        public @Nonnull LabelRecordType getType()
        {
            return type;
        }

        @Override
        public @Nonnull Attachment getAttachments()
        {
            if (attachments == null)
                return attachments = new Attachment();

            return attachments;
        }

        private Attachment attachments;

        private final LabelRecordType type;
    }

    public static enum LabelRecordType
    {
        JUMP_TARGET,
        HANDLER_SCOPE_START,
        HANDLER_SCOPE_END,
        HANDLER
    }

    public static class LabelJumpTargetRecord extends LabelRecord
    {
        LabelJumpTargetRecord()
        {
            super(LabelRecordType.JUMP_TARGET);
        }

        public static @Nonnull LabelJumpTargetRecord of()
        {
            return INSTANCE;
        }

        private static final LabelJumpTargetRecord INSTANCE = new LabelJumpTargetRecord();
    }

    public static abstract class LabelHandlerRecordBase extends LabelRecord
    {
        LabelHandlerRecordBase(@Nonnull LabelRecordType type,
                               @Nonnull TryCatchHandle handle)
        {
            super(type);
            this.handle = Objects.requireNonNull(handle);
        }

        public @Nonnull TryCatchHandle getHandle()
        {
            return handle;
        }

        private final TryCatchHandle handle;
    }

    public static class LabelHandlerRecord extends LabelHandlerRecordBase
    {
        LabelHandlerRecord(TryCatchHandle handle)
        {
            super(LabelRecordType.HANDLER, handle);
        }

        public static @Nonnull LabelHandlerRecord of(@Nonnull TryCatchHandle handle)
        {
            return new LabelHandlerRecord(handle);
        }
    }

    public static class LabelHandlerScopeStartRecord extends LabelHandlerRecordBase
    {
        LabelHandlerScopeStartRecord(TryCatchHandle handle)
        {
            super(LabelRecordType.HANDLER_SCOPE_START, handle);
        }

        public static @Nonnull LabelHandlerScopeStartRecord of(@Nonnull TryCatchHandle handle)
        {
            return new LabelHandlerScopeStartRecord(handle);
        }
    }

    public static class LabelHandlerScopeEndRecord extends LabelHandlerRecordBase
    {
        LabelHandlerScopeEndRecord(TryCatchHandle handle)
        {
            super(LabelRecordType.HANDLER_SCOPE_END, handle);
        }

        public static @Nonnull LabelHandlerScopeEndRecord of(@Nonnull TryCatchHandle handle)
        {
            return new LabelHandlerScopeEndRecord(handle);
        }
    }

    private static class Node<T>
    {
        Node(T element)
        {
            this.element = element;
        }

        final T element;

        Node<T> next;
    }

    public static class Link<T> implements Iterable<T>
    {
        public void add(T element)
        {
            if (head == null)
                head = tail = new Node<>(element);
            else
                tail = tail.next = new Node<>(element);

            size++;
            modCount++;
        }

        @Override
        public @Nonnull Iterator<T> iterator()
        {
            return new Iter(modCount);
        }

        public int size()
        {
            return size;
        }

        private Node<T> head;

        private Node<T> tail;

        private int modCount = 0;

        private int size = 0;

        private class Iter implements Iterator<T>
        {
            Iter(int modCount)
            {
                this.modCount = modCount;
            }

            @Override
            public boolean hasNext()
            {
                checkMod();

                if (terminated)
                    return false;

                if (current == null)
                    return !(terminated = head == null);

                return !(terminated = current.next == null);
            }

            @Override
            public T next()
            {
                checkMod();

                if (terminated)
                    throw new NoSuchElementException();

                if (current == null)
                    current = head;
                else
                    current = current.next;

                if (current == null)
                {
                    this.terminated = true;

                    throw new NoSuchElementException();
                }

                return current.element;
            }

            void checkMod()
            {
                if (Link.this.modCount != modCount)
                    throw new ConcurrentModificationException();
            }

            private Node<T> current;

            private boolean terminated = false;

            private final int modCount;
        }
    }

    public static class LabelRecordCollection extends Link<LabelRecord>
    {
        public LabelRecordCollection()
        {
            this(-1); // iconst_m1
        }

        public LabelRecordCollection(@Nonnegative int ordinal)
        {
            this.ordinal = ordinal;
        }

        public @Nonnegative int ordinal()
        {
            if (ordinal < 0)
                throw new IllegalStateException("Label-record pair mismatch");

            return ordinal;
        }

        private int ordinal;
    }

    public static class LabelRecordQuery
    {
        LabelRecordQuery(@Nonnull List<LabelRecordCollection> records)
        {
            this.iter = records.iterator();

            next();
        }

        // FIFO query, the record array should be sorted previously
        public @Nullable LabelRecordCollection query(int ordinal)
        {
            if (current == null)
                return null;

            if (ordinal == current.ordinal())
            {
                LabelRecordCollection result = current;

                next();

                return result;
            }

            return null;
        }

        public boolean completed()
        {
            return current != null;
        }

        void next()
        {
            if (iter.hasNext())
                current = iter.next();
            else
                current = null;
        }

        private LabelRecordCollection current;

        private final Iterator<LabelRecordCollection> iter;
    }

    public static class TryCatchHandle
    {
        public TryCatchHandle(@Nonnull String type)
        {
            this.type = Objects.requireNonNull(type);
        }

        public @Nonnull String getType()
        {
            return type;
        }

        // identical
        @Override
        public boolean equals(Object obj)
        {
            return this == obj;
        }

        private final String type;
    }

    public static class Builder
    {
        Builder()
        {
        }

        public void next(Label label)
        {
            LabelRecordCollection collection
                    = mappedCollection.computeIfAbsent(label, (unused) -> new LabelRecordCollection());

            collection.ordinal = ordinal++;

            ordered.add(collection);
        }

        void record(Label label, LabelRecord record)
        {
            mappedCollection.computeIfAbsent(label, (unused) -> new LabelRecordCollection())
                    .add(record);
        }

        public void recordJumpTarget(Label label)
        {
            record(label, LabelJumpTargetRecord.of());
        }

        public void recordTryCatch(Label start, Label end, Label handler, String type)
        {
            TryCatchHandle handle = new TryCatchHandle(type);

            record(start, LabelHandlerScopeStartRecord.of(handle));
            record(end, LabelHandlerScopeEndRecord.of(handle));
            record(handler, LabelHandlerRecord.of(handle));
        }

        public @Nonnull DifferentialBlockTable build()
        {
            if (ordered.size() != mappedCollection.size())
                throw new IllegalStateException("Label-record pair mismatch");

            return new DifferentialBlockTable(ordered);
        }

        private int ordinal = 0;

        private final List<LabelRecordCollection> ordered = new ArrayList<>();

        private final Map<Label, LabelRecordCollection> mappedCollection = new HashMap<>();
    }
}
