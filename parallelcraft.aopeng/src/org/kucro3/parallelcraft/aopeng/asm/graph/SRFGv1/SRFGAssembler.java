package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import org.kucro3.parallelcraft.aopeng.asm.graph.Node;
import org.kucro3.parallelcraft.aopeng.asm.graph.DifferentialVisitMeta;
import org.kucro3.parallelcraft.aopeng.asm.graph.Path;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.EscapeNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn.InstructionNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn.JumpInstructionNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn.LookupSwitchInstructionNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn.TableSwitchInstructionNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;
import org.kucro3.parallelcraft.aopeng.util.AttachmentKey;
import org.kucro3.parallelcraft.aopeng.util.TypeAttributedAttachmentKey;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;

@NotThreadSafe
public class SRFGAssembler {
    public SRFGAssembler(@Nonnull SRFGraph graph)
    {
        this.graph = Objects.requireNonNull(graph, "graph");
    }

    public SRFGAssembler(@Nonnull SRFGraph graph,
                         @Nonnull SRFNodeContextPool nodeContextPool,
                         @Nonnull SRFBlockContextPool blockContextPool)
    {
        this(graph);

        setNodeContextPool(nodeContextPool);
        setBlockContextPool(blockContextPool);
    }

    // *Note: Tail recursion is used in this process. Though using normal recursion
    //        could make coding simpler and easier, but that will consume lots of
    //        stack resource and make the process of debug much more complex.
    //
    //        Instructions could be large in number and this will heavily pressure GC,
    //        so the context objects are all managed by pool. This makes the process
    //        of assembling and tail recursion more efficient and the pool can be shared
    //        with different assemblers, but not concurrently.
    public InsnList assemble()
    {
        DifferentialVisitMeta visitMeta = graph.getVisitMeta();

        SRFNodeContextPool nodeContextPool = getNodeContextPool();
        SRFBlockContextPool blockContextPool = getBlockContextPool();

        nodeContextPool.reset();
        blockContextPool.reset();

        List<SRFBlockNode> jumpTargets = new ArrayList<>();

        InsnList rootInsnList = new InsnList();

        // initialize root
        blockContextPool.acquire(graph.getRoot(), new InsnList());

        // assemble process
        while (!blockContextPool.empty())
        {
            SRFBlockContext blockContext = blockContextPool.peek();
            SRFBlockNode blockNode = blockContext.requireSubject();

            switch (blockContext.getStage())
            {
                case 0: // visit stage
                    if (!visitMeta.visited(blockNode))
                    {
                        List<SRF> srfs = blockNode.getFlowBlock().getSRFs();

                        if (srfs.isEmpty())
                            blockContextPool.release(); // exit if the block is empty
                        else
                        {
                            // get or initialize the block insn list
                            InsnList blockInsnList = blockContext.getBlockInsnList();

                            if (blockInsnList == null)
                                blockContext.setBlockInsnList(blockInsnList = new InsnList());

                            // add the root node into the visiting context pool
                            nodeContextPool.acquire(srfs.get(0).getRoot(), blockInsnList, Direction.AIR);

                            blockContext.nextStage();
                        }
                    }
                    else
                        blockContextPool.release(); // exit if the block has been already visited

                    break;

                case 1: // finish stage
                    assert !visitMeta.visited(blockNode);

                    // *Note: The end label of current block is actually the start label
                    //        of the next block, but it simplifies the process appending
                    //        an ending label to the terminal.
                    //
                    // append the start label and end label of the block
                    InsnList blockInsnList = blockContext.requireBlockInsnList();

                    blockInsnList.insert(acquireLabel(blockNode));

                    visitMeta.visit(blockNode);

                    blockContextPool.release();

                    // process jump targets
                    for (SRFBlockNode target : jumpTargets)
                        if (!visitMeta.visited(target))
                            blockContextPool.acquire(target, new InsnList());

                    jumpTargets.clear();

                    // push lower block node if exists
                    GraphNodeManipulator<SRFBlockNode> manipulator = blockNode.getManipulator();

                    if (manipulator.getLowerPathCount() != 0)
                    {
                        assert manipulator.getLowerPathCount() == 1;

                        SRFBlockNode lowerBlockNode = manipulator.getLowerPath(0).getLowerNode();

                        if (!visitMeta.visited(lowerBlockNode))
                            blockContextPool.acquire(lowerBlockNode, new InsnList());
                        else
                        {
                            // *Note: Inserting GOTO instruction is necessary when the lower block
                            //        has been visited previously.
                            assert blockLabelMap.containsKey(lowerBlockNode);

                            blockInsnList.add(new JumpInsnNode(Opcodes.GOTO, blockLabelMap.get(lowerBlockNode)));
                        }
                    }

                    blockInsnList.add(acquireEndLabel(blockNode));

                    //
                    rootInsnList.add(blockInsnList);

                    break;
            }

            if (nodeContextPool.empty())
                continue;

            while (!nodeContextPool.empty())
            {
                SRFNodeContext nodeContext = nodeContextPool.peek();

                SRFNode node = nodeContext.requireSubject();
                GraphNodeManipulator<SRFNode> manipulator = node.getManipulator();

                int upperCount;

                // exit immediately if already visited
                if (visitMeta.visited(node))
                {
                    nodeContextPool.release();

                    continue;
                }

                switch (nodeContext.getStage())
                {
                    case 0: // post-visit stage
                        upperCount = manipulator.getUpperPathCount();

                        if (upperCount == 1)
                        {
                            Path<SRFNode> onlyUpperPath = manipulator.peekUpperPath();
                            SRFNode onlyUpperNode = onlyUpperPath.getUpperNode();

                            if (!visitMeta.visited(onlyUpperNode)) // visit upwards
                                attachInitially(onlyUpperNode,
                                        nodeContextPool.acquire(
                                                onlyUpperNode,
                                                nodeContext.requireSourceInsnList(),
                                                Direction.UPWARDS,
                                                onlyUpperPath));

                            nodeContext.nextStage();

                            break;
                        }
                        else if (upperCount > 1)
                        {
                            InsnCluster cluster = nodeContext.getInsnCluster();

                            if (cluster == null) // first reach
                            {
                                // mark
                                nodeContext.mark();

                                nodeContext.setInsnCluster(cluster = new InsnCluster(upperCount));

                                if (Direction.DOWNWARDS.equals(nodeContext.requireSourceDirection()))
                                    cluster.set(
                                            nodeContext.requireSourcePath().getLowerOrdinal(),
                                            nodeContext.requireSourceInsnList());
                            }

                            if (!cluster.hasNext()) // forward assurance completed
                            {
                                nodeContext.nextStage();

                                break;
                            }

                            // forward assurance
                            int next = cluster.nextIndex();
                            InsnList branch = cluster.next();

                            Path<SRFNode> nextPath = manipulator.getUpperPath(next);
                            SRFNode nextUpperNode = nextPath.getUpperNode();

                            // push
                            if (!visitMeta.visited(nextUpperNode))
                                nodeContextPool
                                        .acquire(nextUpperNode, branch, Direction.UPWARDS, nextPath);
                            else
                                throw new ConcurrentModificationException();

                            break;
                        }

                        // no upper node
                        nodeContext.nextStage();

                        break;

                    case 1: // escape stage
                        if (SRFGv1NodeTypes.ESCAPE.equals(node.getType()))
                        {
                            // escape
                            nodeContextPool.acquire(
                                    ((EscapeNode) node).getTarget().getRoot(),
                                    nodeContext.requireSourceInsnList(),
                                    Direction.AIR);
                        }

                        nodeContext.nextStage();
                        break;

                    case 2: // visit stage
                        upperCount = manipulator.getUpperPathCount();

                        // *Note: A context can only be marked when it was visited during the
                        //        process of multi-branch forward assurance, otherwise the
                        //        mark flag should be ingored.
                        if (upperCount > 1 && nodeContext.isMarked())
                        {
                            // link the insn lists generated in multi-branch forward assurance
                            InsnCluster cluster = nodeContext.requireInsnCluster();

                            if (cluster.hasNext())
                                throw new IllegalStateException("Incomplete forward assurance");

                            // merge the cluster, all to the first insn list in the cluster
                            InsnList clusterRoot = cluster.require(0);

                            int size = cluster.size();
                            for (int i = 1; i < size; i++)
                                clusterRoot.add(cluster.require(i));

                            // *Note: The root InsnList of the block will be set to the root InsnList
                            //        of the cluster. This action ensures that the InsnList in the
                            //        context is always the object of top section and holds the actual
                            //        head of the whole block InsnList.
                            blockContext.setBlockInsnList(clusterRoot);

                            // *Note: The source InsnList of the node will be set to the root InsnList
                            //        of the cluster. This action makes it seem like that the process of
                            //        forward assurance begins from the first path in ordinal and makes
                            //        the assembling process easier to handle.
                            nodeContext.setSourceInsnList(clusterRoot);
                        }

                        // assemble instruction
                        if (SRFGv1NodeTypes.INSTRUCTION == node.getType())
                        {
                            InstructionNode insnNode = (InstructionNode) node;

                            insnNode.accept(
                                    nodeContext.requireSourceInsnList(),
                                    blockLabelMap,
                                    true);

                            // add jump targets into the list
                            switch (insnNode.getInstructionType())
                            {
                                case InstructionNode.JUMP_INSN:
                                    jumpTargets.add(((JumpInstructionNode) node).getTarget());

                                    break;

                                case InstructionNode.LOOKUPSWITCH_INSN:
                                    jumpTargets.addAll(((LookupSwitchInstructionNode) node).getTargets());

                                    break;

                                case InstructionNode.TABLESWITCH_INSN:
                                    jumpTargets.addAll(((TableSwitchInstructionNode) node).getTargets());

                                    break;
                            }
                        }

                        visitMeta.visit(node);

                        unattach(node);

                        // prepare to visit lower nodes
                        int lowerPathCount = manipulator.getLowerPathCount();

                        if (lowerPathCount == 0)
                            if (!node.hasEscapeTarget())
                                nodeContextPool.release();
                            else // process terminal escape
                            {
                                // *Note: The first context can just reuse the current context object
                                //        if not attached previously.
                                SRFNode nextSRFRoot = node.requireEscapeTarget().getRoot();

                                nodeContext.setSubject(nextSRFRoot);
//                              nodeContext.setSourceInsnList(nodeContext.requireSourceInsnList());
                                nodeContext.setSourceDirection(Direction.AIR);

                                nodeContext.resetStage();
                                nodeContext.resetInsnCluster(); // insn cluster is not inheritable

                                attachInitially(nextSRFRoot, nodeContext);
                            }
                        else if (lowerPathCount == 1 && Direction.UPWARDS.equals(nodeContext.requireSourceDirection()))
                            nodeContextPool.release();
                        else
                        {
                            Path<SRFNode> lowerPath = manipulator.getLowerPath(0);
                            SRFNode lowerNode = lowerPath.getLowerNode();

                            SRFNodeContext attachedContext = attached(lowerNode);

                            if (attachedContext == null)
                            {
                                // reusing current context

                                nodeContext.setSubject(lowerNode);
//                              nodeContext.setSourceInsnList(nodeContext.requireSourceInsnList());
                                nodeContext.setSourceDirection(Direction.DOWNWARDS);
                                nodeContext.setSourcePath(lowerPath);

                                nodeContext.resetStage();
                                nodeContext.resetInsnCluster(); // insn cluster is not inheritable

                                attachInitially(lowerNode, nodeContext);
                            }
                            else
                            {
                                nodeContextPool.release();

                                // insert a reference of previously attached context into the pool
                                nodeContextPool.reference(attachedContext);
                            }

                            for (int i = 1; i < lowerPathCount; i++)
                            {
                                lowerPath = manipulator.getLowerPath(i);
                                lowerNode = lowerPath.getLowerNode();

                                attachedContext = attached(lowerNode);

                                if (attachedContext == null)
                                    attachInitially(lowerNode,
                                            nodeContextPool.acquire(
                                                    lowerNode,
                                                    nodeContext.requireSourceInsnList(),
                                                    Direction.DOWNWARDS,
                                                    lowerPath
                                            ));
                                else
                                    nodeContextPool.reference(attachedContext);
                            }
                        }

                        break;
                }
            }
        }

        // TODO further process for exception & handler table

        return rootInsnList;
    }

