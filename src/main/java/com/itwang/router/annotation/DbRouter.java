package com.itwang.router.annotation;

public @interface DbRouter {
    /**
     * 用来分库的key
     * @return
     */
    String dbRouterKey() default "";
}
