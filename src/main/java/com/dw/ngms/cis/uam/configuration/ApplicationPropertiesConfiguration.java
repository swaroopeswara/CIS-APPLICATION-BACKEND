package com.dw.ngms.cis.uam.configuration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("application.properties")
@Data
@Setter
@Getter
public class ApplicationPropertiesConfiguration {



    private String uploadDirectoryPath;
    private String downloadDirectoryPath;
    private String REPORT_RESOURCE_PATH;
    private String invoiceDirectory;
    private String uploadDirectoryPathFTP;
    private String fileNamePDF;
    private String pdfTemplate;
    private String adminUserMail;
    private String provinceAdminMail;
    private String mailFrom;
    private String mailTo;



    @Profile("dev")
    @Bean
    public String devDatabaseConnection() {
        System.out.println("Application Properties for UAM_DEV "+uploadDirectoryPath);
        return "Application Properties for DEV Environment";
    }


    @Profile("pre")
    @Bean
    public String prodDatabaseConnection() {
        System.out.println("Application Properties to UAM_PRE");
        return "Application Properties to PRE Environment";
    }


    @Profile("prod")
    @Bean
    public String prodDataWorldDatabaseConnection() {
        System.out.println("Application Properties to UAM_PROD");
        return "Application Properties to UAM_PROD Environment";
    }

    @Profile("drdlr")
    @Bean
    public String prodDrdlrDatabaseConnection() {
        System.out.println("Application Properties to UAM_DRDLR_PROD");
        return "Application Properties to UAM_DRDLR_PROD Environment";
    }
}
