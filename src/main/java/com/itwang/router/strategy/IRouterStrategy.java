package com.itwang.router.strategy;

public interface IRouterStrategy {
    void chooseDb(String dbkey);

    void clearContext();
}