    public @Nonnull DifferentialVisitMeta getVisitMeta()
    {
        return graph.getVisitMeta();
    }

    public @Nonnull SRFGraph getGraph()
    {
        return graph;
    }

    public @Nonnull SRFNodeContextPool getNodeContextPool()
    {
        return nodeContextPool == null ? nodeContextPool = new SRFNodeContextPool() : nodeContextPool;
    }

    public void setNodeContextPool(@Nonnull SRFNodeContextPool nodeContextPool)
    {
        this.nodeContextPool = Objects.requireNonNull(nodeContextPool);
    }

    public @Nonnull SRFBlockContextPool getBlockContextPool()
    {
        return blockContextPool == null ? blockContextPool = new SRFBlockContextPool() : blockContextPool;
    }

    public void setBlockContextPool(@Nonnull SRFBlockContextPool blockContextPool)
    {
        this.blockContextPool = Objects.requireNonNull(blockContextPool);
    }

    public void reset()
    {
        nodeContextPool.reset();
        blockContextPool.reset();

        blockLabelMap.clear();
        blockEndLabelMap.clear();

        nodeContextAttachmentKey = TypeAttributedAttachmentKey.create(SRFNodeContext.class);
        blockContextAttachmentKey = TypeAttributedAttachmentKey.create(SRFBlockContext.class);
    }

