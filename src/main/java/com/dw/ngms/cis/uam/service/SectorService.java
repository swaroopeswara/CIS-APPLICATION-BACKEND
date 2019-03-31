package com.dw.ngms.cis.uam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.Sector;
import com.dw.ngms.cis.uam.repository.SectorRepository;

@Service
public class SectorService {
	
    @Autowired
    private SectorRepository sectorRepository;

    public List<Sector> getAllSectors() {
        return this.sectorRepository.findAll();
    }

    public Sector addSector(Sector sector) {
    	if(sector == null || sector.getId() != null) return null;
    	sector.setCode(getSectorCode());
        return this.sectorRepository.save(sector);
    }//addSector
    
    private String getSectorCode() {
		String code = null;
		// TODO need to complete this impl
		code = "SECT0010";
		return code;
	}//getSectorCode

}
