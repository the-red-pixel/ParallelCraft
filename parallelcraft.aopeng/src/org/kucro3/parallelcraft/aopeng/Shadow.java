package org.kucro3.parallelcraft.aopeng;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记影射域
 */
@Target({ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.CLASS)
public @interface Shadow {
    /**
     * 影射域的名称
     *
     * @return 名称，默认为直接声明名称
     */
    public String value() default "";

    /**
     * 所影射的域的声明范围
     *
     * @return 声明范围，默认为{@link Scope#GLOBAL}
     */
    public Scope scope() default Scope.GLOBAL;
}