package com.itwang.router.controller;

import com.itwang.router.db.UserStrategyDao;
import com.itwang.router.db.UserStrategyExport;
import com.itwang.router.support.DbSettingConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class DbController {

    @Resource
    private DbSettingConfig dbSettingConfig;

    @Resource
    private UserStrategyDao userStrategyDao;

    @GetMapping("/config")
    public DbSettingConfig getSetting(){
        return dbSettingConfig;
    }

    @GetMapping("/get/{id}")
    public UserStrategyExport getExport(@PathVariable("id") String aaid){
        return userStrategyDao.queryUserStrategyExportByUId(aaid);
    }
}
