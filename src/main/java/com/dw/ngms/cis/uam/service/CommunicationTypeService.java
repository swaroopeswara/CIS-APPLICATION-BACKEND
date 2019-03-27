package com.dw.ngms.cis.uam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.CommunicationType;
import com.dw.ngms.cis.uam.repository.CommunicationTypeRepository;

@Service
public class CommunicationTypeService {
	
	@Autowired
	private CommunicationTypeRepository communicationTypeRepository;
	
	public List<CommunicationType> getAllCommunicationTypes(){
		return communicationTypeRepository.findAll();
	}//getAllCommunicationTypes
}
