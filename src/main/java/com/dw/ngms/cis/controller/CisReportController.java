package com.dw.ngms.cis.controller;

import com.dw.ngms.cis.dto.CisReportDto;
import com.dw.ngms.cis.report.ReportGenerator;
import com.dw.ngms.cis.uam.configuration.ApplicationPropertiesConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/cisorigin.cis/api/v1")
@CrossOrigin(origins = "*")
public class CisReportController extends MessageController {

	@Autowired
	private ReportGenerator reportGenerator;

	@Autowired
	private ApplicationPropertiesConfiguration applicationPropertiesConfiguration;
	
	@PostMapping("/productionReport")
	public ResponseEntity<?> generateProductionReport(HttpServletRequest request, @RequestBody @Valid CisReportDto cisReportDto) {
		String reportJrxml = reportGenerator.getRptFileDir().concat("/production.jrxml");
		String reportName = reportGenerator.getGenFileDir().concat("/ProductionReport.pdf");		
		try {
			reportGenerator.cleanupExistingReport(reportJrxml, reportName);
			
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("fromDate", cisReportDto.getFromDate());
			parameters.put("toDate", (cisReportDto.getToDate() == null) ? new Date() : 
				cisReportDto.getToDate());
			parameters.put("organisation", cisReportDto.getOrganisation());
			parameters.put("section", cisReportDto.getSection());
			parameters.put("sector", cisReportDto.getSector());
			parameters.put("userType", cisReportDto.getUserType());
			parameters.put("province", cisReportDto.getProvince());
			parameters.put("category", cisReportDto.getCategory());
			parameters.put("taskStatus", cisReportDto.getTaskStatus());
			parameters.put("officer", cisReportDto.getOfficer());
			parameters.put("resourcePath", applicationPropertiesConfiguration.getREPORT_RESOURCE_PATH());
			
			boolean isReportGenerated = reportGenerator.generateAndExportReport(reportJrxml, reportName, parameters);
			if(!isReportGenerated)
				return generateEmptyResponse(request, "Production report generation failed");
			
			File reportFile = new File(reportName);
			return (reportFile.exists()) ? getResponseEntityStream(reportFile, reportName) : 
				generateEmptyResponse(request, "Production report generation failed");
		}catch (Exception e) {
			log.error("Report stream population failed {}", e.getMessage());
			return generateFailureResponse(request, e);
		}
	}//generateProductionReport

	@PostMapping("/userProductionReport")
	public ResponseEntity<?> generateUserProductionReport(HttpServletRequest request, @RequestBody @Valid CisReportDto cisReportDto) {
		if(cisReportDto == null) {
			return generateEmptyResponse(request, "Officer required to generate user report");
		}
		String reportJrxml = reportGenerator.getRptFileDir().concat("/userProduction.jrxml");
		String reportName = reportGenerator.getGenFileDir().concat("/UserProductionReport.pdf");		
		try {
			reportGenerator.cleanupExistingReport(reportJrxml, reportName);
			
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("fromDate", cisReportDto.getFromDate());
			parameters.put("toDate", (cisReportDto.getToDate() == null) ? new Date() : 
				cisReportDto.getToDate());
			parameters.put("organisation", cisReportDto.getOrganisation());
			parameters.put("section", cisReportDto.getSection());
			parameters.put("sector", cisReportDto.getSector());
			parameters.put("province", cisReportDto.getProvince());
			parameters.put("category", cisReportDto.getCategory());
			parameters.put("taskStatus", cisReportDto.getTaskStatus());
			parameters.put("officer", cisReportDto.getOfficer());
			parameters.put("resourcePath", applicationPropertiesConfiguration.getREPORT_RESOURCE_PATH());
			
			boolean isReportGenerated = reportGenerator.generateAndExportReport(reportJrxml, reportName, parameters);
			if(!isReportGenerated)
				return generateEmptyResponse(request, "User report generation failed");
			
			File reportFile = new File(reportName);
			return (reportFile.exists()) ? getResponseEntityStream(reportFile, reportName) : 
				generateEmptyResponse(request, "User report generation failed");
		}catch (Exception e) {
			log.error("Report stream population failed {}", e.getMessage());
			return generateFailureResponse(request, e);
		}
	}//generateUserProductionReport
	
	@PostMapping("/notificationReport")
	public ResponseEntity<?> generateNotificationReport(HttpServletRequest request, 
			@RequestBody @Valid CisReportDto cisReportDto) {
		String reportJrxml = reportGenerator.getRptFileDir().concat("/notification.jrxml");
		String reportName = reportGenerator.getGenFileDir().concat("/NotificationReport.pdf");		
		try {
			reportGenerator.cleanupExistingReport(reportJrxml, reportName);
			
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("fromDate", cisReportDto.getFromDate());
			parameters.put("toDate", (cisReportDto.getToDate() == null) ? new Date() : 
				cisReportDto.getToDate());
			parameters.put("section", cisReportDto.getSection());
			parameters.put("sectionCode", cisReportDto.getSectionCode());
			parameters.put("userType", cisReportDto.getUserType());
			parameters.put("province", cisReportDto.getProvince());
			parameters.put("provinceCode", cisReportDto.getProvinceCode());
			parameters.put("category", cisReportDto.getProvince());
			parameters.put("taskStatus", cisReportDto.getProvince());
			parameters.put("resourcePath", applicationPropertiesConfiguration.getREPORT_RESOURCE_PATH());
			
			boolean isReportGenerated = reportGenerator.generateAndExportReport(reportJrxml, reportName, parameters);
			if(!isReportGenerated)
				return generateEmptyResponse(request, "Notification report generation failed");
			
			File reportFile = new File(reportName);
			return (reportFile.exists()) ? getResponseEntityStream(reportFile, reportName) : 
				generateEmptyResponse(request, "Notification report generation failed");
		}catch (Exception e) {
			log.error("Report stream population failed {}", e.getMessage());
			return generateFailureResponse(request, e);
		}
	}//generateNotificationReport
	
	@PostMapping("/overriddenBusinessRulesReport")
	public ResponseEntity<?> generateOverriddenBusinessRulesReport(HttpServletRequest request, 
			@RequestBody @Valid CisReportDto cisReportDto) {
		String reportJrxml = reportGenerator.getRptFileDir().concat("/overriddenBusinessRules.jrxml");
		String reportName = reportGenerator.getGenFileDir().concat("/OverriddenBusinessRulesReport.pdf");		
		try {
			reportGenerator.cleanupExistingReport(reportJrxml, reportName);
			
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("fromDate", cisReportDto.getFromDate());
			parameters.put("toDate", (cisReportDto.getToDate() == null) ? new Date() : 
				cisReportDto.getToDate());
			parameters.put("province", cisReportDto.getProvince());
			parameters.put("admin", cisReportDto.getAdmin());
			parameters.put("resourcePath", applicationPropertiesConfiguration.getREPORT_RESOURCE_PATH());
			
			boolean isReportGenerated = reportGenerator.generateAndExportReport(reportJrxml, reportName, parameters);
			if(!isReportGenerated)
				return generateEmptyResponse(request, "Overridden business rules report generation failed");
			
			File reportFile = new File(reportName);
			return (reportFile.exists()) ? getResponseEntityStream(reportFile, reportName) : 
				generateEmptyResponse(request, "Overridden business rules report generation failed");
		}catch (Exception e) {
			log.error("Report stream population failed {}", e.getMessage());
			return generateFailureResponse(request, e);
		}
	}//generateOverriddenBusinessRulesReport
	
}