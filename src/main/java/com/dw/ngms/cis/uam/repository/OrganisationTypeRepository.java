package com.dw.ngms.cis.uam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dw.ngms.cis.uam.entity.OrganisationType;

@Repository
public interface OrganisationTypeRepository extends JpaRepository<OrganisationType, Long> {
	
}
