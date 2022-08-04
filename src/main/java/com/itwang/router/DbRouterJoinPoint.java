package com.itwang.router;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.itwang.router.annotation.DbRouter;
import com.itwang.router.properties.ConfigProperties;
import com.itwang.router.strategy.IRouterStrategy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class DbRouterJoinPoint {

    private ConfigProperties configProperties;

    private IRouterStrategy strategy;


    private Logger logger = LoggerFactory.getLogger(DbRouterJoinPoint.class);

    @Pointcut("@annotation(com.itwang.router.annotation.DbRouter)")
    public void pointCut(){};

    @Around("pointCut() && @annotation(router)")
    public Object doRouter(ProceedingJoinPoint proceedingJoinPoint, DbRouter router) throws Throwable {
        String routerKey = router.dbRouterKey();
        logger.info("==================aspect========================");
        if(StrUtil.isEmpty(routerKey) && StrUtil.isEmpty(configProperties.getDefaultKey())){
            throw new RuntimeException("this is no router key");
        }
        if(StrUtil.isEmpty(routerKey)){
            routerKey = configProperties.getDefaultKey();
        }
        strategy.chooseDb(getAttr(routerKey, proceedingJoinPoint.getArgs()));
        Object proceed = proceedingJoinPoint.proceed();
        strategy.clearContext();
        return proceed;
    }

    private String getAttr(String routerKey, Object[] args) {
        String value = null;
        if (args.length == 1) {
            if(args[0] instanceof String){
                return args[0].toString();
            }
        }else{

            for(Object obj : args){
                if(StrUtil.isNotEmpty(value)){
                    break;
                }
                value = BeanUtil.getProperty(obj, routerKey);
            }
//            return value;
        }
        return value;
    }

    public DbRouterJoinPoint(ConfigProperties configProperties, IRouterStrategy strategy){
        this.strategy = strategy;
        this.configProperties = configProperties;
    }
}
