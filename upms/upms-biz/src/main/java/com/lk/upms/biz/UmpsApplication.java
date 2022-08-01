package com.lk.upms.biz;

import com.lk.common.feign.annotation.EnableCustomFeignClients;
import com.lk.common.security.annotation.EnableResourceServer;
import com.lk.common.swagger.annotation.EnableDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author smile
 */
@EnableDoc
@EnableResourceServer
@EnableCustomFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class UmpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UmpsApplication.class, args);
    }

}