    public void reset(@Nonnull SRFGraph graph)
    {
        reset();

        this.graph = Objects.requireNonNull(graph, "graph");
    }

    public void reset(@Nonnull SRFGraph graph,
                      @Nonnull SRFNodeContextPool nodeContextPool,
                      @Nonnull SRFBlockContextPool blockContextPool)
    {
        reset(graph);

        setNodeContextPool(nodeContextPool);
        setBlockContextPool(blockContextPool);
    }

    protected void attachInitially(SRFNode node, SRFNodeContext context)
    {
        if (node.getAttachments().putAttachment(nodeContextAttachmentKey, context) != null)
            throw new IllegalStateException("Duplicated context attribute");
    }

    protected void unattach(SRFNode node)
    {
        node.getAttachments().removeAttachment(nodeContextAttachmentKey);
    }

    protected SRFNodeContext attached(SRFNode node)
    {
        return node.getAttachments().getAttachment(nodeContextAttachmentKey);
    }

    protected void attachInitially(SRFBlockNode node, SRFBlockContext context)
    {
        if (node.getAttachments().putAttachment(blockContextAttachmentKey, context) != null)
            throw new IllegalStateException("Duplicated context attribute");
    }

    protected void unattach(SRFBlockNode node)
    {
        node.getAttachments().removeAttachment(blockContextAttachmentKey);
    }

