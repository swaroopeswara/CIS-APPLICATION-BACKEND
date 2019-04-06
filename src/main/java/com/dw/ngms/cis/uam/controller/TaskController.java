package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.dto.TaskDTO;
import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.jsonresponse.UserControllerResponse;
import com.dw.ngms.cis.uam.service.TaskService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by swaroop on 2019/04/06.
 */

@RestController
@RequestMapping("/cisorigin.uam/api/v1")
@CrossOrigin(origins = "*")
public class TaskController extends MessageController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/createTask")
    public ResponseEntity<?> createTask(HttpServletRequest request, @RequestBody @Valid Task task) {
        try {

            Long taskId = this.taskService.getTaskID();
            System.out.println(taskId);
            task.setTaskCode("TASK000" + Long.toString(taskId));
            Task taskService = this.taskService.saveTask(task);
            return ResponseEntity.status(HttpStatus.OK).body(taskService);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createTask


    @PostMapping("/closeTask")
    public ResponseEntity<?> closeTask(HttpServletRequest request, @RequestBody @Valid TaskDTO taskDTO) {
        try {
            UserControllerResponse userControllerResponse = new UserControllerResponse();
            String json = null;
            Gson gson = new Gson();
            System.out.println("taskDoneUserName "+taskDTO.getTaskDoneByUserName());
            Task task = this.taskService.getCloseTask(taskDTO.getTaskCode(),taskDTO.getTaskReferenceCode(),taskDTO.getTaskReferenceType());
            if (task != null && task.getTaskId() != null) {
                task.setTaskCode(taskDTO.getTaskCode());
                task.setTaskReferenceCode(taskDTO.getTaskReferenceCode());
                task.setTaskReferenceType(taskDTO.getTaskReferenceType());
                task.setTaskCLoseDESC(taskDTO.getTaskCloseDesc());
                task.setTaskDoneUserCode(taskDTO.getTaskDoneByUserCode());
                task.setTaskDoneUserName(taskDTO.getTaskDoneByUserName());
                this.taskService.saveTask(task);
                return ResponseEntity.status(HttpStatus.OK).body(task);
            }
            userControllerResponse.setMessage("No Task for the given task code and task reference code and reference type");
            json = gson.toJson(userControllerResponse);
            return ResponseEntity.status(HttpStatus.OK).body(json);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//createTask



    @GetMapping("/getAllTasks")
    public ResponseEntity<?> getAllExternalUsers(HttpServletRequest request, @RequestParam(required = false, defaultValue = "") String taskStatus,
                                                 @RequestParam(required = false, defaultValue = "") String taskType,
                                                 @RequestParam(required = false, defaultValue = "") String taskAllProvinceCode,
                                                 @RequestParam(required = false, defaultValue = "") String taskAllOCSectionCode,
                                                 @RequestParam(required = false, defaultValue = "") String taskAllOCRoleCode) {
        try {
            System.out.println("taskAllProvinceCode "+taskAllOCSectionCode);
            List<Task> taskList = taskService.getAllTasks(taskStatus,taskType,taskAllProvinceCode,taskAllOCSectionCode,taskAllOCRoleCode);
            return (CollectionUtils.isEmpty(taskList)) ? generateEmptyResponse(request, "Tasks not found")
                    : ResponseEntity.status(HttpStatus.OK).body(taskList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllExternalUsers
}
