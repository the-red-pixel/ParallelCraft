package org.kucro3.parallelcraft.aopeng.asm;

import org.objectweb.asm.Type;
import org.objectweb.asm.*;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.function.Function;

public final class Jam2Util extends ClassLoader implements Opcodes {
	private Jam2Util()
	{
		super(getProvidedClassLoader());
	}
	
	public static Jam2Util getInstance()
	{
		return INSTANCE;
	}
	
	public static ClassLoader getProvidedClassLoader()
	{
		return Jam2Util.class.getClassLoader();
	}
	
	public static Class<?> newClass(String name, ClassWriter writer)
	{
		return newClass(name, writer.toByteArray());
	}
	
	public static Class<?> newClass(String name, byte[] byts)
	{
		return newClass(name, byts, 0, byts.length);
	}
	
	public static Class<?> newClass(String name, byte[] byts, int off, int len)
	{
		return INSTANCE.defineClass(name, byts, off, len);
	}

	public static Class<?> newClass(String name, byte[] byts, int off, int len, ProtectionDomain domain)
	{
		return INSTANCE.defineClass(name, byts, off, len, domain);
	}

	public static String generateUUIDForClassName()
	{
		return UUID.randomUUID().toString().replace('-', '_');
	}
	
	public static void pushEmptyConstructor(ClassVisitor cw, int modifiers, Class<?> superClass)
	{
		pushEmptyConstructor(cw, modifiers, Type.getInternalName(superClass));
	}

