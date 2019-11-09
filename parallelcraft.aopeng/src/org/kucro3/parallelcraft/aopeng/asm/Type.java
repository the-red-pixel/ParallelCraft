package org.kucro3.parallelcraft.aopeng.asm;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class Type {
    Type(String descriptor)
    {
        setDescriptor(descriptor);
    }

    public static @Nonnull Type getTypeFromDescriptor(@Nonnull String descriptor)
    {
        return new Type(descriptor);
    }

    public @Nonnull String getDescriptor()
    {
        return descriptor;
    }

    public void setDescriptor(@Nonnull String descriptor)
    {
        this.descriptor = Objects.requireNonNull(descriptor);

        if (descriptor.isBlank())
            throw new IllegalArgumentException("empty descriptor");

        this.internalName = null;
        this.canonicalName = null;

        this.tag = getTag0();
    }

    public @Nullable String getInternalName()
    {
        if (internalName == null && Tag.REFERENCE.equals(tag))
            internalName = descriptor.substring(1, descriptor.length() - 1);

        return internalName;
    }

    public @Nullable String getCanonicalName()
    {
        if (canonicalName == null &&
                (Tag.REFERENCE.equals(tag) || Tag.ARRAY_REFERENCE.equals(tag)))
        {
            if (dimension == 0)
                return canonicalName = descriptor.substring(1, descriptor.length() - 1).replace('/', '.');
            else
            {
                String baseDec = descriptor.substring(dimension);
                Tag tag = Tag.getTag(baseDec.charAt(0));

                if (tag == null)
                    throw new IllegalArgumentException("illegal array descriptor: " + descriptor);

                String tagDefaultBase = tag.getDefaultCanonical();

                String stufix = "[]".repeat(dimension);
                String base = tagDefaultBase != null
                        ? tagDefaultBase
                        : descriptor.substring(1, descriptor.length() - 1).replace('/', '.');

                return canonicalName = base + stufix;
            }
        }

        return canonicalName;
    }

    public @Nonnull Tag getTag()
    {
        return tag;
    }

    private Tag getTag0()
    {
        char prefix = descriptor.charAt(0);

        Tag tag = Tag.getTag(prefix);

        if (tag == null)
            throw new IllegalArgumentException("unknown descriptor prefix: " + prefix);

        if (Tag.ARRAY_REFERENCE.equals(tag))
        {
            int i = 1;
            while (descriptor.charAt(i) == '[')
                i++;

            this.dimension = i;
        }
        else
            this.dimension = 0;

        return this.tag = tag;
    }

    public @Nonnegative int getDimension()
    {
        return dimension;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(descriptor);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Type))
            return false;

        Type object = (Type) obj;

        return object.descriptor.equals(descriptor);
    }

    private String descriptor;

    private String internalName;

    private String canonicalName;

    private int dimension;

    private Tag tag;

    public enum Tag
    {
        ARRAY_REFERENCE ('[', null),
        REFERENCE       ('L', null),
        BOOLEAN         ('Z', "boolean"),
        BYTE            ('B', "byte"),
        CHAR            ('C', "char"),
        DOUBLE          ('D', "double"),
        FLOAT           ('F', "float"),
        SHORT           ('S', "short"),
        INT             ('I', "int"),
        LONG            ('J', "long"),
        VOID            ('V', "void");

        private Tag(char prefix, String defaultCanonical)
        {
            this.prefix = prefix;
            this.defaultCanonical = defaultCanonical;

            reg(this);
        }

        public char getPrefix()
        {
            return prefix;
        }

        private static void reg(Tag tag)
        {
            if (TAGS == null)
                TAGS = new Tag['[' + 1];

            TAGS[tag.getPrefix()] = tag;
        }

        public static @Nullable Tag getTag(char prefix)
        {
            if (prefix > '[')
                return null;

            return TAGS[prefix];
        }

        public @Nullable String getDefaultCanonical()
        {
            return defaultCanonical;
        }

        private final char prefix;

        private final String defaultCanonical;

        private static Tag[] TAGS;
    }
}