    protected SRFBlockContext attached(SRFBlockNode node)
    {
        return node.getAttachments().getAttachment(blockContextAttachmentKey);
    }

    @Nonnull LabelNode acquireLabel(SRFBlockNode blockNode)
    {
        return blockLabelMap.computeIfAbsent(blockNode, (unused) -> new LabelNode(new Label()));
    }

    @Nonnull LabelNode acquireEndLabel(SRFBlockNode blockNode)
    {
        return blockEndLabelMap.computeIfAbsent(blockNode, (unused) -> new LabelNode(new Label()));
    }

    private SRFNodeContextPool nodeContextPool;

    private SRFBlockContextPool blockContextPool;

    protected AttachmentKey<SRFNodeContext> nodeContextAttachmentKey
            = TypeAttributedAttachmentKey.create(SRFNodeContext.class);

    protected AttachmentKey<SRFBlockContext> blockContextAttachmentKey
            = TypeAttributedAttachmentKey.create(SRFBlockContext.class);

    private final Map<SRFBlockNode, LabelNode> blockLabelMap = new HashMap<>();

    private final Map<SRFBlockNode, LabelNode> blockEndLabelMap = new HashMap<>();

    private SRFGraph graph;

    public static class InsnCluster
    {
        public InsnCluster(@Nonnegative int size)
        {
            this.lists = new InsnList[size];
        }

        public @Nullable InsnList get(@Nonnegative int index)
        {
            return lists[index];
        }

        public @Nonnull InsnList require(@Nonnegative int index)
        {
            return Objects.requireNonNull(lists[index]);
        }

        public void set(@Nonnegative int index, @Nullable InsnList insnList)
        {
            lists[index] = insnList;
        }

        public @Nonnull InsnList[] get()
        {
            return lists;
        }

        public boolean exists(@Nonnegative int index)
        {
            return lists[index] != null;
        }

        public boolean hasNext()
        {
            if (walker == lists.length)
                return false;

            if (lists[walker] == null)
                return true;

            for (; walker < lists.length; walker++)
                if (lists[walker] == null)
                    break;

            return walker != lists.length;
        }

