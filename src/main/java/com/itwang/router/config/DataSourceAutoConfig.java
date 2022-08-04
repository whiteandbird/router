package com.itwang.router.config;

import cn.hutool.core.util.StrUtil;
import com.itwang.router.DbRouterJoinPoint;
import com.itwang.router.dynamic.DynamicDataSource;
import com.itwang.router.mybatisPlugin.MybatisPluginForDbrouter;
import com.itwang.router.properties.ConfigProperties;
import com.itwang.router.strategy.IRouterStrategy;
import com.itwang.router.strategy.impl.RouterStrategyImpl;
import com.itwang.router.support.DbSettingConfig;
import com.itwang.router.utils.PropertyUtil;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.itwang.router.constants.StringConstants.CONFIG_PREFIX;

@Configuration
public class DataSourceAutoConfig implements EnvironmentAware {

    private Environment environment;
    /**
     * 默认配置环境
     */
    private Map<String,Object> defaultEnvironment;

    /**
     * 所有的配置环境
     */
    private final Map<String, Map<String, Object>> allEnviroment = new HashMap<>();


    private int dbCount;

    private int tbCount;

    private String defaultKey;
    /**
     * 采用注入的
     */
    @Resource
    private ConfigProperties properties;


    /**
     * 等待其余的注册完毕
     */
    @PostConstruct
    public void doAfter(){
        initOtherKey();
        initDefaultDbConfig();
        initListDbConfig();

    }

    @Bean
    public DbSettingConfig dbSettingConfig(){
        DbSettingConfig settingConfig = new DbSettingConfig();
        settingConfig.setTbCount(tbCount);
        settingConfig.setDbCount(dbCount);
        return settingConfig;
    }

    @Bean
    public DataSource dataSource(){
        Map<Object, Object> dataSourceConfig = new HashMap<>();
        for(String dbName : allEnviroment.keySet()){
            Map<String, Object> targetSourceMap = allEnviroment.get(dbName);
            dataSourceConfig.put(dbName, new DriverManagerDataSource(targetSourceMap.get("url").toString(),
                    targetSourceMap.get("username").toString(),
                    targetSourceMap.get("password").toString()));
        }
        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        dynamicDataSource.setTargetDataSources(dataSourceConfig);

        dynamicDataSource.setDefaultTargetDataSource(new DriverManagerDataSource(
                defaultEnvironment.get("url").toString(),
                defaultEnvironment.get("username").toString(),
                defaultEnvironment.get("password").toString()
        ));

        return dynamicDataSource;

    }

    @Bean
    public TransactionTemplate dataSourceTransactionManager(DataSource dataSource){
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();

        transactionManager.setDataSource(dataSource);
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(transactionManager);
        transactionTemplate.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return transactionTemplate;
    }

    @Bean
    public IRouterStrategy routerStrategy(DbSettingConfig dbSettingConfig){
        return new RouterStrategyImpl(dbSettingConfig);
    }

    @Bean
    public DbRouterJoinPoint dbRouterJoinPoint(IRouterStrategy strategy, ConfigProperties configProperties){
        return new DbRouterJoinPoint(configProperties, strategy);
    }

    @Bean
    public Interceptor interceptor(){
        return new MybatisPluginForDbrouter();
    }

    private void initOtherKey(){
        this.dbCount = properties.getDbCount();
        this.tbCount = properties.getTbCount();
        this.defaultKey = properties.getDefaultKey();
    }

    private void initDefaultDbConfig(){
        if(StrUtil.isEmpty(properties.getDefaultDb())){
            throw new RuntimeException("no defaultdb");
        }
        String defaultDbPrefix = String.format("%s.%s", CONFIG_PREFIX, properties.getDefaultDb());
        this.defaultEnvironment = PropertyUtil.handle(environment, defaultDbPrefix, Map.class);
    }

    private void initListDbConfig(){
        List<String> externDb = properties.getList();
        String prefix = CONFIG_PREFIX;
        for(String dbName : externDb){
            Map<String,Object> result = PropertyUtil.handle(environment, String.format("%s.%s", prefix, dbName), Map.class);
            this.allEnviroment.put(dbName, result);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
