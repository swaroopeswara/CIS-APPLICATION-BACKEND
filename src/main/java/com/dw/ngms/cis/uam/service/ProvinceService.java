package com.dw.ngms.cis.uam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dw.ngms.cis.uam.entity.Province;
import com.dw.ngms.cis.uam.repository.ProvinceRepository;

@Service
public class ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository;

    public List<Province> getAllProvinces() {
        return this.provinceRepository.findAll();
    }

    public Province addProvince(Province Province) {
        return this.provinceRepository.save(Province);
    }

    public Optional<Province> getProvinceById(Long id) {
        return this.provinceRepository.findById(id);
    }

    public String getProvinceShortName(String provinceId) {
        return this.provinceRepository.getProvinceShortName(provinceId);
    }

    public Province updateProvince(Province Province) {
        return this.provinceRepository.save(Province);
    }

    public void deleteProvinceById(Long id) {
        this.provinceRepository.deleteById(id);
    }

    public void deleteAllProvinces() {
        this.provinceRepository.deleteAll();
    }

}
