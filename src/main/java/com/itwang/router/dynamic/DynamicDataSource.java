package com.itwang.router.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import static com.itwang.router.context.DbConfigContext.getDbIdx;

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return "db"+ getDbIdx();
    }
}
