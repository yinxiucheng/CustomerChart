package com.yxc.customerchart.inject.others;

import javax.inject.Qualifier;

/**
 * Created by yanjiang on 2016/3/28.
 */
@Qualifier

public @interface StringQuali {
    String value() default "";
}
