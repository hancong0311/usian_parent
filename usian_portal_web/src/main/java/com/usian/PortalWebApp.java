package com.usian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
   vo pojo  dto
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class PortalWebApp {
    public static void main(String[] args) {
        SpringApplication.run(PortalWebApp.class, args);
    }
}
