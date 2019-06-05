package com.dw.ngms.cis.uam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import com.dw.ngms.cis.uam.ldap.LdapClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@PropertySource("classpath:ldap-config.properties")
public class LdapConfig {

	@Autowired
	private Environment env;

	public void setEnvironment(Environment environment) {
		this.env = environment;

	}

	@Bean
	public LdapContextSource contextSource() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(env.getRequiredProperty("ldap.url"));
		contextSource.setBase(env.getRequiredProperty("ldap.base.dn"));
		contextSource.setUserDn(env.getRequiredProperty("ldap.principal"));
		contextSource.setPassword(env.getRequiredProperty("ldap.password"));
		contextSource.setReferral("follow");
		contextSource.afterPropertiesSet();
		return contextSource;
	}//contextSourceOne
	
	@Bean
	@SuppressWarnings("unused")
	public LdapTemplate ldapTemplate() {		
		try {
			LdapTemplate ldapTemplate = new LdapTemplate(contextSource());
			ldapTemplate.setIgnorePartialResultException(true);
			ldapTemplate.afterPropertiesSet();
			return ldapTemplate;
		} catch (Exception e) {
			log.error("Failed to create ldap template, "+e.getMessage(), e);
			return null;
		}		
	}//ldapTemplate

	@Bean
	public LdapClient ldapClient() {
		return new LdapClient();
	}

}
