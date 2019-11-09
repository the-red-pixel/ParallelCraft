package org.kucro3.parallelcraft.aopeng.asm;

import org.kucro3.parallelcraft.aopeng.util.ArrayFIFOQuery;
import org.kucro3.parallelcraft.aopeng.util.ArrayLIFOQuery;
import org.kucro3.parallelcraft.aopeng.util.FIFOQuery;
import org.kucro3.parallelcraft.aopeng.util.LIFOQuery;
import org.objectweb.asm.Label;

import javax.annotation.Nonnull;
import java.util.*;

public class BlockTable {
    public BlockTable()
    {
    }

    public void addStartLabel(@Nonnull Label startLabel)
    {
        this.startLabels.add(startLabel);

        System.out.println("ST: " + startLabel);
    }

    public void addTryCatchRecord(@Nonnull TryCatchRecord record)
    {
        this.tryCatchRecordMap.put(record.getStart(), record);
        this.tryCatchHandlerLabels.add(record.getHandler());
    }

    public @Nonnull FIFOQuery<Label> createFIFOQuery()
    {
        return new ArrayFIFOQuery<>(startLabels.toArray(new Label[0]));
    }

    public @Nonnull LIFOQuery<Label> createLIFOQuery()
    {
        return new ArrayLIFOQuery<>(startLabels.toArray(new Label[0]));
    }

    public @Nonnull Set<Label> createSet()
    {
        return new HashSet<>(startLabels);
    }

    public @Nonnull List<Label> getStartLabels()
    {
        return startLabels;
    }

    public @Nonnull Map<Label, TryCatchRecord> getTryCatchRecords()
    {
        return tryCatchRecordMap;
    }

    public @Nonnull List<Label> getTryCatchHandlerLabels()
    {
        return tryCatchHandlerLabels;
    }

    private final List<Label> startLabels = new ArrayList<>();

    private final Map<Label, TryCatchRecord> tryCatchRecordMap = new HashMap<>();

    private final List<Label> tryCatchHandlerLabels = new ArrayList<>();

    public static class TryCatchRecord
    {
        public TryCatchRecord(@Nonnull Label start,
                              @Nonnull Label end,
                              @Nonnull Label handler,
                              @Nonnull String type)
        {
            this.start = Objects.requireNonNull(start, "start");
            this.end = Objects.requireNonNull(end, "end");
            this.handler = Objects.requireNonNull(handler, "handler");
            this.type = Objects.requireNonNull(type, "type");
        }

        public @Nonnull Label getStart()
        {
            return start;
        }

        public @Nonnull Label getEnd()
        {
            return end;
        }

        public @Nonnull Label getHandler()
        {
            return handler;
        }

        public @Nonnull String getType()
        {
            return type;
        }

        private final Label start;

        private final Label end;

        private final Label handler;

        private final String type;
    }
}
