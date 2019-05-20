package com.dw.ngms.cis.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dw.ngms.cis.dto.CisReportDto;
import com.dw.ngms.cis.report.ReportGenerator;
import com.dw.ngms.cis.uam.dto.UserLogReportDto;
import com.dw.ngms.cis.uam.dto.UserMaintainReportDto;
import com.dw.ngms.cis.uam.utilities.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/cisorigin.cis/api/v1")
@CrossOrigin(origins = "*")
public class CisReportController extends MessageController {

	@Autowired
	private ReportGenerator reportGenerator;
	
	@PostMapping("/productionReport")
	public ResponseEntity<?> generateProductionReport(HttpServletRequest request, @RequestBody @Valid CisReportDto cisReportDto) {
		String reportJrxml = "requestSummaryDetails.jrxml";
		String reportName = "ProductionReport.pdf";		
		try {
			cleanupUserSummary(reportJrxml, reportName);
			
			String resourcePath = Constants.REPORT_RESOURCE_PATH;
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
			parameters.put("resourcePath", resourcePath);
			
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

	@PostMapping("/notificationReport")
	public ResponseEntity<?> generateNotificationReport(HttpServletRequest request, 
			@RequestBody @Valid CisReportDto cisReportDto) {
		String reportJrxml = "notification.jrxml";
		String reportName = "NotificationReport.pdf";		
		try {
			cleanupUserSummary(reportJrxml, reportName);
			
			String resourcePath = Constants.REPORT_RESOURCE_PATH;
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
			parameters.put("resourcePath", resourcePath);
			
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
		String reportJrxml = "overriddenBusinessRules.jrxml";
		String reportName = "OverriddenBusinessRulesReport.pdf";		
		try {
			cleanupUserSummary(reportJrxml, reportName);
			
			String resourcePath = Constants.REPORT_RESOURCE_PATH;
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("fromDate", cisReportDto.getFromDate());
			parameters.put("toDate", (cisReportDto.getToDate() == null) ? new Date() : 
				cisReportDto.getToDate());
			parameters.put("province", cisReportDto.getProvince());
			parameters.put("admin", cisReportDto.getAdmin());
			parameters.put("resourcePath", resourcePath);
			
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
	
	public void cleanupUserSummary(String fileJrxml, String reportName) {
		this.cleanupFileOnExists(fileJrxml.replace(".jrxml", ".jasper"));
		this.cleanupFileOnExists(reportName);
	}//cleanupUserSummary
	
	public void cleanupFileOnExists(String fileName) {
		try {
			log.info("cleanup of file: {}", fileName);
			File reportFile = new File(fileName);
			if (reportFile.exists()) {
				reportFile.delete();
				log.info("{} has been deleted", fileName);
			}
		}catch(Exception e) {
			log.error("{} cleanup filed {}", fileName, e.getMessage());
		}
	}//cleanupFileOnExists
	
}