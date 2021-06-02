package com.usian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.usian.mapper")
public class CotnentServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(CotnentServiceApp.class, args);
    }
}