        public @Nonnegative int nextIndex()
        {
            if (!hasNext())
                throw new NoSuchElementException("full cluster");

            return walker;
        }

        public @Nonnull InsnList next()
        {
            int index = nextIndex();

            InsnList list = new InsnList();

            set(index, list);

            return list;
        }

        public int size()
        {
            return lists.length;
        }

        private int walker = 0;

        private final InsnList[] lists;
    }

    // for tail recursion
    public static class ContextPool<T extends Node<T>>
    {
        public ContextPool()
        {
            this.pool = new ArrayList<>();
            this.spare = new ArrayList<>();
        }

        protected ContextPool(@Nonnegative int poolCapacity,
                              @Nonnegative int spareCapacity)
        {
            this.pool = new ArrayList<>(poolCapacity);
            this.spare = new ArrayList<>(spareCapacity);
        }

        public static @Nonnull <T extends Node<T>> ContextPool<T> allocate(@Nonnegative int capacity)
        {
            return allocate(capacity, capacity);
        }

        public static @Nonnull <T extends Node<T>> ContextPool<T> allocate(@Nonnegative int poolCapacity,
                                                                           @Nonnegative int spareCapacity)
        {
            ContextPool<T> pool = new ContextPool<>(poolCapacity, spareCapacity);

            fill(pool);

            return pool;
        }

        protected static <T extends Node<T>> void fill(ContextPool<T> contextPool)
        {
            for (int i = 0; i < contextPool.pool.size(); i++)
                contextPool.pool.add(contextPool.newContext());
        }

        public @Nonnull Context<T> peek()
        {
            if (empty())
                throw new NoSuchElementException();

            return pool.get(depth - 1);
        }

        protected Context<T> newContext()
        {
            return new Context<>();
        }

        public @Nonnull Context<T> allocate()
        {
            Context<T> ctx = newContext();

            pool.add(ctx);
            depth++;

            return ctx.activate();
        }

        public @Nonnull Context<T> acquire()
        {
            if (depth < allocated())
                return pool.get(depth++).activate();

            return allocate();
        }

        public @Nonnull Context<T> acquire(@Nonnull T subject)
        {
            Context<T> context = acquire();

            context.setSubject(subject);

            return context;
        }

        public void reference(@Nonnull Context<T> context)
        {
            if (depth < allocated())
            {
                // swap and put the previous context object into the spare space
                spare.add(pool.get(depth));

                pool.set(depth++, context);
            }
            else
            {
                // full usage, expand
                pool.add(context);
                depth++;

                spare.add(newContext());
            }

            context.reference();
        }

        void inactivate()
        {
            Context<T> context = pool.get(--depth);

            context.inactivate();

            // *Note: A context which is referenced more than once is still active after the
            //        single operation of inactivation and it's necessary to replace the reference
            //        context with another inactive context in the spare space. Otherwise, the
            //        active reference on the top of the pool will cause further problems.
            if (context.active())
                pool.set(depth, spare.remove(spare.size() - 1));
        }

        public void release()
        {
            if (empty())
                throw new NoSuchElementException();

            inactivate();
        }

        public void reset()
        {
            while (depth != 0)
                inactivate();
        }

        public void clear()
        {
            reset();

            pool.clear();
        }

        public boolean empty()
        {
            return depth == 0;
        }

        public int depth()
        {
            return depth;
        }

        public int allocated()
        {
            return pool.size();
        }

        // *Note: Every pool has a spare space to store the contexts that are swaped
        //        out of the pool by context references. This prevents reallocations
        //        of context objects.
        protected final ArrayList<Context<T>> spare;

        protected final ArrayList<Context<T>> pool;

        protected int depth = 0;
    }

    public static class SRFNodeContextPool extends ContextPool<SRFNode>
    {
        public SRFNodeContextPool()
        {
            super();
        }

        public SRFNodeContextPool(@Nonnegative int poolCapacity,
                                  @Nonnegative int spareCapacity)
        {
            super(poolCapacity, spareCapacity);
        }

        public static @Nonnull SRFNodeContextPool allocate(@Nonnegative int capacity)
        {
            return allocate(capacity, capacity);
        }

        public static @Nonnull SRFNodeContextPool allocate(@Nonnegative int poolCapacity,
                                                           @Nonnegative int spareCapacity)
        {
            SRFNodeContextPool pool = new SRFNodeContextPool(poolCapacity, spareCapacity);

            fill(pool);

            return pool;
        }

