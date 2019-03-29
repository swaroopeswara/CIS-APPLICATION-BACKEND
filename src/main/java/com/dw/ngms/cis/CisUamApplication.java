package com.dw.ngms.cis;

import com.dw.ngms.cis.uam.storage.StorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.Resource;

@SpringBootApplication
@EnableJpaAuditing
public class CisUamApplication {

    @Resource
    StorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(CisUamApplication.class, args);
    }
}
