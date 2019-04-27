package com.dw.ngms.cis.uam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import com.dw.ngms.cis.uam.ldap.LdapClient;

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
		return contextSource;
	}//contextSourceOne

	@Bean
	public LdapContextSource contextSourceTwo() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(env.getRequiredProperty("ldap.url2"));
		contextSource.setBase(env.getRequiredProperty("ldap.base.dn2"));
		contextSource.setUserDn(env.getRequiredProperty("ldap.principal2"));
		contextSource.setPassword(env.getRequiredProperty("ldap.password2"));
		return contextSource;
	}//contextSourceTwo
	
	@Bean
	public LdapContextSource contextSourceThree() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(env.getRequiredProperty("ldap.url3"));
		contextSource.setBase(env.getRequiredProperty("ldap.base.dn3"));
		contextSource.setUserDn(env.getRequiredProperty("ldap.principal3"));
		contextSource.setPassword(env.getRequiredProperty("ldap.password3"));
		return contextSource;
	}//contextSourceThree
	
	@Bean
	@SuppressWarnings("unused")
	public LdapTemplate ldapTemplate() {
		LdapTemplate ldapTemplate = new LdapTemplate(contextSource());
		if(ldapTemplate == null)
			ldapTemplate = new LdapTemplate(contextSourceTwo());
		if(ldapTemplate == null)
			ldapTemplate = new LdapTemplate(contextSourceThree());
		
		return ldapTemplate;
	}//ldapTemplate

	@Bean
	public LdapClient ldapClient() {
		return new LdapClient();
	}

}
