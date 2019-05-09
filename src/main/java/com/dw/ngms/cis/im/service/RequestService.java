package com.dw.ngms.cis.im.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.im.repository.RequestRepository;
import com.dw.ngms.cis.uam.controller.MessageController;
import com.dw.ngms.cis.uam.dto.MailDTO;
import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.enums.LapseStatus;
import com.dw.ngms.cis.uam.service.TaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by swaroop on 2019/04/19.
 */
@Slf4j
@Service
public class RequestService {

	@Autowired
	private TaskService taskService;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private MessageController messageController;

    public List<Requests> getAllRequests() {
        return this.requestRepository.findAll();
    }//getAllCostCategories

    public List<Requests> getRequestByUserCodeProvinceCode(String userCode, String provinceCode) {
        return this.requestRepository.getRequestByUserCodeProvinceCode(userCode,provinceCode);
    }//getRequestByUserCodeProvinceCode


    public Long getRequestId() {
        return this.requestRepository.getRequestId();
    } //getRequestTypeID




    public Requests saveRequest(Requests requests) {
        return this.requestRepository.save(requests);
    } //saveRequest

    public Requests getRequestsByRequestCode(String requestCode) {
        return this.requestRepository.getRequestsByRequestCode(requestCode);
    } //saveRequest

	public boolean updateRequestOnLapse(String requestCode, Integer lapseTime, boolean isLapsed) {
		log.info("Processing lapse request");
		
		if (StringUtils.isEmpty(requestCode)) 
			throw new RuntimeException("Invalid request code");		
		
		Requests request = getRequestsByRequestCode(requestCode);
		
		if (request == null)
			throw new RuntimeException("No request found, code: "+requestCode);
		
		this.updateAndPersistRequest(isLapsed, request);

			Boolean isProcessed = this.populateTemplateAndSendMail(request, lapseTime);
		if(!isProcessed) 
			throw new RuntimeException("Failed to send mail on lapse request "+requestCode);
				
		log.info("Lapse request updated successfully");
		
		return true;
	}//updateRequestOnLapse

	private boolean populateTemplateAndSendMail(Requests request, Integer lapseTime) {
		Boolean isProcessed = Boolean.TRUE;
		List<MailDTO> mailList = this.populateMailTemplate(request, lapseTime);
		if(!CollectionUtils.isEmpty(mailList)) {
			for(MailDTO mail: mailList) {
				log.info("Sending mail {0}", mail.toString());
				try {
					messageController.sendEmail(mail);
				} catch (Exception e) {
					log.error("Lapse request failed to send mail, error {0}", e.getMessage());
					isProcessed = Boolean.FALSE;
				}
			}
		}
		return isProcessed;
	}//populateTemplateAndSendMail

	private List<MailDTO> populateMailTemplate(Requests request, Integer lapseTime) {
		List<MailDTO> mailList = new ArrayList<>();
		String mailBody = "";
        Task task = taskService.getTask(request.getRequestCode());
        //FIXME
		return mailList;
	}//populateMailTemplate

	private MailDTO getMailTemplate(Requests request, String mailBody, User user) {
		
        Map<String, Object> model = new HashMap<String, Object>();
        MailDTO mailDTO = new MailDTO();
        mailDTO.setMailSubject("Lapse request updated");

        model.put("body1", "Request ");
        model.put("body2", "");
        model.put("body3", "");
        model.put("body4", "");
        model.put("firstName", user.getFirstName() + ",");
        model.put("FOOTER","CIS ADMIN");
        mailDTO.setMailFrom("dataworldproject@gmail.com");
        mailDTO.setMailTo(user.getEmail());
        mailDTO.setModel(model);
    
		return mailDTO;
	}//populateMailTemplate
	
	private void updateAndPersistRequest(boolean isLapsed, Requests request) {
		request.setLapseStatus(isLapsed ? LapseStatus.LAPSED : LapseStatus.PRELAPSE);
		log.info("Request status update, {0}", request.getLapseStatus().name());
		requestRepository.save(request);
	}//updateAndPersistRequest

}
