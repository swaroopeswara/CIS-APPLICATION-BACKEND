package com.dw.ngms.cis;

import com.dw.ngms.cis.uam.storage.StorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.Resource;

@SpringBootApplication
@EnableJpaAuditing
public class CisUamApplication extends SpringBootServletInitializer {

    @Resource
    StorageService storageService;


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CisUamApplication .class);
    }


    public static void main(String[] args) {
        SpringApplication.run(CisUamApplication.class, args);
    }


}
