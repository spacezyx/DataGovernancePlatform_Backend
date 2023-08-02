package com.istlab.datagovernanceplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataGovernancePlatformApplication {

    public static void main(String[] args) {
        // 设置控制台编码格式为UTF-8
        System.setProperty("console.encoding", "UTF-8");

        SpringApplication.run(DataGovernancePlatformApplication.class, args);
    }

}
