package com.itwang.router.db;

import com.itwang.router.annotation.DbRouter;
import com.itwang.router.annotation.DbStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@DbStrategy(splitTable = true)
public interface UserStrategyDao {

    @DbRouter(dbRouterKey = "uid")
    UserStrategyExport queryUserStrategyExportByUId(String uid);
}
