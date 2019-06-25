/*
package com.dw.ngms.cis.uam.configuration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;



@Configuration
@ConfigurationProperties("connection.ftp")
@Data
@Getter
@Setter
public class FTPConfiguration {
	
	private String server;
	private String username;
	private String password;
	private int port;
	private String uploadFilePath;

	@Profile("dev")
	@Bean
	public String devDatabaseConnection() {
		System.out.println("FTP connection for UAM_DEV "+server);
		return "FTP connection for DEV Environment";
	}



	@Profile("pre")
	@Bean
	public String prodDatabaseConnection() {
		System.out.println("FTP Connection to UAM_PRE");
		return "FTP Connection to pre Environment";
	}


	@Profile("prod")
	@Bean
	public String prodDataWorldDatabaseConnection() {
		System.out.println("FTP Connection to to UAM_PROD");
		return "FTP Connection to prod Environment";
	}

	@Profile("drdlr")
	@Bean
	public String prodDrdlrDatabaseConnection() {
		System.out.println("FTP Connection to to UAM_DRDLR");
		return "FTP Connection to drdlr Environment";
	}
}
*/
