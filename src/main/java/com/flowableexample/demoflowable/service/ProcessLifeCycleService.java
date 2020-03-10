package com.flowableexample.demoflowable.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import liquibase.pro.packaged.iF;


@Service
public class ProcessLifeCycleService{

	 	@Autowired
	  	private RuntimeService runtimeService;

	    @Autowired
	    private TaskService taskService;
	    
	    @Autowired
	    private HistoryService historyService;
	    
		private static final Logger log = LoggerFactory.getLogger(ProcessLifeCycleService.class);
		
	    //@Transactional
	    public String startProcess(String processKey, Map<String, Object> variables, String trigger, String triggerDefinition) {
	    	log.info("Starting process");
	        if(trigger.equals("signal")) {
	        	log.info("Signal: "+triggerDefinition);	
	        	runtimeService.signalEventReceived(triggerDefinition, variables);
	        	return runtimeService.createExecutionQuery()
	        				.signalEventSubscriptionName(triggerDefinition)
	        				.singleResult().getProcessInstanceId();
	        
	        }else if(trigger.equals("message")) {
	        	log.info("Message: "+triggerDefinition);
	        	//runtimeService.startProcessInstanceByMessage(triggerDefinition,variables);
	        	return runtimeService.createExecutionQuery()
	        				.messageEventSubscriptionName(triggerDefinition)
	        				.singleResult().getProcessInstanceId();
	        
	        }else
	        	return runtimeService.startProcessInstanceByKey(processKey, variables).getProcessInstanceId()/*.getReferenceId()*/;
	    }

	    	    
	    //@Transactional
	    public void progressProcessByTaskId(String taskId, Map<String, Object> variables) {
	    	log.info("Progress process");
	    	taskService.complete(taskId, variables);
	    }
	    
	    //@Transactional
	    public void progressProcessByProcessId(String processInstanceId, Map<String, Object> variables) {
	    	log.info("Progress process");
	    	taskService.complete(taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult().getId()/*runtimeService.createProcessInstanceQuery()
	    									.processInstanceId(processInstanceId)
	    									.singleResult().getProcessDefinitionId()*/, variables);
	    }
	    
	    //@Transactional
	    public void regressProcessByTaskId(String currentTaskId ) {
	    	log.info("Regress process");
	    	Task currentTask = taskService.createTaskQuery()
	    								.taskId(currentTaskId)
	    								.singleResult();
	    	//currentTask.getExecutionId();
	    	
	    	HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
	    															.processInstanceId(currentTask.getProcessInstanceId())
	    															.orderByHistoricTaskInstanceEndTime()
	    															.desc().list().get(0);
	    	log.info(currentTask.getTaskDefinitionKey() +" to "+ historicTaskInstance.getTaskDefinitionKey());

	    	runtimeService.createChangeActivityStateBuilder()
	    				.processInstanceId(currentTask.getProcessInstanceId())
	    				.moveActivityIdTo(currentTask.getTaskDefinitionKey(), historicTaskInstance.getTaskDefinitionKey());
	    }
	    
	    //@Transactional
	    public void regressProcessByProcessId(String processInstanceId) {
	    	log.info("Regress process");
	    	String currentTaskId = taskService.createTaskQuery().processDefinitionKey(processInstanceId).singleResult().getId();
	    	/*runtimeService.createProcessInstanceQuery()
					.processInstanceId(processInstanceId)
					.singleResult().getActivityId();*/

	    	HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
					.processInstanceId(processInstanceId)
					.orderByHistoricTaskInstanceEndTime()
					.desc().list().get(0);
	    	
	    	Task currentTask = taskService.createTaskQuery()
					.taskId(currentTaskId)
					.singleResult();

	    	log.info(currentTask.getTaskDefinitionKey() +" to "+ historicTaskInstance.getTaskDefinitionKey());
	    	
	    	runtimeService.createChangeActivityStateBuilder()
			.processInstanceId(processInstanceId)
			.moveActivityIdTo(currentTask.getTaskDefinitionKey(), historicTaskInstance.getTaskDefinitionKey())        
			.changeState();
	    	

	    }
	   
	    //@Transactional
	    public void finishProcess(String processId, String deleteReason) {
	    	log.info("Finish process");
	    	runtimeService.deleteProcessInstance(processId, deleteReason);
	    }
	    
	    //@Transactional
	    public List<Map<String, Object>> getTasksUser(String assignee) {
	    	log.info("Fetching user tasks");
	    	return taskService.createTaskQuery().taskCandidateUser(assignee).list()
	        				.stream().map(Task -> {
	        					Map<String, Object> task = new HashMap<>();
	        					task.put("id", Task.getId());
	        					task.put("process instance id", Task.getProcessInstanceId());
	        					task.put("name", Task.getName());
	        					task.put("created", Task.getCreateTime());
	        					task.put("owner", Task.getOwner());
	        					task.put("assignee", Task.getAssignee());
	        					//
	        					return task;
	        				}).collect(Collectors.toList());
	        //return taskService.createTaskQuery().taskCandidateUser(assignee).list();
	    }
	    
