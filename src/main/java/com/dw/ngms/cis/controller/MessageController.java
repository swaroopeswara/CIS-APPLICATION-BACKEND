package com.dw.ngms.cis.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.dw.ngms.cis.exception.ExceptionConstants;
import com.dw.ngms.cis.exception.ResponseBuilderAgent;
import com.dw.ngms.cis.exception.RestResponse;
import com.dw.ngms.cis.im.entity.ApplicationProperties;
import com.dw.ngms.cis.im.service.ApplicationPropertiesService;
import com.dw.ngms.cis.uam.configuration.MailConfiguration;
import com.dw.ngms.cis.uam.dto.MailDTO;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@Controller
public class MessageController implements ExceptionConstants {

    @Autowired
    private ResponseBuilderAgent responseBuilderAgent;

    @Autowired
    private MailConfiguration mailConfiguration;

    @Autowired
    private ApplicationPropertiesService appPropertiesService;
    
    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration freemarkerConfig;

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

    protected ResponseEntity<?> getResponseEntityStream(File reportFile, String reportName) throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + reportName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(reportFile.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        String mimeType = URLConnection.guessContentTypeFromName(reportFile.getName());
        if (mimeType == null)
            mimeType = "application/octet-stream";

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(reportFile.length())
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
    }//getResponseEntityStream

    protected String getResourcePath() throws IOException {
        URL url = this.getClass().getResource("/images/Logo_App.jpg");
        return (url != null && url.getPath() != null) ?
                url.getPath().replace("/images/Logo_App.jpg", "") : null;
    }//getResourcePath


    protected String sendSMS(String cellNumber, String message) throws IOException {


        String myURI = "https://api.bulksms.com/v1/messages";
        String replyText = null;
        // change these values to match your own account
        String myUsername = "dataworldproject";
        String myPassword = "dataworld";
        System.out.println("cellNumber " + cellNumber);
        System.out.println("message " + message);
        // the details of the message we want to send
        /*String myData = "{to: \"+27849410645\", body: \"test\"}";
		                  // {to:+27849410645,body:Hello welcome message}
		                  {"to":"+27849410645","body":"Hello welcome message"}
		                     {to: "+27849410645", body: "test"}*/

        String myData = "{to:\"" + cellNumber + "\"," +
                "body:\"" + message + "\"}";
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
        request.setRequestProperty("Content-Type", "application/json");

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

        return replyText;
    }//sendSMS

    public void sendEmail(MailDTO mailDTO) throws Exception {
        this.sendEmail(mailDTO.getModel(), mailDTO.getMailTo(), mailDTO.getMailSubject());
    }//sendEmail

    public void sendEmail(MailDTO mailDTO, InternetAddress cc) throws Exception {
        this.sendEmailWithBCC(mailDTO.getModel(), mailDTO.getMailTo(), mailDTO.getMailSubject(),cc);
    }//sendEmail

   /* public void sendEmail1(MailDTO mailDTO, InternetAddress cc) throws Exception {
        this.sendEmailWithBCC1(mailDTO.getModel(), mailDTO.getMailTo(), mailDTO.getMailSubject(),cc);
    }//sendEmail


    public void sendEmail2(MailDTO mailDTO, InternetAddress cc) throws Exception {
        this.sendEmailWithBCC2(mailDTO.getModel(), mailDTO.getMailTo(), mailDTO.getMailSubject(),cc);
    }//sendEmail
*/

    public void sendEmail(MailDTO mailDTO,String fileName, File file) throws Exception {
        this.sendEmail(mailDTO.getModel(), mailDTO.getMailTo(), mailDTO.getMailSubject(),fileName,file);
    }//sendEmail


    public void sendEmail(Map<String, Object> model, String mailTo, String mailSubject, String fileName, File file) throws Exception {
        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        helper.setTo(mailTo);
        helper.setText(getProcessedTemplate(model), true);
        helper.setSubject(mailSubject);
        helper.addAttachment(fileName, file);

        sendMailMessage(message);
    }//sendEmail

    public void sendEmails(MailDTO mail) {
    	Map<String, Object> model = mail.getModel();
    	if(!CollectionUtils.isEmpty(mail.getMailsTo())) {
    		mail.getMailsTo().forEach(mailTo -> {
    			try {    				
    				model.put("firstName", mail.getUserNameMap().get(mailTo));
					this.sendEmail(model, mailTo, mail.getMailSubject());
				} catch (Exception e) {
					Log.error("Failed to send email "+e.getMessage());
				}
    		});    		
    	}
	}//sendEmails


