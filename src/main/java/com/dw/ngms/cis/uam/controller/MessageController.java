package com.dw.ngms.cis.uam.controller;

import javax.servlet.http.HttpServletRequest;

import com.dw.ngms.cis.uam.config.SendBlueMailService;
import com.dw.ngms.cis.uam.dto.MailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dw.ngms.cis.exception.ExceptionConstants;
import com.dw.ngms.cis.exception.ResponseBuilderAgent;
import com.dw.ngms.cis.exception.RestResponse;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
public class MessageController implements ExceptionConstants {

	@Autowired
	private ResponseBuilderAgent responseBuilderAgent;
	
	/**
	 * This is to generate failure response 
	 * 
	 * @param request
	 * @param exception
	 * @return ResponseEntity
	 */
	protected ResponseEntity<?> generateFailureResponse(HttpServletRequest request, Exception exception) {
		RestResponse errorResponse = responseBuilderAgent.createFailureResponse(exception, request.getRequestURI(), RESPONSE_ERROR_CODE);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}//generateFailureResponse

	/**
	 * This is to generate empty response 
	 * 
	 * @param request
	 * @param responseMessage
	 * @return ResponseEntity
	 */
	protected ResponseEntity<?> generateEmptyResponse(HttpServletRequest request, String responseMessage) {
		RestResponse emptyResponse = responseBuilderAgent.createErrorResponse(request.getRequestURI(), responseMessage, RESPONSE_ERROR_CODE);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyResponse);
	}//generateEmptyResponse


	protected ResponseEntity<?> generateEmptyWithOKResponse(HttpServletRequest request, String responseMessage) {
		RestResponse emptyResponse = responseBuilderAgent.createErrorResponse(request.getRequestURI(), responseMessage, RESPONSE_SUCCESS_CODE);
		return ResponseEntity.status(HttpStatus.OK).body(emptyResponse);
	}//generateEmptyResponse


	public String sendMail(MailDTO mailDTO) {
		SendBlueMailService http = new SendBlueMailService(ExceptionConstants.sendBlueMailLinkURL, ExceptionConstants.sendBlueMailPassword);
		Map<String, String> attr = new HashMap<>();
		attr.put("HEADER", mailDTO.getHeader());
		attr.put("SUBJECT", mailDTO.getSubject());
		attr.put("BODY", mailDTO.getBody());
		attr.put("FOOTER", mailDTO.getFooter());

		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "text/html; charset=iso-8859-1");
		Map<String, Object> data = new HashMap<>();
		data.put("id", ExceptionConstants.mailTemplateID);
		data.put("to", mailDTO.getToAddress());
		data.put("attr", attr);
		data.put("headers", headers);
		String mailResponse = http.send_transactional_template(data);

		return mailResponse;
	}

}
