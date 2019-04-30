package com.dw.ngms.cis.uam.ldap;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LdapClient {
	
	@Autowired
	private Environment env;

	@Autowired
	private ContextSource contextSource;

	@Autowired
	private LdapTemplate ldapTemplate;

	public boolean authenticate(String username, String password) {
		boolean returnValue = getAuthenticateUser(contextSource, username, password);
		if(returnValue == false) 
			returnValue = getAuthenticateUser(getContextSourceTwo(), username, password);
		if(returnValue == false) 
			returnValue = getAuthenticateUser(getContextSourceThree(), username, password);
		
		return returnValue;
	}//authenticate

	private boolean getAuthenticateUser(ContextSource contextSource, String username, String password) {
		try {
			contextSource.getContext("uid=" + username + ",ou=people," + env.getRequiredProperty("ldap.base.dn"),
				password);
		}catch(Exception e) {
			log.error("User '{}' LDAP authentication failed", username);
			return false;
		}
		return true;
	}//getAuthenticateUser
	
	public List<UserProfile> searchUser(String username) {		
		List<UserProfile> userProfiles = searchLdapUser(ldapTemplate, username);
		if(CollectionUtils.isEmpty(userProfiles))
			userProfiles = searchLdapUser(getLdapTemplateTwo(), username);
		if(CollectionUtils.isEmpty(userProfiles))
			userProfiles = searchLdapUser(getLdapTemplateThree(), username);
		
		return userProfiles;
	}//searchUser

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<UserProfile> searchLdapUser(LdapTemplate ldapTemplate, String username) {
		if(ldapTemplate == null || username == null)
			return new ArrayList<UserProfile>();
	
		return ldapTemplate.search("ou=people", "uid=" + username, (new AttributesMapper() {
			@Override
			public Object mapFromAttributes(Attributes attrs) throws NamingException {
				UserProfile profile = new UserProfile();				
				profile.setExists(Boolean.TRUE);
				
				if (attrs.get("uid") != null)
					profile.setUid((String) attrs.get("uid").get());				
				if (attrs.get("givenName") != null)
					profile.setFirstName((String) attrs.get("givenName").get());
				if (attrs.get("sn") != null)
					profile.setLastName((String) attrs.get("sn").get());
				if (attrs.get("employeeNumber") != null)
					profile.setEmployeeNumber((String) attrs.get("employeeNumber").get());				
				if (attrs.get("facsimileTelephoneNumber") != null)
					profile.setFacsimileTelephoneNumber((String) attrs.get("facsimileTelephoneNumber").get());				
				if (attrs.get("mail") != null)
					profile.setMail((String) attrs.get("mail").get());
				if (attrs.get("mobile") != null)
					profile.setMobile((String) attrs.get("mobile").get());
				if (attrs.get("telephoneNumber") != null)
					profile.setTelephoneNumber((String) attrs.get("telephoneNumber").get());
				if (attrs.get("street") != null)
					profile.setStreet((String) attrs.get("street").get());
				if (attrs.get("l") != null)
					profile.setCity((String) attrs.get("l").get());
				if (attrs.get("st") != null)
					profile.setProvince((String) attrs.get("st").get());
				if (attrs.get("postalCode") != null)
					profile.setPostalCode((String) attrs.get("postalCode").get());
				
				return profile;
			}
		}));

		/*
		 * return ldapTemplate.search( "ou=people", "uid=" + username,
		 * (AttributesMapper<String>) attrs -> (String) attrs .get("cn").get());
		 */
	}//searchLdapUser
	
	public ContextSource getContextSourceTwo() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(env.getRequiredProperty("ldap.url2"));
		contextSource.setBase(env.getRequiredProperty("ldap.base.dn2"));
		contextSource.setUserDn(env.getRequiredProperty("ldap.principal2"));
		contextSource.setPassword(env.getRequiredProperty("ldap.password2"));
		return contextSource;
	}//contextSourceTwo
	
	public ContextSource getContextSourceThree() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(env.getRequiredProperty("ldap.url3"));
		contextSource.setBase(env.getRequiredProperty("ldap.base.dn3"));
		contextSource.setUserDn(env.getRequiredProperty("ldap.principal3"));
		contextSource.setPassword(env.getRequiredProperty("ldap.password3"));
		return contextSource;
	}//contextSourceThree
	
	public LdapTemplate getLdapTemplateTwo() {
		return new LdapTemplate(getContextSourceTwo());
	}//ldapTemplate

	public LdapTemplate getLdapTemplateThree() {
		return new LdapTemplate(getContextSourceThree());
	}//ldapTemplate
	
}