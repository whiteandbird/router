package com.itwang.router.strategy.impl;

import com.itwang.router.controller.context.DbConfigContext;
import com.itwang.router.strategy.IRouterStrategy;
import com.itwang.router.support.DbSettingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouterStrategyImpl implements IRouterStrategy {

    private Logger logger = LoggerFactory.getLogger(RouterStrategyImpl.class);

    private DbSettingConfig dbSettingConfig;


    public RouterStrategyImpl(DbSettingConfig dbSettingConfig){
        this.dbSettingConfig = dbSettingConfig;
    }

    @Override
    public void chooseDb(String dbkey) {
        int tbCount = dbSettingConfig.getTbCount();

        int allCount = dbSettingConfig.getDbCount() * dbSettingConfig.getTbCount();

        int idx = (dbkey.hashCode() ^ (dbkey.hashCode() >>> 16)) & (allCount-1);
        int dbIdx = (idx / tbCount) + 1;
        int tbIdx = idx % tbCount+1;

        // 设置上下文 db
        DbConfigContext.setDbIdx(String.valueOf(String.format("%02d", dbIdx)));
        DbConfigContext.setTbIdx(String.valueOf(String.format("%03d", tbIdx)));
        this.logger.info("setting dbIdx: {}  tbIdx: {}", tbIdx, dbIdx);

    }

    @Override
    public void clearContext() {
        DbConfigContext.dbIdxLocals.remove();
        DbConfigContext.tbIdxLocals.remove();
    }

    public static void main(String[] args) {
        System.out.println(String.format("%02d",5));
    }
}
