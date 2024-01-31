package com.SecondClass;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @title: WebAppConfig
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/12/12 0:07
 **/

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Resource
    private HeaderInterceptor headerInterceptor;

    /**
     * 添加拦截规则
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> patterns = new ArrayList<>();

        patterns.add("/login/login");

        registry.addInterceptor(headerInterceptor)
                .addPathPatterns("/**") //所有的请求都要拦截。
                .excludePathPatterns(patterns); //将不需要拦截的接口请求排除在外
    }

    /**
     * //下面代码意思是:配置一个拦截器，如果访问路径时addResourceHandler中的这个路径（我这里是/**表示所有的路径），
     *  那么就映射到访问本地的addResourceLocations这个路径上，这样就可以看到该路径上的资源了
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**") //配置需要添加静态资源请求的url
                .addResourceLocations("classpath:/pic/"); //配置静态资源路径
    }
}