package com.itwang.router.annotation;

public @interface DbStrategy {
    /**
     * 是否分表
     * @return
     */
    boolean splitTable() default false;
}
