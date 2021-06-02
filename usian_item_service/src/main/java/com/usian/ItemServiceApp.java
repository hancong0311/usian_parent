package com.usian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 通用mapper 在usian使用的步骤：
 *  1. common_pojo  添加通用mapper的依赖
 *  2. xxx_service 的application.yml 配置数据源，mybaits,mapper 核心信息
 *  3. pojo---->common_pojo
 *  4. xxxMapper --->common_mapper
 *  5. xxxApp @MapperScan....
 */
@SpringBootApplication
@EnableDiscoveryClient//等价
//@EnableEurekaClient
@MapperScan("com.usian.mapper")
public class ItemServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApp.class, args);
    }
}
