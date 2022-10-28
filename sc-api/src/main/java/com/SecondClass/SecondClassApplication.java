package com.SecondClass;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.SecondClass.mapper")
public class SecondClassApplication {
	public static void main(String[] args) {
		SpringApplication.run(SecondClassApplication.class, args);
	}
}
