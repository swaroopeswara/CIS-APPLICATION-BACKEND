package com.dw.ngms.cis.uam.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.uam.entity.Sector;
import com.dw.ngms.cis.uam.service.SectorService;

@RestController
@RequestMapping("/sector")
public class SectorController extends MessageController {
	
    @Autowired
    private SectorService sectorService;

    @GetMapping("/getSectors")
    public ResponseEntity<?> getAllSectors(HttpServletRequest request) {
        try {
        	List<Sector> sectorList = this.sectorService.getAllSectors();
        	return (CollectionUtils.isEmpty(sectorList)) ? generateEmptyResponse(request, "Sectors not found") 
            		: ResponseEntity.status(HttpStatus.OK).body(sectorList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllSectors
    
//    @RequestMapping(value = "/addSector", method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public Sector addSector(@RequestBody Sector sector) {
//        return this.sectorService.addSector(sector);
//    }
//
//    @RequestMapping(value = "/updateSector", method = RequestMethod.PUT,
//            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public Sector updateSector(@RequestBody Sector sector) {
//        return this.sectorService.updateSector(sector);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public Optional<Sector> getSector(@PathVariable int id) {
//        return this.sectorService.getSectorById(id);
//    }
//
//    @RequestMapping(value = "/all", method = RequestMethod.DELETE)
//    public void deleteAllSectors() {
//        this.sectorService.deleteAllSectors();
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//    public void deleteSector(@PathVariable int id) {
//        this.sectorService.deleteSectorById(id);
//    }
    
}
