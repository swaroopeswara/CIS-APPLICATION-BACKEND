package com.dw.ngms.cis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CisUamApplication {

    public static void main(String[] args) {
        SpringApplication.run(CisUamApplication.class, args);
    }
}
