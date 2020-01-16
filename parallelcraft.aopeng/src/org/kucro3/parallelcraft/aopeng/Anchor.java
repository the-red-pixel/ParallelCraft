package org.kucro3.parallelcraft.aopeng;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记AOP锚点
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Anchor {
    /**
     * AOP锚点名称
     *
     * @return 名称
     */
    public String value();

    /**
     * AOP锚点的专用方法。<br>
     * 在指定了专用方法之后，方可使用域穿透相关的功能（如 {@link Shadow}, {@link Promote}）。
     *
     * @return 专用方法信息
     */
    public For dedicated() default @For(type = PlaceHolder.class, name = "", params = {});

    /**
     * 用于表示AOP锚点的专用方法的信息
     */
    public @interface For
    {
        /**
         * 指定方法所在的类
         *
         * @return 类
         */
        public Class<?> type();

        /**
         * 指定方法的名称
         *
         * @return 名称
         */
        public String name();

        /**
         * 指定方法的参数
         *
         * @return 参数列表
         */
        public Class<?>[] params();
    }
}
