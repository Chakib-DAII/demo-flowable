package com.flowableexample.demoflowable.controller;

import java.util.List;
import java.util.Map;

import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowableexample.demoflowable.service.ProcessLifeCycleService;

import liquibase.pro.packaged.iF;

@RestController
@RequestMapping("/process")
public class ProcessController {
		
	@Autowired
	ProcessLifeCycleService processService;
	
	@Autowired
	ObjectMapper mapper;
	
    @PostMapping(value="/start/{processKey}/{trigger}")
	public ResponseEntity<String> startProcess(@PathVariable(name = "processKey") String processKey, @PathVariable(name = "trigger") String trigger, @RequestParam String triggerDefinition, @RequestBody(required=false) Map<String, Object> variables) {
		try {
				return new ResponseEntity<String>(processService.startProcess(processKey, variables, trigger, triggerDefinition), HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
    @PostMapping(value="/claim-pid/{processId}/{assignee}")
	public ResponseEntity<String> claimTaskByProcessId(@PathVariable(name="processId") String processId,@PathVariable(name="assignee") String assignee) {
    	try {
    		processService.claimTaskByProcessId(processId, assignee);
			return new ResponseEntity<String>(processId,HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
    
    @PostMapping(value="/progress-pid/{processId}")
	public ResponseEntity<String> progressProcessByProcessId(@PathVariable(name="processId") String processId,@RequestBody String variables) {
    	try {
    		processService.progressProcessByProcessId(processId, mapper.readValue(variables,Map.class));
			return new ResponseEntity<String>(processId,HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
    
    @PostMapping(value="/progress-tid/{taskId}")
	public ResponseEntity<String> progressProcessByTaskId(@PathVariable(name="taskId") String taskId, @RequestBody String variables) {
    	try {
    		processService.progressProcessByTaskId(taskId, mapper.readValue(variables,Map.class));
			return new ResponseEntity<String>(taskId, HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
    @PostMapping(value="/regress-pid/{processId}")
	public ResponseEntity<String> regressProcessByProcessId(@PathVariable(name="processId") String processId) {
    	try {
    		processService.regressProcessByProcessId(processId);
			return new ResponseEntity<String>(processId,HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
    }
    
    @PostMapping(value="/regress-tid/{taskId}")
	public ResponseEntity<String> regressProcessByTaskId(@PathVariable(name="taskId") String taskId) {
    	try {
    		processService.regressProcessByTaskId(taskId);
			return new ResponseEntity<String>(taskId, HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
    
    @PostMapping(value="/finish/{processId}")
	public ResponseEntity<String> finishProcess(@PathVariable(name="processId") String processId, @RequestParam String deleteReason) {
    	try {
    		processService.finishProcess(processId, deleteReason);
			return new ResponseEntity<String>(processId,HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
    }
	
    @GetMapping(value="/tasks-user")
	public ResponseEntity<String> getTasksUser(@RequestParam String assignee) {
    	try {
			return new ResponseEntity<String>(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(processService.getTasksUser(assignee)),HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
    }
    
    @GetMapping(value="/tasks-group")
	public ResponseEntity<String> getTasksGroup(@RequestParam String assignee) {
    	try {
			return new ResponseEntity<String>(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(processService.getTasksUser(assignee)),HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
    }
    
    @GetMapping(value="/finished-activities/{processKey}")
	public ResponseEntity<String> getFinishedProcessActivities(@PathVariable(name = "processKey") String processKey) {
    	try {
			return new ResponseEntity<String>(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(processService.getFinishedProcesses(processKey)),HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
    }
    
    @PostMapping(value="/trigger/{processId}/{trigger}")
	public ResponseEntity<String> triggerProcess(@PathVariable(name = "processId") String processId, @PathVariable(name = "trigger") String trigger, @RequestParam String triggerDefinition) {
		try {
				processService.triggerProcess(processId, trigger, triggerDefinition);
				return new ResponseEntity<>(HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
    
}