	public static void pushEmptyConstructor(ClassVisitor cw, int modifiers, String superClass)
	{
		MethodVisitor mv = __init__(cw, modifiers, "()V", null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, superClass,
				"<init>", "()V", false);
		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	public static boolean pushBoxing(MethodVisitor mv, Class<?> type)
	{
		if(!type.isPrimitive())
			return false;
		return pushBoxing(mv, Type.getDescriptor(type));
	}

	public static boolean pushBoxing(MethodVisitor mv, String descriptor)
	{
		return _pushBoxingOperation$(mv, descriptor, -1, LAMBDA_PUSHBOXINGOPERATION_$_0);
	}

	public static boolean pushUnboxing(MethodVisitor mv, Class<?> type)
	{
		if(!type.isPrimitive())
			return false;
		return pushUnboxing(mv, Type.getDescriptor(type));
	}

	public static boolean pushUnboxing(MethodVisitor mv, String descriptor)
	{
		return _pushBoxingOperation$(mv, descriptor, -1, LAMBDA_PUSHBOXINGOPERATION_$_1);
	}

	private static boolean _pushBoxingOperation$(MethodVisitor mv, String descriptor, int index, LAMBDA_PUSHBOXINGOPERATION_$0[] lambda)
	{
		if(index < 0)
		{
			Integer objIndex = MAPPED_DESCRIPTOR_INDEX.get(descriptor);
			if(objIndex == null)
				return false;
			index = objIndex.intValue();
		}
		LAMBDA_PUSHBOXINGOPERATION_$0 L0 = lambda[index];
		if(L0 == null)
			return false;
		L0.function(mv);
		return true;
	}

	public static void pushBoxingBoolean(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
	}

	public static void pushBoxingByte(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
	}

	public static void pushBoxingCharacter(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
	}

	public static void pushBoxingShort(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
	}

	public static void pushBoxingInteger(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
	}

	public static void pushBoxingFloat(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
	}

	public static void pushBoxingLong(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
	}

	public static void pushBoxingDouble(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
	}

	public static void pushUnboxingBoolean(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
	}

	public static void pushUnboxingByte(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B" ,false);
	}

	public static void pushUnboxingCharacter(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
	}

	public static void pushUnboxingShort(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
	}

	public static void pushUnboxingInteger(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
	}

	public static void pushUnboxingFloat(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
	}

	public static void pushUnboxingLong(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
	}

	public static void pushUnboxingDouble(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
	}

	public static MethodVisitor newFinalize(ClassVisitor cw)
	{
		return _newMethod(cw, ACC_PROTECTED, "finalize", void.class, null, null);
	}

	public static MethodVisitor newConstructor(ClassVisitor cw, int modifiers, Class<?>[] arguments,
                                               String... throwings)
	{
		return __init__(cw, modifiers, _toDescriptor(void.class, arguments), throwings);
	}

	public static MethodVisitor newConstructor(ClassVisitor cw, int modifiers, String[] arguments,
                                               String... throwings)
	{
		return __init__(cw, modifiers, _toDescriptor(DESCRIPTOR_VOID, arguments), throwings);
	}

	public static MethodVisitor newStaticBlock(ClassVisitor cw)
	{
		return __clinit__(cw);
	}

	public static MethodVisitor newMethod(ClassVisitor cw, int modifiers, String name, Class<?> returnType,
                                          Class<?>[] arguments, String... throwings)
	{
		return _newMethod(cw, modifiers, name, returnType, arguments, throwings);
	}

	public static MethodVisitor newMethod(ClassVisitor cw, int modifiers, String name, String returnType,
                                          String[] arguments, String... throwings)
	{
		return _newMethod(cw, modifiers, name, returnType, arguments, throwings);
	}

	public static FieldVisitor newField(ClassVisitor cw, int modifiers, String name, Class<?> type)
	{
		return _newField(cw, modifiers, name, Type.getDescriptor(type));
	}

	public static FieldVisitor newField(ClassVisitor cw, int modifiers, String name, String type)
	{
		return _newField(cw, modifiers, name, type);
	}

	static MethodVisitor _newMethod(ClassVisitor cw, int modifiers, String name, String descriptor,
                                    String[] throwing)
	{
		return cw.visitMethod(modifiers, name, descriptor, null, throwing);
	}

	static MethodVisitor _newMethod(ClassVisitor cw, int modifiers, String name, String returnType,
                                    String[] arguments, String[] throwing)
	{
		return cw.visitMethod(modifiers, name, _toDescriptor(returnType, arguments, ""), null, throwing);
	}

	static MethodVisitor _newMethod(ClassVisitor cw, int modifiers, String name, String returnType,
                                    String argument, String[] throwing)
	{
		return cw.visitMethod(modifiers, name, "(" + (argument == null ? "" : argument) + ")" + returnType, null, throwing);
	}

	static MethodVisitor _newMethod(ClassVisitor cw, int modifiers, String name, Class<?> returnType,
                                    Class<?>[] arguments, String[] throwing)
	{
		return _newMethod(cw, modifiers, name, _toDescriptor(returnType, arguments, ""), throwing);
	}

	static FieldVisitor _newField(ClassVisitor cw, int modifiers, String name, String type)
	{
		return cw.visitField(modifiers, name, type, null, null);
	}

	static String _toDescriptor(Class<?> returnType, Class<?>[] arguments, String prefix)
	{
		return _toDescriptor$(returnType, arguments, prefix, (obj) -> Type.getDescriptor((Class<?>)obj));
	}

	static String _toDescriptor(Class<?> returnType, Class<?>[] arguments)
	{
		return _toDescriptor(returnType, arguments, "");
	}

	static String _toDescriptor(String returnType, String[] arguments, String prefix)
	{
		return _toDescriptor$(returnType, arguments, prefix, (obj) -> (String)obj);
	}

	static String _toDescriptor(String returnType, String[] arguments)
	{
		return _toDescriptor(returnType, arguments, "");
	}

	static String _toConstructorDescriptor(Class<?>[] arguments)
	{
		return _toDescriptor(void.class, arguments);
	}

	static String _toConstructorDescriptor(String[] arguments)
	{
		return _toDescriptor(DESCRIPTOR_VOID, arguments);
	}

	private static String _toDescriptor$(Object returnType, Object[] arguments, String prefix, LAMBDA__TODESCRIPTOR_$0 lambda0)
	{
		Objects.requireNonNull(returnType);

		StringBuilder sb = new StringBuilder("(");
		sb.append(prefix);
		if(arguments != null)
			for(int i = 0; i < arguments.length; i++)
				sb.append(lambda0.function(arguments[i]));
		sb.append(")");

		return sb.append(lambda0.function(returnType)).toString();
	}

	static int _forReturnInsn(Class<?> type)
	{
		return _forTypeDependingInsn(type, TYPE_DEPENDING_INSN_INDEX_RETURN);
	}

	static int _forLocalLoadInsn(Class<?> type)
	{
		return _forTypeDependingInsn(type, TYPE_DEPENDING_INSN_INDEX_LOCAL_LOAD);
	}

	static int _forReturnInsn(String desc)
	{
		return _forTypeDependingInsn(desc, TYPE_DEPENDING_INSN_INDEX_RETURN);
	}

	static int _forLocalLoadInsn(String desc)
	{
		return _forTypeDependingInsn(desc, TYPE_DEPENDING_INSN_INDEX_LOCAL_LOAD);
	}

	static int _forTypeDependingInsn(Class<?> type, int index)
	{
		if(type.isPrimitive())
			return _forTypeDependingInsn(Type.getDescriptor(type), index);
		return TYPE_DEPENDING_INSN_CONST[0][index];
	}

	static int _forTypeDependingInsn(String desc, int index)
	{
		switch(_toSymbol(desc))
		{
		case DESCRIPTOR_VOID:
			return TYPE_DEPENDING_INSN_CONST[1][index];
		case DESCRIPTOR_BOOLEAN:
		case DESCRIPTOR_CHAR:
		case DESCRIPTOR_SHORT:
		case DESCRIPTOR_BYTE:
		case DESCRIPTOR_INT:
			return TYPE_DEPENDING_INSN_CONST[2][index];
		case DESCRIPTOR_FLOAT:
			return TYPE_DEPENDING_INSN_CONST[3][index];
		case DESCRIPTOR_LONG:
			return TYPE_DEPENDING_INSN_CONST[4][index];
		case DESCRIPTOR_DOUBLE:
			return TYPE_DEPENDING_INSN_CONST[5][index];
		case DESCRIPTOR_SYMBOL_ARRAY:
		case DESCRIPTOR_SYMBOL_OBJECT:
			return TYPE_DEPENDING_INSN_CONST[0][index];
		default:
			throw new IllegalStateException();
		}
	}

	static String _nextDescriptor(String descriptor, int[] currentIndex)
	{
		char c;
		while(currentIndex[0] < descriptor.length())
			switch(c = descriptor.charAt(currentIndex[0]))
			{
			case '(':
			case ')':
				currentIndex[0]++;
				break;
			case 'L':
			case '[':
				int start = currentIndex[0];
				for(; currentIndex[0] < descriptor.length(); currentIndex[0]++)
					switch(c = descriptor.charAt(currentIndex[0]))
					{
					case '[':
						continue;
					case 'L':
						for(; currentIndex[0] < descriptor.length(); currentIndex[0]++)
							if(descriptor.charAt(currentIndex[0]) == ';')
								return descriptor.substring(start, ++currentIndex[0]);
					default:
						return descriptor.substring(start, ++currentIndex[0]);
					}
				throw new IllegalArgumentException("Uncompleted descriptor: " + descriptor);
			default:
				currentIndex[0]++;
				return String.valueOf(c);
			}
		return null;
	}

	static String _getReturnDescriptor(String descriptor)
	{
		int index = descriptor.lastIndexOf(')');
		if(index < 0)
			throw new IllegalArgumentException("Illegal descriptor: " + descriptor);
		return descriptor.substring(index + 1, descriptor.length());
	}

	static String _toSymbol(String descriptor)
	{
		return String.valueOf(descriptor.charAt(0));
	}

	static String _boxingMapped(String key)
	{
		return MAPPED_BOXING_OBJECT.get(key);
	}

	static String _tryBoxingMapped(String key)
	{
		String result = _boxingMapped(key);
		if(result == null)
			return key;
		return result;
	}

	static String _generateLambdaFunctionName(String arg)
	{
		if(arg != null)
			return new StringBuilder("lambda$").append(arg).toString();
		return new StringBuilder("lambda$").append(UUID.randomUUID().toString().replace("-", "_")).toString();
	}

	static String _namingJavaBean(String fieldName)
	{
		if(fieldName.length() == 0)
			throw new IllegalArgumentException("Name cannot be empty");

		char f = Character.toUpperCase(fieldName.charAt(0));
		return new StringBuilder()
				.append(f)
				.append(fieldName.length() > 1 ? fieldName.substring(1) : "")
				.toString();
	}

	static String _namingJavaBeanGetter(String namedJavaBean)
	{
		return new StringBuilder("get").append(namedJavaBean).toString();
	}

	static String _namingJavaBeanSetter(String namedJavaBean)
	{
		return new StringBuilder("set").append(namedJavaBean).toString();
	}

	static void _pushGetVoidObject(MethodVisitor mv)
	{
		mv.visitFieldInsn(GETSTATIC, "org/kucro3/jam2/util/Jam2Util", "RETURN_VOID", "Ljava/lang/Object;");
	}

	static String[] _toDescriptors(Class<?>[] classes)
	{
		if(classes == null)
			return new String[0];
		String[] descriptors = new String[classes.length];
		for(int i = 0; i < classes.length; i++)
			descriptors[i] = Type.getDescriptor(classes[i]);
		return descriptors;
	}

	public static String[] toDescriptors(Class<?>[] classes)
	{
		return _toDescriptors(classes);
	}

	public static String toDescriptor(String returnType, String[] arguments)
	{
		return toDescriptor(null, returnType, arguments);
	}

	public static String toDescriptor(String name, String returnType, String[] arguments)
	{
		return name == null ?
				_toDescriptor(returnType, arguments) :
				(name + _toDescriptor(returnType, arguments));
	}

	public static String toDescriptor(Class<?> type)
    {
        return Type.getDescriptor(type);
    }

	public static String toDescriptor(Class<?> returnType, Class<?>[] arguments)
	{
		return toDescriptor(null, returnType, arguments);
	}

	public static String toDescriptor(String name, Class<?> returnType, Class<?>[] arguments)
	{
		return name == null ?
				_toDescriptor(returnType, arguments) :
				(name + _toDescriptor(returnType, arguments));
	}

	public static String toDescriptor(Method mthd)
	{
		return toDescriptor(mthd.getName(), mthd.getReturnType(), mthd.getParameterTypes());
	}

	public static String toConstructorDescriptor(Constructor<?> constructor)
	{
		return toConstructorDescriptor(constructor.getParameterTypes());
	}

	public static String toConstructorDescriptor(Class<?>[] arguments)
	{
		return toDescriptor("", void.class, arguments);
	}

	public static String toConstructorFullDescriptor(Class<?>[] arguments)
	{
		return toDescriptor("<init>", void.class, arguments);
	}

	static String[] _toInternalNames(Class<?>[] classes)
	{
		if(classes == null)
			return new String[0];
		String[] names = new String[classes.length];
		for(int i = 0; i < classes.length; i++)
			names[i] = Type.getInternalName(classes[i]);
		return names;
	}

	public static String[] toInternalNames(Class<?>[] classes)
	{
		return _toInternalNames(classes);
	}

	static String[] _toCanonicalNames(Class<?>[] classes)
	{
		if(classes == null)
			return new String[0];
		String[] names = new String[classes.length];
		for(int i = 0; i < classes.length; i++)
			names[i] = classes[i].getCanonicalName();
		return names;
	}

	public static String[] toCanonicalNames(Class<?>[] classes)
	{
		return _toCanonicalNames(classes);
	}

	public static String toInternalName(Class<?> clazz)
	{
		return Type.getInternalName(clazz);
	}

	public static boolean isClassResource(String resource)
	{
		return resource.endsWith(".class");
	}

	public static String fromResourceToInternalName(String resource)
	{
		return resource.substring(0, resource.length() - 6);
	}

	public static String[] fromResourcesToInternalNames(String... resources)
	{
		return __stringProcess(resources, Jam2Util::fromResourceToInternalName);
	}

	public static String fromInternalNameToResource(String internalName)
	{
		return internalName + ".class";
	}

	public static String[] fromInternalNamesToResources(String... internalNames)
	{
		return __stringProcess(internalNames, Jam2Util::fromInternalNameToResource);
	}

	public static String fromInternalNameToDescriptor(String internalName)
	{
		return Type.getObjectType(internalName).getDescriptor();
	}

	public static String[] fromInternalNamesToDescriptors(String... internalNames)
	{
		return __stringProcess(internalNames, Jam2Util::fromInternalNameToDescriptor);
	}

	public static String fromDescriptorToInternalName(String desc)
	{
		return Type.getType(desc).getInternalName();
	}

	public static String[] fromDescriptorsToInternalNames(String... descriptors)
	{
		return __stringProcess(descriptors, Jam2Util::fromDescriptorToInternalName);
	}

	public static String fromDescriptorToCanonical(String desc)
	{
		return fromInternalNameToCanonical(fromDescriptorToInternalName(desc));
	}

	public static String[] fromDescriptorsToCanonicals(String... descriptors)
	{
		return __stringProcess(descriptors, Jam2Util::fromDescriptorToCanonical);
	}

	public static String fromCanonicalToInternalName(String name)
	{
		return name.replace('.', '/');
	}

	public static String[] fromCanonicalsToInternalNames(String... names)
	{
		return __stringProcess(names, Jam2Util::fromCanonicalToInternalName);
	}

	static String[] __stringProcess(String[] arr, Function<String, String> processor)
	{
		String[] ret = new String[arr.length];
		for(int i = 0; i < ret.length; i++)
			ret[i] = processor.apply(arr[i]);
		return ret;
	}

	public static String fromInternalNameToCanonical(String name)
	{
		return name.replace('/', '.');
	}

	public static Optional<Class<?>[]> tryFromCanoncialsToClasses(String... names)
	{
		return __tryToClasses(names, null);
	}

	public static Optional<Class<?>> tryFromCanoncialToClass(String name)
	{
		Optional<Class<?>[]> optional = tryFromCanoncialsToClasses(name);
		return optional.isPresent() ? Optional.of(optional.get()[0]) : Optional.empty();
	}

	public static Optional<Class<?>[]> tryFromInternalNamesToClasses(String... names)
	{
		return __tryToClasses(names, (internalName) -> Jam2Util.fromInternalNameToCanonical(internalName));
	}

	public static Optional<Class<?>> tryFromInternalNameToClass(String name)
	{
		Optional<Class<?>[]> optional = tryFromInternalNamesToClasses(name);
		return optional.isPresent() ? Optional.of(optional.get()[0]) : Optional.empty();
	}

	public static Optional<Class<?>[]> tryFromDescriptorsToClasses(String... names)
	{
		return __tryToClasses(names, (descriptor) -> Jam2Util.fromDescriptorToCanonical(descriptor));
	}

	public static Optional<Class<?>> tryFromDescriptorToClass(String name)
	{
		Optional<Class<?>[]> optional = tryFromDescriptorsToClasses(name);
		return optional.isPresent() ? Optional.of(optional.get()[0]) : Optional.empty();
	}

	private static Optional<Class<?>[]> __tryToClasses(String[] names, Function<String, String> func)
	{
		Class<?>[] classes = new Class<?>[names.length];

		try {
			for(int i = 0; i < classes.length; i++)
				classes[i] = Class.forName(func == null ? names[i] : func.apply(names[i]));
		} catch (ClassNotFoundException e) {
			return Optional.empty();
		}

		return Optional.of(classes);
	}

	static Type[] _toTypes(String[] descriptors)
	{
		Type[] types = new Type[descriptors.length];
		for(int i = 0; i < descriptors.length; i++)
			types[i] = Type.getType(descriptors[i]);
		return types;
	}

	static MethodVisitor __init__(ClassVisitor cw, int modifiers, String descriptor, String[] throwings)
	{
		return _newMethod(cw, modifiers, "<init>", descriptor, throwings);
	}

	static MethodVisitor __clinit__(ClassVisitor cw)
	{
		return _newMethod(cw, ACC_PUBLIC + ACC_STATIC, "<clinit>", _toDescriptor(void.class, null, ""), null);
	}

	@SuppressWarnings("deprecation")
	public Class<?> defClass(byte[] b, int off, int len)
	{
		return super.defineClass(b, off, len);
	}

	public Class<?> defClass(String name, byte[] b, int off, int len)
	{
		return super.defineClass(name, b, off, len);
	}

	public static Class<?> defClass(ClassLoader loader, byte[] b, int off, int len)
	{
		if(METHOD_CLASSLOADER_DEFINECLASS == null)
			throw new UnsupportedOperationException();

		try {
			return (Class<?>) METHOD_CLASSLOADER_DEFINECLASS.invoke(loader, b, off, len);
		} catch (Exception e) {
			throw new UnsupportedOperationException();
		}
	}

	public static Class<?> defClass(ClassLoader loader, String name, byte[] b, int off, int len)
	{
		if(METHOD_CLASSLOADER_DEFINECLASS_WITH_NAME == null)
			throw new UnsupportedOperationException();

		try {
			return (Class) METHOD_CLASSLOADER_DEFINECLASS_WITH_NAME.invoke(loader, name, b, off, len);
		} catch (Exception e) {
			throw new UnsupportedOperationException();
		}
	}

	private static final Jam2Util INSTANCE = new Jam2Util();

	private static final int[][] TYPE_DEPENDING_INSN_CONST = {
			{ARETURN, ALOAD},
			{RETURN},
			{IRETURN, ILOAD},
			{FRETURN, FLOAD},
			{LRETURN, LLOAD},
			{DRETURN, DLOAD}
	};

	private static final int TYPE_DEPENDING_INSN_INDEX_RETURN = 0;

	private static final int TYPE_DEPENDING_INSN_INDEX_LOCAL_LOAD = 1;

	private static final Method METHOD_CLASSLOADER_DEFINECLASS;

	private static final Method METHOD_CLASSLOADER_DEFINECLASS_WITH_NAME;

	static {
		// must not use org.kucro3.jam2.invoker here
		// it will cause a dead loop

		Method mthd = null;

		try {
			// defineClass(byte[], int, int)
			mthd = ClassLoader.class.getMethod("defineClass", byte[].class, int.class, int.class);
			mthd.setAccessible(true);
		} catch (Exception e) {
		} finally {
			METHOD_CLASSLOADER_DEFINECLASS = mthd;
		}

		try {
			// defineClass(String, byte[], int, int)
			mthd = ClassLoader.class.getMethod("defineClass", String.class, byte[].class, int.class, int.class);
			mthd.setAccessible(true);
		} catch (Exception e) {
		} finally {
			METHOD_CLASSLOADER_DEFINECLASS_WITH_NAME = mthd;
		}
	}

	private static interface LAMBDA__TODESCRIPTOR_$0
	{
		public String function(Object arg0);
	}

	private static final LAMBDA__TODESCRIPTOR_$0
			LAMBDA__TO_DESCRIPTOR_$0_0 = (obj) -> Type.getDescriptor((Class<?>)obj),
			LAMBDA__TO_DESCRIPTOR_$0_1 = (obj) -> (String)obj;

	private static interface LAMBDA_PUSHBOXINGOPERATION_$0
	{
		public void function(MethodVisitor mv);
	}

	private static final LAMBDA_PUSHBOXINGOPERATION_$0[]
			LAMBDA_PUSHBOXINGOPERATION_$_0 = {
				null,
				Jam2Util::pushBoxingBoolean,
				Jam2Util::pushBoxingCharacter,
				Jam2Util::pushBoxingByte,
				Jam2Util::pushBoxingShort,
				Jam2Util::pushBoxingInteger,
				Jam2Util::pushBoxingFloat,
				Jam2Util::pushBoxingLong,
				Jam2Util::pushBoxingDouble
		},
			LAMBDA_PUSHBOXINGOPERATION_$_1 = {
				null,
				Jam2Util::pushUnboxingBoolean,
				Jam2Util::pushUnboxingCharacter,
				Jam2Util::pushUnboxingByte,
				Jam2Util::pushUnboxingShort,
				Jam2Util::pushUnboxingInteger,
				Jam2Util::pushUnboxingFloat,
				Jam2Util::pushUnboxingLong,
				Jam2Util::pushUnboxingDouble
		};

	public static final Object RETURN_VOID = new Object();

	public static final String DESCRIPTOR_VOID = "V";

	public static final String DESCRIPTOR_BOOLEAN = "Z";

	public static final String DESCRIPTOR_CHAR = "C";

	public static final String DESCRIPTOR_SHORT = "S";

	public static final String DESCRIPTOR_BYTE = "B";

	public static final String DESCRIPTOR_INT = "I";

	public static final String DESCRIPTOR_FLOAT = "F";

	public static final String DESCRIPTOR_LONG = "J";

	public static final String DESCRIPTOR_DOUBLE = "D";

	public static final String DESCRIPTOR_SYMBOL_OBJECT = "L";

	public static final String DESCRIPTOR_SYMBOL_ARRAY = "[";

	private static final MethodType METHOD_TYPE_LAMBDA_METAFACTORY =
			MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class,
					MethodType.class, MethodHandle.class, MethodType.class);

