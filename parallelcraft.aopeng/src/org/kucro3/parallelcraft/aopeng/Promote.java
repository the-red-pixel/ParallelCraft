package org.kucro3.parallelcraft.aopeng;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记穿透域
 */
@Target(ElementType.LOCAL_VARIABLE)
@Retention(RetentionPolicy.CLASS)
public @interface Promote {
    /**
     * 穿透域名称
     * @return 名称，默认为直接声明名称
     */
    public String value() default "";

    /**
     * 该穿透域的声明范围
     * @return 声明范围，默认为{@link Scope#LOCAL}
     */
    public Scope scope() default Scope.LOCAL;

    /**
     * 当此标志为真，并且环境中已经存在同名的域，则此标记的作用将相同于{@link Shadow}
     *
     * @return 标志
     */
    public boolean ifAbsent() default false;
}
