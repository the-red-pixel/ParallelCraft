package org.kucro3.parallelcraft.aopeng.asm.graph.SRFGv1.node.insn;

import org.objectweb.asm.Handle;

import javax.annotation.Nonnull;
import java.util.Objects;

public class InvokeDynamicInstructionNode extends InstructionNode {
    public InvokeDynamicInstructionNode(int opcode,
                                        @Nonnull String name,
                                        @Nonnull String descriptor,
                                        @Nonnull Handle bootstrapMethod,
                                        @Nonnull Object... bootstrapArguments)
    {
        super(opcode, INVOKE_DYNAMIC_INSN);

        this.name = Objects.requireNonNull(name, "name");
        this.descriptor = Objects.requireNonNull(descriptor, "descriptor");
        this.bootstrapMethod = Objects.requireNonNull(bootstrapMethod, "bootstrapMethod");
        this.bootstrapArguments = bootstrapArguments;
    }

    public void setBootstrapMethod(@Nonnull Handle bootstrapMethod)
    {
        this.bootstrapMethod = Objects.requireNonNull(bootstrapMethod);
    }

    public void setBootstrapArguments(@Nonnull Object... bootstrapArguments)
    {
        this.bootstrapArguments = bootstrapArguments;
    }

    public void setDescriptor(@Nonnull String descriptor)
    {
        this.descriptor = Objects.requireNonNull(descriptor);
    }

    public void setName(@Nonnull String name)
    {
        this.name = Objects.requireNonNull(name);
    }

    public @Nonnull Handle getBootstrapMethod()
    {
        return bootstrapMethod;
    }

    public @Nonnull Object[] getBootstrapArguments()
    {
        return bootstrapArguments;
    }

    public @Nonnull String getDescriptor()
    {
        return descriptor;
    }

    public @Nonnull String getName()
    {
        return name;
    }

    private Handle bootstrapMethod;

    private Object[] bootstrapArguments;

    private String descriptor;

    private String name;
}
