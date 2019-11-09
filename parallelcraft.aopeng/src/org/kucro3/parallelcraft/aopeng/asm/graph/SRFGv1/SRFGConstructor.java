package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1;

import com.theredpixelteam.redtea.util.Pair;
import com.theredpixelteam.redtea.util.ShouldNotReachHere;
import com.theredpixelteam.redtea.util.Vector3;
import org.kucro3.parallelcraft.aopeng.asm.BlockTable;
import org.kucro3.parallelcraft.aopeng.asm.MethodDescriptorIterator;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.EscapeNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.InstructionNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.StackBlankNode;
import org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.StackComputation.Operator;
import org.kucro3.parallelcraft.aopeng.asm.graph.VisitMeta;
import org.kucro3.parallelcraft.aopeng.asm.graph.manipulator.GraphNodeManipulator;
import org.kucro3.parallelcraft.aopeng.asm.graph.util.GraphHelper;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;
import java.util.function.Function;

@NotThreadSafe
public class SRFGConstructor extends MethodVisitor implements Opcodes {
    public SRFGConstructor(@Nonnull BlockTable blockTable)
    {
        this(blockTable, ASM7);
    }

    public SRFGConstructor(@Nonnull BlockTable blockTable,
                           @Nullable MethodVisitor mv)
    {
        this(blockTable, ASM7, mv);
    }

    protected SRFGConstructor(@Nonnull BlockTable blockTable,
                              int api)
    {
        this(blockTable, api, null);
    }

    protected SRFGConstructor(@Nonnull BlockTable blockTable,
                              int api,
                              @Nullable MethodVisitor mv)
    {
        super(api, mv);

        Objects.requireNonNull(blockTable, "blockTable");

        this.firstBlockNode = this.currentBlockNode =
                new SRFBlockNode(new SRFBlock(this.visitMeta = new VisitMeta()));

        // pre-construct all jump-targeted blocks
        for (Label startLabel : blockTable.getStartLabels())
            labelBlockMap.put(startLabel, new SRFBlockNode(new SRFBlock(visitMeta)));

        Function<Label, SRFBlockNode> labelBlockMapping = (u) -> new SRFBlockNode(new SRFBlock(visitMeta));

        // pre-construct all throwable-handled subject blocks
        for (BlockTable.TryCatchRecord record : blockTable.getTryCatchRecords().values())
        {
            // put the subject block
            SRFBlockNode blockNode =
                    labelBlockMap.computeIfAbsent(record.getStart(), labelBlockMapping);

            // put the terminal of the subject block
            labelBlockMap.computeIfAbsent(record.getEnd(), labelBlockMapping);

            // put the handler block
            SRFBlockNode handlerBlockNode =
                    labelBlockMap.computeIfAbsent(record.getHandler(), labelBlockMapping);

            blockNode.getFlowBlock().getThrowableHandlers()
                    .pushFirst(new ThrowableHandler(handlerBlockNode, record.getType()));
        }
    }

    @Override
    public void visitInsn(int opcode)
    {
        // direct establishment for NOP instruction
        if (opcode == 0x00)
                // nop
        {
            pushStack(insn(opcode));

            return;
        }

        if ((opcode >= 0x01 && opcode <= 0x0F)
                // aconst_null,
                // iconst_m1, iconst_0, iconst_1, iconst_2, iconst_3, iconst_4, iconst_5,
                // lconst_0, lconst_1,
                // fconst_0, fconst_1, fconst_2,
                // dconst_0, dconst_1
        || (opcode >= 0x2E && opcode <= 0x35)
                // iaload, laload, faload, daload,
                // aaload, baload, caload, saload
        || (opcode >= 0x4F && /*opcode <= 0x56)
                // iastore, lastore, fastore, dastore,
                // aastore, bastore, castore, sastore
        || (opcode >= 0x57 && opcode <= 0x5F)
                // pop, pop2, swap,
                // dup, dup_x1, dup_x2,
                // dup2, dup2_x1, dup2_x2
        || (opcode >= 0x60 &&*/ opcode <= 0x83)
                // iadd, ladd, fadd, dadd, isub, lsub, fsub, dsub,
                // imul, lmul, fmul, dmul, idiv, ldiv, fdiv, ddiv,
                // irem, lrem, frem, drem, ineg, lneg, fneg, dneg,
                // ishl, lshl, ishr, lshr, iushr, lushr,
                // iand, land, ior, lor, ixor, lxor
        || (opcode >= 0x85 && /*opcode <= 0x93)
                // i2l, i2f, i2d, l2i, l2f, l2d,
                // f2i, f2l, f2d, d2i, d2l, d2f,
                // i2b, i2c, i2s
        || (opcode >= 0x94 &&*/ opcode <= 0x98)
                // lcmp, fcmpl, fcmpg, dcmpl, dcmpg
        || (opcode >= 0xAC && opcode <= 0xB1)
                // ireturn, lreturn, freturn, dreturn, areturn, return
        || (opcode == 0xBF)
                // athrow
        || (opcode == 0xC2) || (opcode == 0xC3))
                // monitorenter, monitorexit
            computeStack(insn(opcode));
        else
            throw new IllegalArgumentException(Integer.toHexString(opcode));

        super.visitInsn(opcode);
    }

