package com.dw.ngms.cis.uam.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dw.ngms.cis.exception.ExceptionConstants;
import com.dw.ngms.cis.exception.ResponseBuilderAgent;
import com.dw.ngms.cis.exception.RestResponse;

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
}
