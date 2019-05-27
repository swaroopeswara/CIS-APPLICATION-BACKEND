package com.dw.ngms.cis.im.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.dw.ngms.cis.controller.MessageController;
import com.dw.ngms.cis.im.entity.Requests;
import com.dw.ngms.cis.im.repository.RequestRepository;
import com.dw.ngms.cis.uam.dto.MailDTO;
import com.dw.ngms.cis.uam.entity.InternalRole;
import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.enums.LapseStatus;
import com.dw.ngms.cis.uam.service.TaskService;
import com.dw.ngms.cis.uam.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by swaroop on 2019/04/19.
 */
@Slf4j
@Service
public class RequestService {

	@Autowired
	private Environment env;
	@Autowired
	private TaskService taskService;
	@Autowired
	private UserService userService;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private MessageController messageController;
    
    public List<Requests> getAllRequests() {
        return this.requestRepository.findAll();
    }//getAllRequests

	public List<Requests> getRequestByUserCode(String userCode) {
		return this.requestRepository.getRequestByUserCode(userCode);
	}//getRequestByUserCode


	public List<Requests> getRequestsPaidInfoByProvince(String provinceCode) {
		return this.requestRepository.getRequestsPaidInfoByProvince(provinceCode);
	}//getRequestsPaidInfoByProvince


	public List<Requests> getRequestsPaidInfoByProvinceWeek(String provinceCode) {
		return this.requestRepository.getRequestsPaidInfoByProvinceWeek(provinceCode);
	}//getRequestsPaidInfoByProvinceWeek

	public List<Requests> getRequestsPaidInfoByProvinceMonth(String provinceCode) {
		return this.requestRepository.getRequestsPaidInfoByProvinceMonth(provinceCode);
	}//getRequestsPaidInfoByProvinceMonth

	public List<Requests> getAllRequestsPaidInfoByProvinceWeek() {
		return this.requestRepository.getAllRequestsPaidInfoByProvinceWeek();
	}//getAllRequestsPaidInfoByProvinceWeek


	public List<Requests> getAllRequestsPaidInfoByProvinceMonth() {
		return this.requestRepository.getAllRequestsPaidInfoByProvinceMonth();
	}//getAllRequestsPaidInfoByProvinceMonth



	public List<Requests> getAllRequestsPaidInfoByProvince() {
		return this.requestRepository.findAll();
	}//getRequestsPaidInfoByProvince


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

		Boolean isProcessed = this.populateAndSendMail(request, lapseTime, isLapsed);
		if(!isProcessed)
			throw new RuntimeException("Failed to send mail on lapse request "+requestCode);

		log.info("Lapse request updated successfully");
		
		return true;
	}//updateRequestOnLapse

	private boolean populateAndSendMail(Requests request, Integer lapseTime, boolean isLapsed) {
		try {
			MailDTO mail = this.populateMail(request, lapseTime, isLapsed);
			if(mail != null) {
				log.info("Sending mail {0}", mail.toString());			
				messageController.sendEmails(mail);			
			}
		} catch (Exception e) {
			log.error("Lapse request failed to send mail, error {0}", e.getMessage());
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}//populateAndSendMail

	private MailDTO populateMail(Requests request, Integer lapseTime, boolean isLapsed) {		
        Task task = taskService.getTask(request.getRequestCode());
        
        String mailBody = (isLapsed) ? getPropertyValue("lapsed.notification.body1", request.getRequestCode()) :
        		getPropertyValue("prelapse.notification.body1", request.getRequestCode());
        
        List<User> userList = (isLapsed) ? this.getReportingUsersToNotify(task) : this.getAssignedUsersToNotify(task);
        
    	return this.populateMailForUsers(request, mailBody, userList);
	}//populateMail

	/**
	 * @param task
	 * @param isLapsed
	 * @return
	 */
	private List<User> getAssignedUsersToNotify(Task task) {
		List<User> userList = task.getUserList();
		List<InternalRole> internalRoleList = task.getInternalRoleList();
		if(!CollectionUtils.isEmpty(internalRoleList)) {
			for(InternalRole internalRole : internalRoleList) {
				userList.addAll(userService.getAllInternalUsersByInternalRoleCode(internalRole.getInternalRoleCode()));
			}
			//Remove duplicates
			userList = userList.stream().distinct().collect(Collectors.toList());
		}
		return userList;
	}//getAssignedUsersToNotify

	private List<User> getReportingUsersToNotify(Task task) {
		List<User> userList = new ArrayList<>();
		if(task != null && task.getTaskDoneUserCode() != null) {
			User user = userService.findByUserCode(task.getTaskDoneUserCode());
			if(user != null)
				userList.add(user);
		}
		return userList;
	}//getReportingUsersToNotify

	private MailDTO populateMailForUsers(Requests request, String mailBody1, List<User> userList) {
		
        MailDTO mailDTO = new MailDTO();
        mailDTO.setMailSubject("Lapse request updated");

        mailDTO.getModel().put("body1", mailBody1);
        mailDTO.getModel().put("body2", "");
        mailDTO.getModel().put("body3", "");
        mailDTO.getModel().put("body4", "");
        mailDTO.getModel().put("firstName", "");
        mailDTO.getModel().put("FOOTER","CIS ADMIN");
        mailDTO.setMailFrom("cheifsurveyorgeneral@gmail.com");
    
        if(CollectionUtils.isEmpty(userList)) 
        	throw new RuntimeException("No users found to send notification mail");
        
        userList.forEach(user -> {
        	String email = user.getEmail();
        	if(!mailDTO.getMailsTo().contains(email)) {
        		mailDTO.getMailsTo().add(user.getEmail());        	
        		mailDTO.getUserNameMap().put(user.getEmail(), user.getFirstName());
        	}
        });        
		return mailDTO;
	}//populateMailForUsers
	
	private void updateAndPersistRequest(boolean isLapsed, Requests request) {
		request.setLapseStatus(isLapsed ? LapseStatus.LAPSED : LapseStatus.PRELAPSE);
		log.info("Request status update, {0}", request.getLapseStatus().name());
		requestRepository.save(request);
	}//updateAndPersistRequest

	private String getPropertyValue(String propertyKey, String requestCode) {
		String value = env.getRequiredProperty(propertyKey);		
		return (!StringUtils.isEmpty(value) && value.contains("{0}")) ? value.replace("{0}", requestCode) : value;
	}//getPropertyValue
}
