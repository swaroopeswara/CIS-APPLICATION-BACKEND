package com.dw.ngms.cis.uam.ldap;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LdapClient {
	
	@Autowired
	private Environment env;

	@Autowired
	private ContextSource contextSource;

	@Autowired
	private LdapTemplate ldapTemplate;

	public boolean authenticate(final String username, final String password) {
		try {
			contextSource.getContext("uid=" + username + ",ou=people," + env.getRequiredProperty("ldap.base.dn"),
					password);		
		}catch(Exception e) {
			log.error("User '{}' LDAP authentication failed", username);
			return false;
		}
		return true;
	}//authenticate

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<UserProfile> searchUser(final String username) {

		return ldapTemplate.search("ou=people", "uid=" + username, (new AttributesMapper() {

			@Override
			public Object mapFromAttributes(Attributes attrs) throws NamingException {
				UserProfile profile = new UserProfile();

				if (attrs.get("givenName") != null)
					profile.setFirstName((String) attrs.get("givenName").get());

				if (attrs.get("sn") != null)
					profile.setLastName((String) attrs.get("sn").get());

				if (attrs.get("designation") != null)
					profile.setDesignation((String) attrs.get("designation").get());

				return profile;
			}
		}));

		/*
		 * return ldapTemplate.search( "ou=people", "uid=" + username,
		 * (AttributesMapper<String>) attrs -> (String) attrs .get("cn").get());
		 */
	}

}