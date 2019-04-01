package com.dw.ngms.cis.uam.service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeGeneratorService {

	@Autowired
	private EntityManager em; 
	
	private Long getNextValueOfSequence(String sequenceName) {
		try {
			String queryString = "select nextval('"+sequenceName+ "')";
			Query query = em.createNativeQuery(queryString);
			return (Long)query.getSingleResult();
		}catch(Exception e) {
			//TODO log here
			throw new RuntimeException("Failed to generate next value for "+sequenceName);
		}
	}//getNextValueOfSequence

	public String getSectorNextCode() {
		return "SEC"+ getNextValueOfSequence("SECTOR_ID_SEQ");
	}//getSectorNextCode
	
	public String getOrganisationTypeNextCode() {
		return "ORGTY"+ getNextValueOfSequence("ORGANISATION_TYPE_ID_SEQ");
	}

	public String getCommunicationTypeNextCode() {
		return "COMM"+ getNextValueOfSequence("COMMUNICATION_TYPE_ID_SEQ");
	}
}
