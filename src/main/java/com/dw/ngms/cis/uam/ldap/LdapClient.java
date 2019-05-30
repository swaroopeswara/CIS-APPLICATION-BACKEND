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
		boolean returnValue = getAuthenticateUser(ldapTemplate, username, password);
		if(returnValue == false) 
			returnValue = getAuthenticateUser(getLdapTemplateTwo(), username, password);
		if(returnValue == false) 
			returnValue = getAuthenticateUser(getLdapTemplateThree(), username, password);
		
		return returnValue;
	}//authenticate

	private boolean getAuthenticateUser(LdapTemplate ldpTemplate, String username, String password) {
		if(ldpTemplate == null || username == null || password == null) return false;
		
		try {
			AndFilter filter = new AndFilter();
	        filter.and(new EqualsFilter("objectclass", env.getRequiredProperty("ldap.user.search.filter.class")));
	        filter.and(new EqualsFilter(env.getRequiredProperty("ldap.user.search.attribute"), username));
	        
	        return ldpTemplate.authenticate(LdapUtils.emptyLdapName(), filter.encode(), password);
		}catch(Exception e) {
			log.error("User '{}' LDAP authentication failed", username);
			return false;
		}
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
	private List<UserProfile> searchLdapUser(LdapTemplate ldpTemplate, String username) {
		if(ldpTemplate == null || username == null)
			return new ArrayList<UserProfile>();
			
		AndFilter filter = new AndFilter();
		String uidAttribute = env.getRequiredProperty("ldap.user.search.attribute");
		filter.and(new EqualsFilter("objectclass", env.getRequiredProperty("ldap.user.search.filter.class")));
        filter.and(new EqualsFilter(uidAttribute, username));
        
		return ldpTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), SearchControls.SUBTREE_SCOPE, (new AttributesMapper() {
			@Override
			public Object mapFromAttributes(Attributes attrs) throws NamingException {
				UserProfile profile = new UserProfile();				
				profile.setExists(Boolean.TRUE);
				
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