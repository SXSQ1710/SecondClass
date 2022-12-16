package com.SecondClass.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

///**
// * @title: CorsConfig
// * @Author SXSQ
// * @Description //TODO 解决跨域问题
// * @Date 2022/9/13 11:07
// **/
//
////
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");

    }
}
//
///**
// * 解决异步访问跨域
// */
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        //本应用的所有方法都会去处理跨域请求
//        registry.addMapping("/**")
//                //允许远端访问的域名
//                .allowedOrigins("http://localhost:8080")
//                //允许请求的方法("POST", "GET", "PUT", "OPTIONS", "DELETE")
//                .allowedMethods("*")
//                //允许请求头
//                .allowedHeaders("*");
//    }
//}
