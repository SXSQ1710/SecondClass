package com.SecondClass;


import com.SecondClass.config.myFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@MapperScan("com.SecondClass.mapper")
@EnableScheduling
public class SecondClassApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecondClassApplication.class, args);
	}

//	@Bean
//	public FilterRegistrationBean getFilterRegistrationBean(){
//		FilterRegistrationBean bean = new FilterRegistrationBean(new myFilter());
//		//bean.addUrlPatterns(new String[]{"*.do","*.jsp"});
//		bean.addUrlPatterns("/*");
//		return bean;
//	}


}