    private static InstructionNode insn(int opcode)
    {
        return new InstructionNode(new InsnNode(opcode));
    }

    @Override
    public void visitIntInsn(int opcode, int operand)
    {
        if (opcode == 0x10
                // bipush
        ||  opcode == 0x11
                // sipush
        ||  opcode == 0xBC)
                // newarray
            computeStack(intInsn(opcode, operand));
        else
            throw new IllegalArgumentException(Integer.toHexString(opcode));

        super.visitIntInsn(opcode, operand);
    }

    private static InstructionNode intInsn(int opcode, int operand)
    {
        return new InstructionNode(new IntInsnNode(opcode, operand));
    }

    @Override
    public void visitVarInsn(int opcode, int var)
    {
        if ((opcode >= 0x15 && opcode <= 0x19)
                // iload, lload, fload, dload, aload
        || (opcode >= 0x36 && opcode <= 0x3A))
                // istore, lstore, fstore, dstore, astore
            computeStack(varInsn(opcode, var));
        else
            throw new IllegalArgumentException(Integer.toHexString(opcode));

        super.visitVarInsn(opcode, var);
    }

    private static InstructionNode varInsn(int opcode, int var)
    {
        return new InstructionNode(new VarInsnNode(opcode, var));
    }

    @Override
    public void visitTypeInsn(int opcode, String type)
    {
        if (opcode == 0xBB
                // new
        || opcode == 0xBD
                // anewarray
        || opcode == 0xC0
                // checkcast
        || opcode == 0xC1)
                // instanceof
            computeStack(typeInsn(opcode, type));
        else
            throw new IllegalArgumentException(Integer.toHexString(opcode));

        super.visitTypeInsn(opcode, type);
    }

    private static InstructionNode typeInsn(int opcode, String type)
    {
        return new InstructionNode(new TypeInsnNode(opcode, type));
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor)
    {
        if (opcode >= 0xB2 && opcode <= 0xB5)
            computeStack(fieldInsn(opcode, owner, name, descriptor));
        else
            throw new IllegalArgumentException(Integer.toHexString(opcode));

        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    private static InstructionNode fieldInsn(int opcode, String owner, String name, String descriptor)
    {
        return new InstructionNode(new FieldInsnNode(opcode, owner, name, descriptor));
    }

    @Deprecated
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor)
    {
        visitMethodInsn(opcode, owner, name, descriptor, opcode == INVOKEINTERFACE);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface)
    {
        if (opcode >= 0xB6 && opcode <= 0xB9)
            computeStack(methodInsn(opcode, owner, name, descriptor, isInterface));
        else
            throw new IllegalArgumentException(Integer.toHexString(opcode));

        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    private static InstructionNode methodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface)
    {
        return new InstructionNode(new MethodInsnNode(opcode, owner, name, descriptor, isInterface));
    }

