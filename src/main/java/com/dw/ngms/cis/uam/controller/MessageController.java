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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
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

	protected ResponseEntity<?> generateEmptyWithOKResponse() {
		RestResponse emptyResponse = responseBuilderAgent.createErrorResponse();
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


	public String sendSMS(String cellNumber, String message) throws IOException {


		String myURI = "https://api.bulksms.com/v1/messages";
		String replyText = null;
		// change these values to match your own account
		String myUsername = "dataworldproject";
		String myPassword = "dataworld";
		System.out.println("cellNumber "+cellNumber);
		System.out.println("message "+message);
		// the details of the message we want to send
		/*String myData = "{to: \"+27849410645\", body: \"test\"}";
		                  // {to:+27849410645,body:Hello welcome message}
		                  {"to":"+27849410645","body":"Hello welcome message"}
		                     {to: "+27849410645", body: "test"}*/

		String myData = "{to:\""+cellNumber+"\"," +
				"body:\""+message+"\"}";
		/*String myData = "{to:"  + cellNumber +
				"," +
				"body:" + message +
				"}";*/

		System.out.println(myData);

		// if your message does not contain unicode, the "encoding" is not required:
		// String myData = "{to: \"1111111\", body: \"Hello Mr. Smith!\"}";

		// build the request based on the supplied settings
		URL url = new URL(myURI);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.setDoOutput(true);

		// supply the credentials
		String authStr = myUsername + ":" + myPassword;
		String authEncoded = Base64.getEncoder().encodeToString(authStr.getBytes());
		request.setRequestProperty("Authorization", "Basic " + authEncoded);

		// we want to use HTTP POST
		request.setRequestMethod("POST");
		request.setRequestProperty( "Content-Type", "application/json");

		// write the data to the request
		OutputStreamWriter out = new OutputStreamWriter(request.getOutputStream());
		out.write(myData);
		out.close();

		// try ... catch to handle errors nicely
		try {
			// make the call to the API
			InputStream response = request.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(response));
			
			while ((replyText = in.readLine()) != null) {
				System.out.println(replyText);
			}
			in.close();
		} catch (IOException ex) {
			System.out.println("An error occurred:" + ex.getMessage());
			BufferedReader in = new BufferedReader(new InputStreamReader(request.getErrorStream()));
			// print the detail that comes with the error
			while ((replyText = in.readLine()) != null) {
				System.out.println(replyText);
			}
			in.close();
		}
		request.disconnect();
		
		return  replyText;
	}
	
	
	}

