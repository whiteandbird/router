package com.itwang.router.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

import static com.itwang.router.controller.context.DbConfigContext.getDbIdx;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);
    // 确定选哪个db
    @Override
    protected Object determineCurrentLookupKey() {
        if(logger.isDebugEnabled()){
            logger.info("choose db to execute : {}", "db"+getDbIdx());
        }
        return "db"+ getDbIdx();
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        logger.info("======设置默认 数据源======{}",targetDataSources);
        super.setTargetDataSources(targetDataSources);
    }
}
