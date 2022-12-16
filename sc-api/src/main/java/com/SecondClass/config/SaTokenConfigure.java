package com.SecondClass.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @title: SaTokenConfigure
 * @Author SXSQ
 * @Description // SaToken拦截器
 * @Date 2022/11/13 14:52
 **/

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Resource
    private CorsInterceptor corsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

//        // 跨域拦截器需放在最上面
//        registry.addInterceptor(corsInterceptor).addPathPatterns("/**");

//        // 校验token的拦截器
        registry.addInterceptor(new SaInterceptor(handler -> {
         // 指定一条 match 规则
         SaRouter
                 .match("/**")    // 拦截的 path 列表，可以写多个 */
                 .notMatch("/api/manage/login")        // 排除掉的 path 列表，可以写多个
                 .check(r -> StpUtil.checkLogin());        // 要执行的校验动作，可以写完整的 lambda 表达式

         // 根据路由划分模块，不同模块不同鉴权
         //角色划分：admin(校团委)、manage(普通组织)、user(学生)
         SaRouter.match("/api/activity/**", r -> StpUtil.checkRole("user"));
         SaRouter.match("/api/manage/createOrg", r -> StpUtil.checkRole("admin"));
         SaRouter.match("/api/manage/addAccount", r -> StpUtil.checkRole("admin"));
         SaRouter.match("/api/manage/addAccountByBatch", r -> StpUtil.checkRole("admin"));

        })).addPathPatterns("/**");
     }
}

