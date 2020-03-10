package com.flowableexample.demoflowable.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowableexample.demoflowable.pojo.Approval;
import com.flowableexample.demoflowable.pojo.Article;

@Service
public class ArticleWorkflowService {

	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	IdmIdentityService idmIdentityService;

	private static final Logger log = LoggerFactory.getLogger(ArticleWorkflowService.class);
	
	@Transactional
    public Map<String,List<String>> startProcess(String assignee, Article article) {
		Map<String, Object> variables = new HashMap<>();
        variables.put("author", article.getAuthor());
        variables.put("url", article.getUrl());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("articleReview", variables);
        log.info("Article Submitted, Process Instance: "+processInstance.getProcessDefinitionId());
        List<String> tasksIds = new ArrayList<>();
        tasksIds.addAll(taskService.createTaskQuery()
        					.processInstanceId(processInstance.getId())
        					.list()
        					.stream()
        	                .map(Task::getId)
        	                .collect(Collectors.toList())
        		);
        taskService.setAssignee(tasksIds.get(0), article.getAuthor());
        taskService.complete(tasksIds.get(0), variables);
        tasksIds.addAll(taskService.createTaskQuery()
				.processInstanceId(processInstance.getId())
				.list()
				.stream()
                .map(Task::getId)
                .collect(Collectors.toList()));
        taskService.addCandidateGroup(tasksIds.get(1),assignee);
        Map<String,List<String>> processInfos = new HashMap<>();
        processInfos.put(processInstance.getId(),
        		/*taskService.createTaskQuery()
        					.processInstanceId(processInstance.getId())
        					.list()
        					.stream()
        	                .map(Task::getId)
        	                .collect(Collectors.toList())*/
        		tasksIds
        		);
        return processInfos;
    }
  
    @Transactional
    public List<Article> getTasks(String assignee) {
    	log.info("Fetching tasks of "+assignee);
        List<Task> tasks = taskService.createTaskQuery()
          .taskCandidateGroup(assignee)
          .list();
        return tasks.stream()
          .map(task -> {
              Map<String, Object> variables = taskService.getVariables(task.getId());
              return new Article(task.getId(), (String) variables.get("author"), (String) variables.get("url"));
          })
          .collect(Collectors.toList());
    }
    
    @Transactional
    public List<Article> getTasks() {
    	log.info("fetching all tasks");
        List<Task> tasks = taskService.createTaskQuery()
          .list();
        return tasks.stream()
          .map(task -> {
              Map<String, Object> variables = taskService.getVariables(task.getId());
              return new Article(task.getId(), (String) variables.get("author"), (String) variables.get("url"));
          })
          .collect(Collectors.toList());
    }
  
    @Transactional
    public String submitReview(Approval approval) {
    	log.info("reviewing Article "+ approval.getId() + " with satus "+approval.isStatus());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("approved", approval.isStatus());
        taskService.complete(approval.getId(), variables);
        return approval.getId();
    }
}
