package com.dw.ngms.cis.uam.controller;

import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.entity.User;
import com.dw.ngms.cis.uam.service.TaskService;
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


    @GetMapping("/getAllTasks")
    public ResponseEntity<?> getAllExternalUsers(HttpServletRequest request, @RequestParam String taskStatus,
                                                 @RequestParam String taskType,
                                                 @RequestParam String taskAllProvinceCode,
                                                 @RequestParam String taskAllOCSectionCode,
                                                 @RequestParam String taskAllOCRoleCode) {
        try {
            List<Task> taskList = taskService.getAllTasks(taskStatus,taskType,taskAllProvinceCode,taskAllOCSectionCode,taskAllOCRoleCode);
            return (CollectionUtils.isEmpty(taskList)) ? generateEmptyResponse(request, "Tasks not found")
                    : ResponseEntity.status(HttpStatus.OK).body(taskList);
        } catch (Exception exception) {
            return generateFailureResponse(request, exception);
        }
    }//getAllExternalUsers
}
