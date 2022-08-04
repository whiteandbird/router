package com.itwang.router.properties;

import com.itwang.router.support.DataBaseConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.itwang.router.constants.StringConstants.CONFIG_PREFIX;

@ConfigurationProperties(prefix = CONFIG_PREFIX)
@Component
public class ConfigProperties {

    public ConfigProperties(){
        System.out.println("config init");
    }
    /**
     * 默认主库
     */
    private String defaultDb;


    /**
     * 所有库
     */
    private List<String> list;

    /**
     * 默认key
     */
    private String defaultKey;

    /**
     * 分库数量
     */
    private int dbCount;

    /**
     * 分表数量
     */
    private int tbCount;



    public String getDefaultDb() {
        return defaultDb;
    }

    public void setDefaultDb(String defaultDb) {
        this.defaultDb = defaultDb;
    }


    public int getDbCount() {
        return dbCount;
    }

    public void setDbCount(int dbCount) {
        this.dbCount = dbCount;
    }

    public int getTbCount() {
        return tbCount;
    }

    public void setTbCount(int tbCount) {
        this.tbCount = tbCount;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getDefaultKey() {
        return defaultKey;
    }

    public void setDefaultKey(String defaultKey) {
        this.defaultKey = defaultKey;
    }
}
