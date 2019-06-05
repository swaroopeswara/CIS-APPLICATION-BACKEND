package com.dw.ngms.cis.uam.ldap;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LdapClient {
	
	@Autowired
	private Environment env;

	@Autowired
	private LdapTemplate ldapTemplate;

	public boolean authenticate(String username, String password) {
		log.info("Authentication on ldap server one");
		boolean returnValue = getAuthenticateUser(ldapTemplate, username, password);
		if(returnValue == false) {
			log.info("Authentication on ldap server two");
			returnValue = getAuthenticateUser(getLdapTemplateTwo(), username, password);
		}
		if(returnValue == false) {
			log.info("Authentication on ldap server three");
			returnValue = getAuthenticateUser(getLdapTemplateThree(), username, password);
		}
		return returnValue;
	}//authenticate

	private boolean getAuthenticateUser(LdapTemplate ldpTemplate, String username, String password) {
		if(ldpTemplate == null || username == null || password == null) return false;
		log.info("LDAP template in authenticate user {}", ldpTemplate.toString());
		try {
			AndFilter filter = new AndFilter();
	        filter.and(new EqualsFilter("objectclass", env.getRequiredProperty("ldap.user.search.filter.class")));
	        filter.and(new EqualsFilter(env.getRequiredProperty("ldap.user.search.attribute"), username));
	        log.info("Authenticate user filter {}", filter.toString());
	        
	        return ldpTemplate.authenticate(LdapUtils.emptyLdapName(), filter.encode(), password);
		}catch(Exception e) {
			log.error("User '{}' LDAP authentication failed, {}",username, e);
			return false;
		}
	}//getAuthenticateUser
	
	public List<UserProfile> searchUser(String username) {		
		log.info("Search on ldap server one");
		List<UserProfile> userProfiles = searchLdapUser(ldapTemplate, username);
		if(CollectionUtils.isEmpty(userProfiles)) {
			log.info("Search on ldap server two");
			userProfiles = searchLdapUser(getLdapTemplateTwo(), username);
		}
		if(CollectionUtils.isEmpty(userProfiles)) {
			log.info("Search on ldap server three");
			userProfiles = searchLdapUser(getLdapTemplateThree(), username);
		}
		return userProfiles;
	}//searchUser

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<UserProfile> searchLdapUser(LdapTemplate ldpTemplate, String username) {
		if(ldpTemplate == null || username == null)
			return new ArrayList<UserProfile>();
		log.info("LDAP template in search user {}", ldpTemplate.toString());	
		
		AndFilter filter = new AndFilter();
		String uidAttribute = env.getRequiredProperty("ldap.user.search.attribute");
		filter.and(new EqualsFilter("objectclass", env.getRequiredProperty("ldap.user.search.filter.class")));
        filter.and(new EqualsFilter(uidAttribute, username));
        
        log.info("Search user filter {}", filter.toString());
        
		return ldpTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), SearchControls.SUBTREE_SCOPE, (new AttributesMapper() {
			@Override
			public Object mapFromAttributes(Attributes attrs) throws NamingException {
				UserProfile profile = new UserProfile();				
				profile.setExists(Boolean.TRUE);
				
				log.info("User attributes {} ", attrs.toString());
				
				if (attrs.get(uidAttribute) != null)
					profile.setUid((String) attrs.get(uidAttribute).get());				
				if (attrs.get("givenName") != null)
					profile.setFirstName((String) attrs.get("givenName").get());
				if (attrs.get("sn") != null)
					profile.setLastName((String) attrs.get("sn").get());
				if (attrs.get("employeeNumber") != null)
					profile.setEmployeeNumber((String) attrs.get("employeeNumber").get());				
				if (attrs.get("facsimileTelephoneNumber") != null)
					profile.setFacsimileTelephoneNumber((String) attrs.get("facsimileTelephoneNumber").get());				
				if (attrs.get("userPrincipalName") != null)
					profile.setMail((String) attrs.get("userPrincipalName").get());
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
				
				log.info("User profile created {}", profile.toString());
				return profile;
			}
		}));
	}//searchLdapUser
	
	public ContextSource getContextSourceTwo() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(env.getRequiredProperty("ldap.url2"));
		contextSource.setBase(env.getRequiredProperty("ldap.base.dn2"));
		contextSource.setUserDn(env.getRequiredProperty("ldap.principal2"));
		contextSource.setPassword(env.getRequiredProperty("ldap.password2"));
		contextSource.setReferral("follow");
		contextSource.afterPropertiesSet();
		return contextSource;
	}//contextSourceTwo
	
	public ContextSource getContextSourceThree() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(env.getRequiredProperty("ldap.url3"));
		contextSource.setBase(env.getRequiredProperty("ldap.base.dn3"));
		contextSource.setUserDn(env.getRequiredProperty("ldap.principal3"));
		contextSource.setPassword(env.getRequiredProperty("ldap.password3"));
		contextSource.setReferral("follow");
		contextSource.afterPropertiesSet();
		return contextSource;
	}//contextSourceThree
	
	public LdapTemplate getLdapTemplateTwo() {
		try {
			LdapTemplate ldapTemplate = new LdapTemplate(getContextSourceTwo());
			ldapTemplate.setIgnorePartialResultException(true);
			ldapTemplate.afterPropertiesSet();
			return ldapTemplate;
		} catch (Exception e) {
			log.error("Failed to create ldap template, "+e.getMessage(), e);
			return null;
		}	
	}//ldapTemplate

	public LdapTemplate getLdapTemplateThree() {
		try {
			LdapTemplate ldapTemplate = new LdapTemplate(getContextSourceThree());
			ldapTemplate.setIgnorePartialResultException(true);
			ldapTemplate.afterPropertiesSet();
			return ldapTemplate;
		} catch (Exception e) {
			log.error("Failed to create ldap template, "+e.getMessage(), e);
			return null;
		}	
	}//ldapTemplate
	
}