        @Override
        protected SRFNodeContext newContext()
        {
            return new SRFNodeContext();
        }

        public @Nonnull SRFNodeContext peek()
        {
            return (SRFNodeContext) super.peek();
        }

        @Override
        public @Nonnull SRFNodeContext allocate()
        {
            return (SRFNodeContext) super.allocate();
        }

        @Override
        public @Nonnull SRFNodeContext acquire()
        {
            return (SRFNodeContext) super.acquire();
        }

        @Override
        public @Nonnull SRFNodeContext acquire(@Nonnull SRFNode subject)
        {
            SRFNodeContext context = acquire();

            context.setSubject(subject);

            return context;
        }

        public @Nonnull SRFNodeContext acquire(@Nonnull SRFNode subject,
                                               @Nonnull InsnList sourceInsnList)
        {
            SRFNodeContext context = acquire(subject);

            context.setSourceInsnList(sourceInsnList);

            return context;
        }

        // in this case, the source direction should be AIR
        public @Nonnull SRFNodeContext acquire(@Nonnull SRFNode subject,
                                               @Nonnull InsnList sourceInsnList,
                                               @Nonnull Direction sourceDirection)
        {
            assert Direction.AIR.equals(sourceDirection);

            SRFNodeContext context = acquire(subject);

            context.setSourceInsnList(sourceInsnList);
            context.setSourceDirection(sourceDirection);

            return context;
        }

        // in this case, the source direction should not be AIR
        protected @Nonnull SRFNodeContext acquire(@Nonnull SRFNode subject,
                                                  @Nonnull InsnList sourceInsnList,
                                                  @Nonnull Direction sourceDirection,
                                                  @Nonnull Path<SRFNode> sourcePath)
        {
            assert !Direction.AIR.equals(sourceDirection);

            SRFNodeContext context = acquire(subject);

            context.setSourceInsnList(sourceInsnList);
            context.setSourceDirection(sourceDirection);
            context.setSourcePath(sourcePath);

            return context;
        }
    }

    public static class SRFBlockContextPool extends ContextPool<SRFBlockNode>
    {
        public SRFBlockContextPool()
        {
            super();
        }

        protected SRFBlockContextPool(@Nonnegative int poolCapacity,
                                      @Nonnegative int spareCapacity)
        {
            super(poolCapacity, spareCapacity);
        }

        public static @Nonnull SRFBlockContextPool allocate(@Nonnegative int capacity)
        {
            return allocate(capacity, capacity);
        }

        public static @Nonnull SRFBlockContextPool allocate(@Nonnegative int poolCapacity,
                                                            @Nonnegative int spareCapacity)
        {
            SRFBlockContextPool pool = new SRFBlockContextPool(poolCapacity, spareCapacity);

            fill(pool);

            return pool;
        }

        @Override
        protected Context<SRFBlockNode> newContext()
        {
            return new SRFBlockContext();
        }

        @Override
        public @Nonnull SRFBlockContext peek()
        {
            return (SRFBlockContext) super.peek();
        }

        @Override
        public @Nonnull SRFBlockContext allocate()
        {
            return (SRFBlockContext) super.allocate();
        }

        @Override
        public @Nonnull SRFBlockContext acquire()
        {
            return (SRFBlockContext) super.acquire();
        }

        @Override
        public @Nonnull SRFBlockContext acquire(@Nonnull SRFBlockNode subject)
        {
            return (SRFBlockContext) super.acquire(subject);
        }

        public @Nonnull SRFBlockContext acquire(@Nonnull SRFBlockNode subject,
                                                @Nonnull InsnList blockInsnList)
        {
            SRFBlockContext context = acquire(subject);

            context.setBlockInsnList(blockInsnList);

            return context;
        }
    }

    // *Note: The activity state of a context is represented by its own
    //        reference count.
    public static class Context<T extends Node<T>>
    {
        public void reset()
        {
            resetStage();
            resetSubject();
        }

        public int getStage()
        {
            return this.stage;
        }

        public void nextStage()
        {
            this.stage++;
        }

        public void resetStage()
        {
            this.stage = 0;
        }

        public int referenceCount()
        {
            return referenceCount;
        }

        public void reference()
        {
            this.referenceCount++;
        }

