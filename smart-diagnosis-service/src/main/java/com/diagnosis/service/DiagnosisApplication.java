package com.diagnosis.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 智慧问诊服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DiagnosisApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiagnosisApplication.class, args);
    }
}
