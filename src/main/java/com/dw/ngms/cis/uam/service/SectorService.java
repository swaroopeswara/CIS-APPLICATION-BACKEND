package com.dw.ngms.cis.uam.service;

import java.util.List;
import java.util.Optional;

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

    public Sector addSector(Sector Sector) {
        return this.sectorRepository.save(Sector);
    }

    public Optional<Sector> getSectorById(Long id) {
        return this.sectorRepository.findById(id);
    }

    public Sector updateSector(Sector Sector) {
        return this.sectorRepository.save(Sector);
    }

    public void deleteSectorById(Long id) {
        this.sectorRepository.deleteById(id);
    }

    public void deleteAllSectors() {
        this.sectorRepository.deleteAll();
    }

}
