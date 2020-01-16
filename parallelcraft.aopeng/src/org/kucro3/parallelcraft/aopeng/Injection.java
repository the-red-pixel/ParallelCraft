package org.kucro3.parallelcraft.aopeng;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记AOP注入
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Injection {
    /**
     * 注入名称，必须与其他的注入不同
     *
     * @return 名称
     */
    public String name();

    /**
     * 注入的目标锚点名称
     *
     * @return 目标锚点名称
     */
    public String target();

    /**
     * 标记注入在某些注入之后，这些注入不必须存在。
     *
     * @return 注入名称表
     */
    public String[] after() default {};

    /**
     * 标记注入依赖。这些注入必须存在，否则此注入将不发挥作用。
     *
     * @return 注入依赖名称表
     */
    public String[] dependencies() default {};
}
