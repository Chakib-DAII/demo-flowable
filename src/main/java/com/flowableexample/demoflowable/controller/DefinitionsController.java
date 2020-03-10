package com.flowableexample.demoflowable.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefinitionsController {

    protected final RepositoryService repositoryService;
    
    protected final HistoryService historyService;

    public DefinitionsController(RepositoryService repositoryService, HistoryService historyService) {
        this.repositoryService = repositoryService;
        this.historyService = historyService;
    }

    @GetMapping("/latest-definitions")
    public  ResponseEntity<List> latestDefinitions() {
        try {
	    	return new ResponseEntity<List>(
	    			repositoryService.createProcessDefinitionQuery()
	                .latestVersion()
	                .list()
	                .stream()
	                .map(ProcessDefinition::getKey)
	                .collect(Collectors.toList()), HttpStatus.OK);	
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
		}
    }
    
    @GetMapping("/process-history")
    public ResponseEntity<List> history(@RequestParam String processInstanceId) {
    	//HistoryService historyService = ProcessEngine.getHistoryService();
    	try {
	    	return new ResponseEntity<List>(
	    /*List<HistoricActivityInstance> activities = */historyService
    	  .createHistoricActivityInstanceQuery()
    	  .processInstanceId(processInstanceId/*processInstance.getId()*/)
    	  .finished()
    	  .orderByHistoricActivityInstanceEndTime()
    	  .asc()
    	  .list(), HttpStatus.OK);	
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
		}
    	
    	//return activities;
    }

}