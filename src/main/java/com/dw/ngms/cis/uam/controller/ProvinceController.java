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

import com.dw.ngms.cis.uam.entity.Province;
import com.dw.ngms.cis.uam.service.ProvinceService;

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
public class ProvinceController extends MessageController {
	
	@Autowired
    private ProvinceService provinceService;

    @GetMapping("/getProvinces")
    public ResponseEntity<?> getAllProvinces(HttpServletRequest request) {
        try {
        	List<Province> provinceList = this.provinceService.getAllProvinces();
        	return (CollectionUtils.isEmpty(provinceList)) ? generateEmptyResponse(request, "Provinces not found") 
            		: ResponseEntity.status(HttpStatus.OK).body(provinceList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllProvinces
    
//    @RequestMapping(value = "/addProvince", method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public Province addProvince(@RequestBody Province province) {
//        return this.provinceService.addProvince(province);
//    }
//
//    @RequestMapping(value = "/updateProvince", method = RequestMethod.PUT,
//            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public Province updateProvince(@RequestBody Province province) {
//        return this.provinceService.updateProvince(province);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public Optional<Province> getProvince(@PathVariable int id) {
//        return this.provinceService.getProvinceById(id);
//    }
//
//    @RequestMapping(value = "/all", method = RequestMethod.DELETE)
//    public void deleteAllProvinces() {
//        this.provinceService.deleteAllProvinces();
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//    public void deleteProvince(@PathVariable int id) {
//        this.provinceService.deleteProvinceById(id);
//    }
    
}