        public void setSubject(@Nonnull T subject)
        {
            this.subject = Objects.requireNonNull(subject);
        }

        public @Nonnull T requireSubject()
        {
            T subject = getSubject();

            if (subject == null)
                throw new IllegalStateException("Context subject not initialized");

            return subject;
        }

        public @Nullable T getSubject()
        {
            return subject;
        }

        public void resetSubject()
        {
            this.subject = null;
        }

        public Context<T> activate()
        {
            referenceCount++;

            return this;
        }

        public void inactivate()
        {
            if (referenceCount > 0)
                referenceCount--;

            if (referenceCount == 0)
                reset();
        }

        public boolean active()
        {
            return referenceCount != 0;
        }

        private @Nonnegative int referenceCount = 0;

        private T subject;

        private int stage;
    }

    public static class SRFNodeContext extends Context<SRFNode>
    {
        public void setSourceInsnList(@Nonnull InsnList insnList)
        {
            this.sourceInsnList = Objects.requireNonNull(insnList);
        }

        public @Nonnull InsnList requireSourceInsnList()
        {
            InsnList insnList = getSourceInsnList();

            if (insnList == null)
                throw new IllegalStateException("Context source insn list not initialized");

            return insnList;
        }

        public @Nullable InsnList getSourceInsnList()
        {
            return sourceInsnList;
        }

        public void resetSourceInsnList()
        {
            this.sourceInsnList = null;
        }

        public void setSourceDirection(@Nonnull Direction direction)
        {
            this.sourceDirection = Objects.requireNonNull(direction);
        }

        public @Nonnull Direction requireSourceDirection()
        {
            Direction direction = getSourceDirection();

            if (direction == null)
                throw new IllegalStateException("Context source direction not initialized");

            return direction;
        }

        public @Nullable Direction getSourceDirection()
        {
            return sourceDirection;
        }

        public void resetSourceDirection()
        {
            this.sourceDirection = null;
        }

        public void setSourcePath(@Nonnull Path<SRFNode> path)
        {
            this.sourcePath = Objects.requireNonNull(path);
        }

        public @Nonnull Path<SRFNode> requireSourcePath()
        {
            Path<SRFNode> path = getSourcePath();

            if (path == null)
                throw new IllegalStateException("Context source path not initialized");

            return path;
        }

        public @Nullable Path<SRFNode> getSourcePath()
        {
            return sourcePath;
        }

        public void resetSourcePath()
        {
            this.sourcePath = null;
        }

        public void setInsnCluster(@Nullable InsnCluster insnCluster)
        {
            this.insnCluster = insnCluster;
        }

        public @Nonnull InsnCluster requireInsnCluster()
        {
            InsnCluster cluster = getInsnCluster();

            if (cluster == null)
                throw new IllegalStateException("Insn cluster not initialized");

            return cluster;
        }

        public @Nullable InsnCluster getInsnCluster()
        {
            return insnCluster;
        }

        public void resetInsnCluster()
        {
            this.insnCluster = null;
        }

        @Override
        public void reset()
        {
            super.reset();

            resetInsnCluster();
            resetSourceDirection();
            resetSourceInsnList();
            resetSourcePath();
        }

        public void mark()
        {
            this.mark = true;
        }

        public void unmark()
        {
            this.mark = false;
        }

        public boolean isMarked()
        {
            return this.mark;
        }

        private Path<SRFNode> sourcePath;

        private Direction sourceDirection;

        private InsnList sourceInsnList;

        private InsnCluster insnCluster;

        private boolean mark = false;
    }

    public static class SRFBlockContext extends Context<SRFBlockNode>
    {
        public void setBlockInsnList(@Nullable InsnList insnList)
        {
            this.blockInsnList = insnList;
        }

        public @Nonnull InsnList requireBlockInsnList()
        {
            InsnList blockInsnList = getBlockInsnList();

            if (blockInsnList == null)
                throw new IllegalStateException("Block insn list not initialized");

            return blockInsnList;
        }

        public @Nullable InsnList getBlockInsnList()
        {
            return blockInsnList;
        }

        public void resetBlockInsnList()
        {
            this.blockInsnList = null;
        }

        @Override
        public void reset()
        {
            super.reset();

            resetBlockInsnList();
        }

        private InsnList blockInsnList;
    }

    public static enum Direction
    {
        AIR,
        DOWNWARDS,
        UPWARDS
    }
}