	    //@Transactional
	    public List<Map<String, Object>> getTasksGroup(String assigneeGroup) {
	    	log.info("Fetching group tasks");
	    	return taskService.createTaskQuery().taskCandidateGroup(assigneeGroup).list()
	        		.stream().map(Task -> {
    					Map<String, Object> task = new HashMap<>();
    					task.put("id", Task.getId());
    					task.put("process instance id", Task.getProcessInstanceId());
    					task.put("name", Task.getName());
    					task.put("created", Task.getCreateTime());
    					task.put("owner", Task.getOwner());
    					task.put("assignee", Task.getAssignee());
    					//
    					return task;
    				}).collect(Collectors.toList());
	    }
	    
	  
	    //@Transactional
	    public List<Map<String, Object>> getFinishedProcessTasks(String processInsatnceId) {
	    	log.info("Fetching finished Tasks for Process"+ processInsatnceId);
	    	//List<HistoricActivityInstance> activities =
	    	  return historyService.createHistoricTaskInstanceQuery()
	    	   .processInstanceId(processInsatnceId)
	    	   .finished()
	    	   .orderByHistoricTaskInstanceEndTime().asc()
	    	   .list()
	    	   .stream().map(Activity -> {
	    		   Map<String, Object> act = new HashMap<>();
					act.put("id", Activity.getId());
					act.put("name", Activity.getName());
					act.put("started", Activity.getCreateTime());
					act.put("owner", Activity.getEndTime());
					act.put("assignee", Activity.getAssignee());
					//
	    		   return act;
	    	   }).collect(Collectors.toList());

	    	/*for (HistoricActivityInstance activity : activities) {
	    	  System.out.println(activity.getActivityId() + " took "
	    	    + activity.getDurationInMillis() + " milliseconds"
	    	    + activity.getStartTime()
	    	    + activity.getEndTime());
	    	}*/
	    	//return activities;
	    }

	    public List<Map<String, Object>> getFinishedProcesses(String processKey) {
	    	log.info("Fetching finished Tasks ");
	    	//List<HistoricActivityInstance> activities =
	    	  return historyService.createHistoricActivityInstanceQuery()
	    	   .processInstanceId(processKey)
	    	   .finished()
	    	   .orderByHistoricActivityInstanceEndTime().asc()
	    	   .list()
	    	   .stream().map(Activity -> {
	    		   Map<String, Object> act = new HashMap<>();
					act.put("id", Activity.getActivityId());
					act.put("name", Activity.getActivityName());
					act.put("started", Activity.getStartTime());
					act.put("owner", Activity.getEndTime());
					act.put("assignee", Activity.getAssignee());
					//
	    		   return act;
	    	   }).collect(Collectors.toList());

	    	/*for (HistoricActivityInstance activity : activities) {
	    	  System.out.println(activity.getActivityId() + " took "
	    	    + activity.getDurationInMillis() + " milliseconds"
	    	    + activity.getStartTime()
	    	    + activity.getEndTime());
	    	}*/
	    	//return activities;
	    }
	    
	    //user will become the assignee of the task, and the task will disappear from every task list of the other members of the group
	    public void claimTaskByTaskId(String taskId, String assignee) {
	    	log.info("Claiming Task process");
	    	taskService.claim(taskId, assignee);
	    }
	    
	    public void claimTaskByProcessId(String processInstanceId, String assignee) {
	    	log.info("Claiming Task process");
	    	taskService.claim(taskService.createTaskQuery()
	    								.processInstanceId(processInstanceId)
	    								//.desc()
	    								.singleResult().getId(), assignee);
	    }
	    
	    public void triggerProcess/*OrProcessDefinition*/(String processId, String trigger, String triggerDefinition) {
	    	log.info("trigger process "+ processId +" last Execution with :"+triggerDefinition +" "+trigger);
	    	//process executions
	    	List<Execution> processExecutions = runtimeService.createExecutionQuery()
	    		    								.processInstanceId(processId)
	    		    								.orderByProcessInstanceId()
	    		    								.desc().list();
	    	
	    	//process activities
	    	/*List<Execution> processExecutionsWithActivityIds = processExecutions.stream()
	    		    .filter(e -> e.getActivityId() != null)
	    		    .collect(Collectors.toList());*/
 	        
	    	if(trigger.equals("signal")) {

	        	log.info("Signal: "+triggerDefinition);	
	    		runtimeService.signalEventReceived(triggerDefinition/*, processExecutions.get(0).getId()*/);
	        
	    	}else if(trigger.equals("escalation")) {
	        
	    		//
	        
	    	}else if(trigger.equals("message")) {

	        	log.info("Message: "+triggerDefinition);	
	        	runtimeService.messageEventReceived(triggerDefinition, processExecutions.get(0).getId());
	        }
	        			
	    }
	    
}