    public void sendEmailWithBCC(Map<String, Object> model, String mailTo, String mailSubject, InternetAddress cc) throws Exception {
        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(mailTo);
        helper.setText(getProcessedTemplate(model), true);
        helper.setSubject(mailSubject);
        String to = "swaroopeswara@gmail.com";
        InternetAddress[] parse = InternetAddress.parse(to ,true);
        message.setRecipients(javax.mail.Message.RecipientType.TO,parse);
        sendMailMessage(message);
    }//sendEmail


   /* public void sendEmailWithBCC1(Map<String, Object> model, String mailTo, String mailSubject, InternetAddress cc) throws Exception {
        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(mailTo);
        //helper.setCc(cc);
        helper.setText(getProcessedTemplate(model), true);
        helper.setSubject(mailSubject);
       *//*  String to = "swaroopeswara@gmail.com,sibusiso.dlamini@drdlr.gov.za,Bongani.Mtshali@drdlr.gov.za,Nondwe.Monyake@drdlr.gov.za,pieter.swart@drdlr.gov.za,George.mhlanga@drdlr.gov.za" +
                ",Hessie.Molotsi@drdlr.gov.za,Khaya.Mkoko@drdlr.gov.za,Manuel.Malapane@drdlr.gov.za,Rosalind.Mdubeki@drdlr.gov.za" +
                ",Mike.Caister@drdlr.gov.za,thembela.gazi@drdlr.gov.za,Anastasia.makgoba@drdlr.gov.za,Prince.mashele@drdlr.gov.za,Fani.Motimone@drdlr.gov.za" +
                ",Miliswa.Kula@drdlr.gov.za,coenraad.mouton@drdlr.gov.za,Silungile.Mthembu@drdlr.gov.za,Thembela.Gazi@drdlr.gov.za" +
                ",Wonder.Modipa@drdlr.gov.za,moagiabel.mohohlo@drdlr.gov.za,Elias.shongwe@drdlr.gov.za";*//*

        //String to = "swaroopeswara@gmail.com,swaroopragava23@gmail.com,vijay@dataworld.co.za";

    *//*  String to = "swaroopeswara@gmail.com,sibusiso.dlamini@drdlr.gov.za,Bongani.Mtshali@drdlr.gov.za,Nondwe.Monyake@drdlr.gov.za,pieter.swart@drdlr.gov.za,George.mhlanga@drdlr.gov.za" +
                ",Hessie.Molotsi@drdlr.gov.za,Khaya.Mkoko@drdlr.gov.za,Manuel.Malapane@drdlr.gov.za,Rosalind.Mdubeki@drdlr.gov.za" +
                ",Mike.Caister@drdlr.gov.za,thembela.gazi@drdlr.gov.za,Anastasia.makgoba@drdlr.gov.za,Prince.mashele@drdlr.gov.za,Fani.Motimone@drdlr.gov.za" +
                ",Miliswa.Kula@drdlr.gov.za,coenraad.mouton@drdlr.gov.za,Silungile.Mthembu@drdlr.gov.za,Thembela.Gazi@drdlr.gov.za" +
                ",Wonder.Modipa@drdlr.gov.za,moagiabel.mohohlo@drdlr.gov.za,Elias.shongwe@drdlr.gov.za";*//*

        //String to = "swaroopeswara@gmail.com,swaroopragava23@gmail.com,vijay@dataworld.co.za";

        String to = "Hessie.Molotsi@drdlr.gov.za,Khaya.Mkoko@drdlr.gov.za,Manuel.Malapane@drdlr.gov.za,Rosalind.Mdubeki@drdlr.gov.za,Mike.Caister@drdlr.gov.za";
        //String to = "swaroopeswara@gmail.com,sibusiso.dlamini@drdlr.gov.za,Bongani.Mtshali@drdlr.gov.za,Nondwe.Monyake@drdlr.gov.za,pieter.swart@drdlr.gov.za,George.mhlanga@drdlr.gov.za,Hessie.Molotsi@drdlr.gov.za,Khaya.Mkoko@drdlr.gov.za,Manuel.Malapane@drdlr.gov.za,Rosalind.Mdubeki@drdlr.gov.za,Mike.Caister@drdlr.gov.za,thembela.gazi@drdlr.gov.za,Anastasia.makgoba@drdlr.gov.za,Prince.mashele@drdlr.gov.za,Fani.Motimone@drdlr.gov.za";

        InternetAddress[] parse = InternetAddress.parse(to ,true);
        message.setRecipients(javax.mail.Message.RecipientType.TO,parse);
        sendMailMessage(message);
    }//sendEmail



    public void sendEmailWithBCC2(Map<String, Object> model, String mailTo, String mailSubject, InternetAddress cc) throws Exception {
        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(mailTo);
        //helper.setCc(cc);
        helper.setText(getProcessedTemplate(model), true);
        helper.setSubject(mailSubject);
    *//*  String to = "swaroopeswara@gmail.com,sibusiso.dlamini@drdlr.gov.za,Bongani.Mtshali@drdlr.gov.za,Nondwe.Monyake@drdlr.gov.za,pieter.swart@drdlr.gov.za,George.mhlanga@drdlr.gov.za" +
                ",Hessie.Molotsi@drdlr.gov.za,Khaya.Mkoko@drdlr.gov.za,Manuel.Malapane@drdlr.gov.za,Rosalind.Mdubeki@drdlr.gov.za" +
                ",Mike.Caister@drdlr.gov.za,thembela.gazi@drdlr.gov.za,Anastasia.makgoba@drdlr.gov.za,Prince.mashele@drdlr.gov.za,Fani.Motimone@drdlr.gov.za" +
                ",Miliswa.Kula@drdlr.gov.za,coenraad.mouton@drdlr.gov.za,Silungile.Mthembu@drdlr.gov.za,Thembela.Gazi@drdlr.gov.za" +
                ",Wonder.Modipa@drdlr.gov.za,moagiabel.mohohlo@drdlr.gov.za,Elias.shongwe@drdlr.gov.za";*//*

        //String to = "swaroopeswara@gmail.com,swaroopragava23@gmail.com,vijay@dataworld.co.za";

        String to = "thembela.gazi@drdlr.gov.za,Anastasia.makgoba@drdlr.gov.za,Prince.mashele@drdlr.gov.za,Fani.Motimone@drdlr.gov.za,swaroopeswara@gmail.com";
        //String to = "swaroopeswara@gmail.com,sibusiso.dlamini@drdlr.gov.za,Bongani.Mtshali@drdlr.gov.za,Nondwe.Monyake@drdlr.gov.za,pieter.swart@drdlr.gov.za,George.mhlanga@drdlr.gov.za,Hessie.Molotsi@drdlr.gov.za,Khaya.Mkoko@drdlr.gov.za,Manuel.Malapane@drdlr.gov.za,Rosalind.Mdubeki@drdlr.gov.za,Mike.Caister@drdlr.gov.za,thembela.gazi@drdlr.gov.za,Anastasia.makgoba@drdlr.gov.za,Prince.mashele@drdlr.gov.za,Fani.Motimone@drdlr.gov.za";

        InternetAddress[] parse = InternetAddress.parse(to ,true);
        message.setRecipients(javax.mail.Message.RecipientType.TO,parse);
        sendMailMessage(message);
    }//sendEmail*/

    public void sendEmail(Map<String, Object> model, String mailTo, String mailSubject) throws Exception {
        MimeMessage message = sender.createMimeMessage();
        
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(mailTo);
        helper.setText(getProcessedTemplate(model), true);
        helper.setSubject(mailSubject);

        sendMailMessage(message);
    }//sendEmail

	private void sendMailMessage(MimeMessage message) {
		ApplicationProperties property = appPropertiesService.getProperty("SEND_MAIL");
		if(property != null && property.getKeyValue() != null && 
				property.getKeyValue().equalsIgnoreCase("false")) {
			log.warn("Mail configuration disabled, no mail sent");
			return;
		}else{
            log.warn("Test mail");
            sender.send(message);
        }

	}//sendMailMessage
    
	private String getProcessedTemplate(Map<String, Object> model) throws TemplateNotFoundException,
			MalformedTemplateNameException, ParseException, IOException, TemplateException {
		// Using a subfolder such as /templates here
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");

        Template t = freemarkerConfig.getTemplate("email-template.ftl");
        return FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
	}//getProcessedTemplate

}

