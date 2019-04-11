package com.dw.ngms.cis.uam.report;

import java.io.File;
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

import com.dw.ngms.cis.uam.controller.MessageController;
import com.dw.ngms.cis.uam.dto.UserSummaryReportDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class ReportController extends MessageController {

	@Autowired
	private ReportGenerator reportGenerator;
	
	@PostMapping("/userSummaryReport")
	public ResponseEntity<?> generateUserSummaryReport(HttpServletRequest request, @RequestBody @Valid UserSummaryReportDto userSummaryReportDto) {
		String reportJrxml = "userSummary.jrxml";
		String reportName = "UserSummaryReport.pdf";
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("fromDate", userSummaryReportDto.getFromDate());
		parameters.put("toDate", userSummaryReportDto.getToDate());
		parameters.put("organisation", userSummaryReportDto.getOrganisation());
		parameters.put("section", userSummaryReportDto.getSection());
		parameters.put("sector", userSummaryReportDto.getSector());
		parameters.put("userType", userSummaryReportDto.getUserType());
		parameters.put("province", userSummaryReportDto.getProvince());
		parameters.put("resourcePath", getImagePath());
		try {
			boolean isReportGenerated = reportGenerator.generateAndExportReport(reportJrxml, parameters);
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

}