	private static final String METHOD_DESCRIPTOR_LAMBDA_METAFACTORY = METHOD_TYPE_LAMBDA_METAFACTORY.toMethodDescriptorString();

	private static final Handle METHOD_HANDLE_LAMBDA_METAFACTORY =
			new Handle(H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", METHOD_DESCRIPTOR_LAMBDA_METAFACTORY);

	private static final Map<String, Integer> MAPPED_DESCRIPTOR_INDEX
			= new HashMap<String, Integer>() {
		/**
		 *
		 */
		private static final long serialVersionUID = -8847617886452962469L;

		{
			put(DESCRIPTOR_VOID, Type.VOID);
			put(DESCRIPTOR_BOOLEAN, Type.BOOLEAN);
			put(DESCRIPTOR_CHAR, Type.CHAR);
			put(DESCRIPTOR_SHORT, Type.SHORT);
			put(DESCRIPTOR_BYTE, Type.BYTE);
			put(DESCRIPTOR_INT, Type.INT);
			put(DESCRIPTOR_FLOAT, Type.FLOAT);
			put(DESCRIPTOR_LONG, Type.LONG);
			put(DESCRIPTOR_DOUBLE, Type.DOUBLE);
		}
	};
	
	private static final Map<String, String> MAPPED_BOXING_OBJECT
			= new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2982736764808443622L;

		{
			put(DESCRIPTOR_BOOLEAN, "java/lang/Boolean");
			put(DESCRIPTOR_CHAR, "java/lang/Character");
			put(DESCRIPTOR_SHORT, "java/lang/Short");
			put(DESCRIPTOR_BYTE, "java/lang/Byte");
			put(DESCRIPTOR_INT, "java/lang/Integer");
			put(DESCRIPTOR_FLOAT, "java/lang/Float");
			put(DESCRIPTOR_LONG, "java/lang/Long");
			put(DESCRIPTOR_DOUBLE, "java/lang/Double");
		}
	};
	
