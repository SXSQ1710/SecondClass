package com.SecondClass.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @title: MybatisPlusConfig
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/9/12 16:04
 **/

@EnableTransactionManagement  //开启事务
@Configuration  //配置类注解
public class MybatisPlusConfig {
    @Bean
    public  MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());//乐观锁插件拦截器OptimisticLockerInnerInterceptor
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); //插件分页拦截器
        return mybatisPlusInterceptor;
    }

}

