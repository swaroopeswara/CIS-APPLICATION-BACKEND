package com.dw.ngms.cis.uam.report;

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

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.report.ReportGenerator;
import com.dw.ngms.cis.uam.dto.UserLogReportDto;
import com.dw.ngms.cis.uam.dto.UserMaintainReportDto;
import com.dw.ngms.cis.uam.dto.UserSummaryReportDto;
import com.dw.ngms.cis.uam.utilities.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class UamReportController extends MessageController {

	@Autowired
	private ReportGenerator reportGenerator;
	
	@PostMapping("/userSummaryReport")
	public ResponseEntity<?> generateUserSummaryReport(HttpServletRequest request, @RequestBody @Valid UserSummaryReportDto userSummaryReportDto) {
		String reportJrxml = reportGenerator.getRptFileDir().concat("/userSummary.jrxml");
		String reportName = reportGenerator.getGenFileDir().concat("/UserSummaryReport.pdf");		
		try {
			reportGenerator.cleanupExistingReport(reportJrxml, reportName);
			
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("fromDate", userSummaryReportDto.getFromDate());
			parameters.put("toDate", (userSummaryReportDto.getToDate() == null) ? new Date() : 
				userSummaryReportDto.getToDate());
			parameters.put("organisation", userSummaryReportDto.getOrganisation());
			parameters.put("section", userSummaryReportDto.getSection());
			parameters.put("sector", userSummaryReportDto.getSector());
			parameters.put("userType", userSummaryReportDto.getUserType());
			parameters.put("province", userSummaryReportDto.getProvince());		
			parameters.put("resourcePath", Constants.REPORT_RESOURCE_PATH);
			
			boolean isReportGenerated = reportGenerator.generateAndExportReport(reportJrxml, reportName, parameters);
			if(!isReportGenerated)
				return generateEmptyResponse(request, "User summary report generation failed");
			
			File reportFile = new File(reportName);
			return (reportFile.exists()) ? getResponseEntityStream(reportFile, reportName) : 
				generateEmptyResponse(request, "User summary report generation failed");
		}catch (Exception e) {
			log.error("Report stream population failed {}", e.getMessage());
			return generateFailureResponse(request, e);
		}
	}//generateUserSummaryReport

	@PostMapping("/userLogReport")
	public ResponseEntity<?> generateUserLogReport(HttpServletRequest request, 
			@RequestBody @Valid UserLogReportDto userLogReportDto) {
		String reportJrxml = reportGenerator.getRptFileDir().concat("/userLog.jrxml");
		String reportName = reportGenerator.getGenFileDir().concat("/UserLogReport.pdf");		
		try {
			reportGenerator.cleanupExistingReport(reportJrxml, reportName);
			
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("fromDate", userLogReportDto.getFromDate());
			parameters.put("toDate", (userLogReportDto.getToDate() == null) ? new Date() : 
				userLogReportDto.getToDate());
			parameters.put("userType", userLogReportDto.getUserType());
			parameters.put("resourcePath", Constants.REPORT_RESOURCE_PATH);
			
			boolean isReportGenerated = reportGenerator.generateAndExportReport(reportJrxml, reportName, parameters);
			if(!isReportGenerated)
				return generateEmptyResponse(request, "User log report generation failed");
			
			File reportFile = new File(reportName);
			return (reportFile.exists()) ? getResponseEntityStream(reportFile, reportName) : 
				generateEmptyResponse(request, "User log report generation failed");
		}catch (Exception e) {
			log.error("Report stream population failed {}", e.getMessage());
			return generateFailureResponse(request, e);
		}
	}//generateUserLogReport
	
	@PostMapping("/quarterlyUpdatedUserReport")
	public ResponseEntity<?> generateQuarterlyUpdatedUserReport(HttpServletRequest request, 
			@RequestBody @Valid UserMaintainReportDto userMaintainReportDto) {
		String reportJrxml = reportGenerator.getRptFileDir().concat("/quarterlyUpdatedUser.jrxml");
		String reportName = reportGenerator.getGenFileDir().concat("/QuarterlyUpdatedUserReport.pdf");		
		try {
			reportGenerator.cleanupExistingReport(reportJrxml, reportName);
			
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("fromDate", userMaintainReportDto.getFromDate());
			parameters.put("toDate", (userMaintainReportDto.getToDate() == null) ? new Date() : 
				userMaintainReportDto.getToDate());
			parameters.put("userType", userMaintainReportDto.getUserType());
			parameters.put("resourcePath", Constants.REPORT_RESOURCE_PATH);
			
			boolean isReportGenerated = reportGenerator.generateAndExportReport(reportJrxml, reportName, parameters);
			if(!isReportGenerated)
				return generateEmptyResponse(request, "Quarterly updated user report generation failed");
			
			File reportFile = new File(reportName);
			return (reportFile.exists()) ? getResponseEntityStream(reportFile, reportName) : 
				generateEmptyResponse(request, "Quarterly updated user report generation failed");
		}catch (Exception e) {
			log.error("Report stream population failed {}", e.getMessage());
			return generateFailureResponse(request, e);
		}
	}//generateQuarterlyUpdatedUserReport
	
	@PostMapping("/quarterlyDeletedUserReport")
	public ResponseEntity<?> generateQuarterlyDeletedUserReport(HttpServletRequest request, 
			@RequestBody @Valid UserMaintainReportDto userMaintainReportDto) {
		String reportJrxml = reportGenerator.getRptFileDir().concat("/quarterlyDeletedUser.jrxml");
		String reportName = reportGenerator.getGenFileDir().concat("/QuarterlyDeletedUserReport.pdf");		
		try {
			reportGenerator.cleanupExistingReport(reportJrxml, reportName);
			
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("fromDate", userMaintainReportDto.getFromDate());
			parameters.put("toDate", (userMaintainReportDto.getToDate() == null) ? new Date() : 
				userMaintainReportDto.getToDate());
			parameters.put("userType", userMaintainReportDto.getUserType());
			parameters.put("resourcePath", Constants.REPORT_RESOURCE_PATH);
			
			boolean isReportGenerated = reportGenerator.generateAndExportReport(reportJrxml, reportName, parameters);
			if(!isReportGenerated)
				return generateEmptyResponse(request, "Quarterly deleted user report generation failed");
			
			File reportFile = new File(reportName);
			return (reportFile.exists()) ? getResponseEntityStream(reportFile, reportName) : 
				generateEmptyResponse(request, "Quarterly deleted user report generation failed");
		}catch (Exception e) {
			log.error("Report stream population failed {}", e.getMessage());
			return generateFailureResponse(request, e);
		}
	}//generateQuarterlyDeletedUserReport
	
}