    @Override
    public void visitInvokeDynamicInsn(String name,
                                       String descriptor,
                                       Handle bootstrapMethodHandle,
                                       Object... bootstrapMethodArguments)
    {
        computeStack(invokeDynamicMethod(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));

        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    private static InstructionNode invokeDynamicMethod(String name,
                                                       String descriptor,
                                                       Handle bootstrapMethodHandle,
                                                       Object... bootstrapMethodArguments)
    {
        return new InstructionNode(new InvokeDynamicInsnNode(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
    }

    @Override
    public void visitJumpInsn(int opcode, Label label)
    {
        if (opcode >= 0x99 && opcode <= 0xA7)
                // ifeq, ifne, iflt, ifge, ifgt, ifle
                // if_icmpeq, if_icmpne, if_icmplt, if_icmpge
                // if_icmpgt, if_icmple, if_acmpeq, if_acmpne
                // goto
            computeStack(jumpInsn(opcode, label));
        else
            throw new IllegalArgumentException(Integer.toHexString(opcode));

        super.visitJumpInsn(opcode, label);
    }

    private static InstructionNode jumpInsn(int opcode, Label label)
    {
        return new InstructionNode(new JumpInsnNode(opcode, new LabelNode(label)));
    }

    @Override
    public void visitLdcInsn(Object cst)
    {
        computeStack(ldcInsn(cst));

        super.visitLdcInsn(cst);
    }

    private static InstructionNode ldcInsn(Object cst)
    {
        return new InstructionNode(new LdcInsnNode(cst));
    }

    @Override
    public void visitIincInsn(int var, int increment)
    {
        computeStack(iincInsn(var, increment));

        super.visitIincInsn(var, increment);
    }

    private static InstructionNode iincInsn(int var, int increment)
    {
        return new InstructionNode(new IincInsnNode(var, increment));
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels)
    {
        computeStack(tableSwitchInsn(min, max, dflt, labels));

        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    private static InstructionNode tableSwitchInsn(int min, int max, Label dflt, Label... labels)
    {
        return new InstructionNode(new TableSwitchInsnNode(min, max, new LabelNode(dflt), wrap(labels)));
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels)
    {
        computeStack(lookupSwitchInsn(dflt, keys, labels));

        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    private static InstructionNode lookupSwitchInsn(Label dflt, int[] keys, Label[] labels)
    {
        return new InstructionNode(new LookupSwitchInsnNode(new LabelNode(dflt), keys, wrap(labels)));
    }

    private static LabelNode[] wrap(Label[] labels)
    {
        int len = labels.length;

        LabelNode[] nodes = new LabelNode[len];
        for (int i = 0; i < len; i++)
            nodes[i] = new LabelNode(labels[i]);

        return nodes;
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int dimension)
    {
        computeStack(multiANewArrayInsn(descriptor, dimension));

        super.visitMultiANewArrayInsn(descriptor, dimension);
    }

    private static InstructionNode multiANewArrayInsn(String descriptor, int dimension)
    {
        return new InstructionNode(new MultiANewArrayInsnNode(descriptor, dimension));
    }

    @Override
    public void visitLabel(Label label)
    {
        // check if jump referenced label
        SRFBlockNode nextBlockNode;
        if ((nextBlockNode = labelBlockMap.get(label)) != null)
        {
            SRFBlock currentBlock = currentBlockNode.getFlowBlock();

            if (currentBlock.isEmpty())
            {
                // do nothing if the current block is empty
                return;
            }

            // check stack, append stack blank node if not empty
            if (!stack.isEmpty())
                consumeStack(stack.size(), new StackBlankNode(), false);

            finishBlock();

            // link new block after the current block
            if (!GraphHelper.append(currentBlockNode, nextBlockNode))
                throw new IllegalStateException("SRFGraph block linkage failure");

            // transfer the construction process to the new block
            currentBlockNode = nextBlockNode;
        }

        super.visitLabel(label);
    }

    public @Nonnull SRFGraph construct()
    {
        finishBlock();

        return new SRFGraph(firstBlockNode);
    }

    private void finishBlock()
    {
        SRFBlock currentBlock = currentBlockNode.getFlowBlock();

        // add all roots not merged into the block
        List<SRF> assuredSRF = currentBlock.getSRFs();
        for (Vector3<SRFNode, Boolean, EscapeInsertion> srfRoot : srfRoots)
            if (srfRoot.second())
            {
                SRF srf = new SRF(srfRoot.first());

                // insert escape node or attribution for assured SRF
                srfRoot.third().insert(srf);

                assuredSRF.add(srf);
            }

        // finish the construction stage of current block

        // end of previous block workflow
        stack.clear();
        srfRoots.clear();
    }

    public @Nullable SRFNode getLastNode()
    {
        return lastNode;
    }

    public @Nonnull VisitMeta getVisitMeta()
    {
        return visitMeta;
    }

    private void computeStack(InstructionNode source)
    {
        int opcode = source.getNode().getOpcode();

        Pair<Operator[], Operator[]> computation = StackComputation.get(opcode);

        if (computation == null)
            throw new IllegalArgumentException("Unsupported instruction: " + Integer.toHexString(opcode));

        Operator[] consumption = computation.first();
        Operator[] production = computation.second();

        Iterator<SRFStackElement> stackIterator = stack.iterator();

        // sx parameters (stored in local)
        StackElementType sx0_0 = null, sx0_1 = null;
        StackElementType sx1_0 = null, sx1_1 = null;

        // method descriptor
        MethodDescriptorIterator descIterator = null;

        // stack consumption computation & verification
        int consumptionDepth = 0;
        for (Operator op : consumption)
        {
            SRFStackElement top = next(stackIterator, source);
            StackElementType topType = top.getType();

            switch (op)
            {
                case AR:
                case B:
                case C:
                case D:
                case F:
                case I:
                case J:
                case L:
                case S:
                case Z:
                    if (!top.toOperator().equals(op))
                        throw stackTypeIncompatibility(source, op, topType);

                    consumptionDepth++;

                    break;

                case SX1:
                    // only type category 1 is allowed under SX1
                    if (topType.getCategory() != 1)
                        throw stackCategoryIncompatibility(source, 1, topType.getCategory());

                    if (sx0_0 == null)
                        sx0_0 = topType;
                    else if (sx1_0 == null)
                        sx1_0 = topType;
                    else
                        throw new IllegalStateException("SX operator overflow");

                    consumptionDepth++;

                    break;

                case SX2:
                    if (topType.getCategory() == 1)
                    {
                        SRFStackElement second = next(stackIterator, source);
                        StackElementType secondType = second.getType();

                        if (secondType.getCategory() != -1)
                            throw stackCategoryIncompatibility(source, 1, secondType.getCategory());

                        if (sx0_0 == null)
                        {
                            sx0_0 = topType;
                            sx0_1 = secondType;
                        }
                        else if (sx1_0 == null)
                        {
                            sx1_0 = topType;
                            sx1_1 = secondType;
                        }
                        else
                            throw new IllegalStateException("SX operator overflow");

                        consumptionDepth += 2;
                    }
                    else if (topType.getCategory() == 2)
                    {
                        if (sx0_0 == null)
                            sx0_0 = topType;
                        else if (sx1_0 == null)
                            sx1_0 = topType;
                        else
                            throw new IllegalStateException("SX operator overflow");

                        consumptionDepth++;
                    }
                    else
                        throw new ShouldNotReachHere();

                    break;

                case PR1:
                    if (opcode == PUTSTATIC || opcode == PUTFIELD)
                    {
                        FieldInsnNode fieldInsnNode = (FieldInsnNode) source.getNode();
                        StackElementType required = StackElementType.from(fieldInsnNode.desc);

                        if (required == null)
                            throw new IllegalStateException("Corrupt field descriptor: " + fieldInsnNode.desc);

                        if (!required.equals(topType))
                            throw stackTypeIncompatibility(source, required, topType);

                        consumptionDepth++;
                    }
                    else
                        throw new IllegalStateException("PR1 operator violation in " + Integer.toHexString(opcode));

                    break;

                case VPR:
                    if (opcode == MULTIANEWARRAY)
                    {
                        MultiANewArrayInsnNode insnNode = (MultiANewArrayInsnNode) source.getNode();

                        if (!StackElementType.INT.equals(top.getType()))
                            throw stackTypeIncompatibility(source, StackElementType.INT, top.getType());

                        consumptionDepth++;

                        int dims = insnNode.dims;
                        for (int i = 1; i < dims; i++)
                        {
                            StackElementType type = next(stackIterator, source).getType();

                            if (!StackElementType.INT.equals(type))
                                throw stackTypeIncompatibility(source, StackElementType.INT, type);

                            consumptionDepth++;
                        }
                    }
                    else if (opcode == INVOKEVIRTUAL || opcode == INVOKESPECIAL
                         ||  opcode == INVOKESTATIC  || opcode == INVOKEINTERFACE
                         ||  opcode == INVOKEDYNAMIC)
                    {
                        MethodInsnNode insnNode = (MethodInsnNode) source.getNode();
                        descIterator = new MethodDescriptorIterator(insnNode.desc);

                        descIterator.complete();

                        String[] arguments = descIterator.getArguments();
                        int argumentCount = arguments.length;

                        // break if zero parameter
                        if (argumentCount == 0)
                            break;

                        StackElementType[] argumentTypes = new StackElementType[argumentCount];

                        // convert descriptors into stack element types
                        for (int i = 0; i < arguments.length; i++)
                            if ((argumentTypes[i] = StackElementType.from(arguments[i])) == null)
                                throw new IllegalStateException("Corrupt method parameter descriptor: " + arguments[i]);

                        // due to LIFO stack, top element is computed last,
                        // so the stack element verification starts from the last parameter
                        if (!argumentTypes[argumentCount - 1].equals(topType))
                            throw stackTypeIncompatibility(source, argumentTypes[argumentCount - 1], topType);

                        consumptionDepth++;

                        for (int i = 1; i < argumentCount; i++)
                        {
                            StackElementType type = next(stackIterator, source).getType();

                            if (!argumentTypes[argumentCount - 1 - i].equals(type))
                                throw stackTypeIncompatibility(source, argumentTypes[argumentCount - 1 - i], type);

                            consumptionDepth++;
                        }
                    }
                    else
                        throw new IllegalStateException("VPR operator violation in " + Integer.toHexString(opcode));

                    break;

                case X0:
                case X1:
                    throw new IllegalStateException(op.name() + " operator not allowed in consumption");

                default:
                    throw new ShouldNotReachHere();
            }
        }

        int currentSRF = consumeStack(consumptionDepth, source, true);

        // stack production computation
        ArrayList<StackElementType> types = new ArrayList<>(production.length);
        for (Operator op : production)
        {
            switch (op)
            {
                case AR:
                case B:
                case C:
                case D:
                case F:
                case I:
                case J:
                case L:
                case S:
                case Z:
                    types.add(StackElementType.from(op));

                    break;

                case X0:
                    if (sx0_0 == null)
                        throw new IllegalStateException("X0 operator mismatch");

                    types.add(sx0_0);

                    if (sx0_1 != null)
                        types.add(sx0_1);

                    break;

                case X1:
                    if (sx1_0 == null)
                        throw new IllegalStateException("X1 operator mismatch");

                    types.add(sx1_0);

                    if (sx1_1 != null)
                        types.add(sx1_1);

                    break;

                case PR1:
                    if (opcode == LDC)
                    {
                        LdcInsnNode insnNode = (LdcInsnNode) source.getNode();
                        Object cst = insnNode.cst;

                        if (cst == null)
                            throw new IllegalStateException("empty load constant");

                        if (cst instanceof Integer)
                            types.add(StackElementType.INT);
                        else if (cst instanceof Float)
                            types.add(StackElementType.FLOAT);
                        else if (cst instanceof Long)
                            types.add(StackElementType.LONG);
                        else if (cst instanceof Double)
                            types.add(StackElementType.DOUBLE);
                        else if (cst instanceof String)
                            types.add(StackElementType.REFERENCE);
                        else if (cst instanceof Type)
                            types.add(StackElementType.REFERENCE);
                        else if (cst instanceof Handle)
                            types.add(StackElementType.REFERENCE);
                        else if (cst instanceof ConstantDynamic)
                            types.add(StackElementType.REFERENCE);
                        else
                            throw new IllegalStateException("illegal type in load constant: " + cst.getClass());
                    }
                    else if (opcode == GETSTATIC || opcode == GETFIELD)
                    {
                        FieldInsnNode insnNode = (FieldInsnNode) source.getNode();
                        StackElementType field = StackElementType.from(insnNode.desc);

                        if (field == null)
                            throw new IllegalStateException("Corrupt field descriptor: " + insnNode.desc);

                        types.add(field);
                    }
                    else if (opcode >= 0xB6 && opcode <= 0xBA)
                        // invokevirtual, invokespecial, invokestatic,
                        // invokeinterface, invokedynamic
                    {
                        if (descIterator == null)
                            throw new ShouldNotReachHere();

                        String returnDesc = descIterator.getReturnType();

                        if (returnDesc.equals("V"))
                            break;

                        StackElementType returnType = StackElementType.from(returnDesc);

                        if (returnType == null)
                            throw new IllegalStateException("Corrupt method descriptor: " + descIterator.getDescriptor());

                        types.add(returnType);
                    }
                    else
                        throw new IllegalStateException("PR1 operator violation in " + Integer.toHexString(opcode));

                    break;

                case VPR:
                case SX1:
                case SX2:
                    throw new IllegalStateException(op.name() + " operator now allowed in production");

                default:
                    throw new ShouldNotReachHere();
            }
        }

        pushStack(source, currentSRF, types);
    }

    private static SRFStackElement next(Iterator<SRFStackElement> stackIterator,
                                        SRFNode source)
    {
        if (!stackIterator.hasNext())
            throw stackUnderflow(source);

        return stackIterator.next();
    }

    private int nextSRF()
    {
        return srfRoots.size();
    }

    private void pushStack(SRFNode source, StackElementType... types)
    {
        pushStack(source, nextSRF(), types);
    }

    private void pushStack(SRFNode source, int currentSRF, StackElementType... types)
    {
        pushStack(source, currentSRF, Arrays.asList(types));
    }

    private void pushStack(SRFNode source, int currentSRF, List<StackElementType> types)
    {
        // *Note: A node without any on-stack connectivity with other node (when count == 0)
        //        will immediately establish a settled SRF.
        //
        //        And the establishing and settling process is implicitly finished when
        //        pushing a new node into the SRFG because if no elements originated from a
        //        SRF was pushed onto stack, that SRF would never be merged.

        int count = types.size();

        // pushing a head node, establish as an unsettled SRF root
        if (source.getManipulator().getUpperPathCount() == 0)
            // establish a new SRF root, may be merged in future
            srfRoots.add(Vector3.of(source, Boolean.TRUE,
                    // check if first node or terminal escape
                    currentSRF == 0 ? NON_ESCAPE_INSERTION // SRF Root [0]
                            : stack.isEmpty() ? new TerminalEscapeInsertion(lastNode) // empty stack -> terminal escape
                            : new ConnectiveEscapeInsertion(stack.peek())));

        for (int i = 0; i < count; i++)  // LIFO
            stack.push(new SRFStackElement(currentSRF, source, types.get(count - 1 - i)));

        lastNode = source;
    }

    // return current SRF
    private int consumeStack(int count, SRFNode node, boolean pop)
    {
        // zero consumption means establishing a new SRF
        if (count == 0)
            return nextSRF();

        if (count > stack.size())
            throw new IllegalStateException("Stack underflow");

        Iterator<SRFStackElement> stackIterator = stack.iterator();

        // peek first stack element to check for SRF mergence
        SRFStackElement firstElement = stackIterator.next();
        int mergence = firstElement.getSourceSRF();

        // link the first node if needed
        if (node != null && !GraphHelper.push(firstElement.getUpperNode(), node))
            throw new IllegalStateException("SRFGraph node linkage failure");

        if (count != 1) // no need to merge SRF if count = 1
        {
            // link the node after the source node of consumed stack element
            // and all differenct SRF will be merged to the first SRF
            int i = 1;
            while (i++ < count) // stack depth has been checked already, no need to call hasNext()
            {
                SRFStackElement stackElement = stackIterator.next();

                // link node if needed
                if (node != null && !GraphHelper.push(stackElement.getUpperNode(), node))
                    throw new IllegalStateException("SRFGraph node linkage failure");

                // check SRF and merge if needed
                int sourceSRF;
                if ((sourceSRF = stackElement.getSourceSRF()) != mergence) {
                    srfRoots.get(sourceSRF).second(Boolean.FALSE);

                    if (!pop)
                        stackElement.setSourceSRF(mergence);
                }
            }
        }

        if (pop)
            for (int j = 0; j < count; j++)
                stackIterator.remove();

        if (node != null)
            lastNode = node;

        return mergence;
    }

    private static StackComputationException attach(StackComputationException dst,
                                                    SRFNode src)
    {
        dst.getAttachments()
                .putAttachment(StackComputationException.ATTACHMENT_SOURCE_SRFG_NODE, src);

        return dst;
    }

    private static StackComputationException stackCategoryIncompatibility(SRFNode source,
                                                                          int expectedCategory,
                                                                          int foundCategory)
    {
        StackComputationException exception = StackComputationException.categoryIncompatibility(
                expectedCategory,
                foundCategory);

        return source != null ? attach(exception, source) : exception;
    }

    private static StackComputationException stackTypeIncompatibility(SRFNode source,
                                                                      Operator operator,
                                                                      StackElementType found)
    {
        return stackTypeIncompatibility(source, Objects.requireNonNull(StackElementType.from(operator),
                "Illegal computation operator on stack"), found);
    }

    private static StackComputationException stackTypeIncompatibility(SRFNode source,
                                                                      StackElementType expected,
                                                                      StackElementType found)
    {
        StackComputationException exception = StackComputationException.typeIncompatibility(
                expected,
                found);

        return source != null ? attach(exception, source) : exception;
    }

    private static StackComputationException stackUnderflow(SRFNode source)
    {
        StackComputationException exception
                = StackComputationException.underflow();

        return source != null ? attach(exception, source) : exception;
    }

    private final VisitMeta visitMeta;

    private final Map<Label, SRFBlockNode> labelBlockMap = new HashMap<>();


    // Block workflow

    private final LinkedList<SRFStackElement> stack = new LinkedList<>();

    private SRFBlockNode currentBlockNode;

    private final SRFBlockNode firstBlockNode;

    // Field declaration for vector3:
    //  #1 SRFNode         : SRF Root node
    //  #2 Boolean         : Enabled if true, otherwise merged
    //  #3 EscapeInsertion : Escape node or attribute insertion processor
    private List<Vector3<SRFNode, Boolean, EscapeInsertion>> srfRoots = new ArrayList<>();


    // SRF workflow

    private SRFNode lastNode;


    private static final EscapeInsertion NON_ESCAPE_INSERTION = (unused) -> {};

    private interface EscapeInsertion
    {
        public void insert(@Nonnull SRF target);
    }

    private static class TerminalEscapeInsertion implements EscapeInsertion
    {
        TerminalEscapeInsertion(SRFNode terminalNode)
        {
            this.terminalNode = terminalNode;
        }

        @Override
        public void insert(@Nonnull SRF target)
        {
            if (!terminalNode.isTerminal())
                throw new IllegalStateException("Terminal escape attached on non-terminal node");

            terminalNode.setEscapeTarget(target);
        }

        private final SRFNode terminalNode;
    }

    private static class ConnectiveEscapeInsertion implements EscapeInsertion
    {
        ConnectiveEscapeInsertion(SRFStackElement top)
        {
            this.top = top;
        }

        @Override
        public void insert(@Nonnull SRF target)
        {
            SRFNode node = top.getUpperNode();
            GraphNodeManipulator<SRFNode> manipulator = node.getManipulator();

            if (manipulator.getLowerPathCount() == 0)
            {
                // encountering terminal node
                // might be on-stack malformation but no need to fix it

                node.setEscapeTarget(target);

                return;
            }

            // insert the escape node to the top-stack path
            GraphHelper.insertAndPush(manipulator.getLowerPath(0) , new EscapeNode(target));
        }

        private final SRFStackElement top;
    }
}
