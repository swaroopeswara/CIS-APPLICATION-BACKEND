package com.dw.ngms.cis.uam.service;

import com.dw.ngms.cis.uam.entity.Task;
import com.dw.ngms.cis.uam.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swaroop on 2019/04/06.
 */

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;


    public Task saveTask(Task task) {
        return this.taskRepository.save(task);
    } //FindUserByEmail


  /*  public List<Task> getAllTasks(String taskStatus,String taskType,String taskAllProvinceCode,String taskAllOCSectionCode,String taskAllOCRoleCode){
        return this.taskRepository.getAllTasks(taskStatus,taskType,taskAllProvinceCode,taskAllOCSectionCode,taskAllOCRoleCode);
    }
*/

    public List<Task> getAllTasks(String taskStatus, String taskType, String taskAllProvinceCode, String taskAllOCSectionCode, String taskAllOCRoleCode) {
        return this.taskRepository.getAllTasksTest(taskStatus, taskType);
    }

    public List<Task> findAll(Specification<Task> task) {
        return this.taskRepository.findAll();
    }

    public List<Task> findByCriteria(String taskStatus, String taskType,String taskAllProvinceCode,String taskAllOCSectionCode,String taskAllOCRoleCode) {
        System.out.println("Task status " + taskStatus);
        System.out.println("Task taskAllOCRoleCode " + taskAllOCRoleCode);
        return this.taskRepository.findAll(new Specification<Task>() {
            @Override
            public Predicate toPredicate(javax.persistence.criteria.Root<Task> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (taskStatus != null && !StringUtils.isEmpty(taskStatus)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskStatus"), taskStatus)));
                }
                if (taskType != null && !StringUtils.isEmpty(taskType)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskType"), taskType)));
                }
                if (taskAllProvinceCode != null && !StringUtils.isEmpty(taskAllProvinceCode)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskAllProvinceCode"), taskAllProvinceCode)));
                }
                if (taskAllOCSectionCode != null && !StringUtils.isEmpty(taskAllOCSectionCode)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskAllOCSectionCode"), taskAllOCSectionCode)));
                }
                if (taskAllOCRoleCode != null && !StringUtils.isEmpty(taskAllOCRoleCode)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("taskAllOCRoleCode"), taskAllOCRoleCode)));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });

    }


    public Long getTaskID() {
        return this.taskRepository.getTaskID();
    }

    public Task getCloseTask(String taskCode, String taskReferenceCode, String taskReferenceType) {
        return this.taskRepository.getCloseTask(taskCode, taskReferenceCode, taskReferenceType);
    } //FindUserByEmail


}