	public static enum CallingType
	{
		VIRTUAL(INVOKEVIRTUAL, false),
		INTERFACE(INVOKEINTERFACE, true),
		STATIC(INVOKESTATIC, false),
		SPECIAL(INVOKESPECIAL, false);
		
		private CallingType(int insn, boolean flag)
		{
			this.insn = insn;
			this.flag = flag;
		}
		
		final int getMethodInsn()
		{
			return insn;
		}
		
		final boolean getFlag()
		{
			return flag;
		}
		
		public static CallingType fromModifier(int modifiers)
		{
			if(Modifier.isStatic(modifiers))
				return STATIC;
			if(Modifier.isPrivate(modifiers))
				return SPECIAL;
			return VIRTUAL;
		}
		
		public static CallingType fromMethod(Method method)
		{
			if(method.getDeclaringClass().isInterface())
				return INTERFACE;
			return fromModifier(method.getModifiers());
		}
		
		private final boolean flag;
		
		private final int insn;
	}
	
	public static enum FieldType
	{
		STATIC(GETSTATIC, PUTSTATIC, true),
		NONSTATIC(GETFIELD, PUTFIELD, false);
		
		private FieldType(int get, int put, boolean flag)
		{
			this.get = get;
			this.put = put;
			this.flag = flag;
		}
		
		final int get()
		{
			return get;
		}
		
		final int put()
		{
			return put;
		}
		
		final boolean isStatic()
		{
			return flag;
		}
		
		public static FieldType fromModifier(int modifiers)
		{
			if(Modifier.isStatic(modifiers))
				return STATIC;
			return NONSTATIC;
		}
		
		private final boolean flag;
		
		private final int get;
		
		private final int put;
	}
}