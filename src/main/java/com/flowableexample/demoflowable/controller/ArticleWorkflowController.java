package com.flowableexample.demoflowable.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flowableexample.demoflowable.pojo.Approval;
import com.flowableexample.demoflowable.pojo.Article;
import com.flowableexample.demoflowable.service.ArticleWorkflowService;

@RestController
@RequestMapping("/article")
public class ArticleWorkflowController {
	@Autowired
    private ArticleWorkflowService service;
  
    @PostMapping("/submit")
    public ResponseEntity<Map<String,List<String>>> submit(Principal principal, @RequestBody Article article) {
        try {
	    	return new ResponseEntity<Map<String,List<String>>>(service.startProcess(principal.getName(), article), HttpStatus.OK);	
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
		}
    }
  
    @GetMapping("/tasks")
    public ResponseEntity<List<Article>> getTasks(@RequestParam String assignee) {
        try {
	    	if(assignee == "all"){
	    		return new ResponseEntity<List<Article>>(service.getTasks(), HttpStatus.OK);
	    	}else
	    		return new ResponseEntity<List<Article>>(service.getTasks(assignee), HttpStatus.OK);	
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
		}
    }
  
    @PostMapping("/review")
    public ResponseEntity<String> review(@RequestBody Approval approval) {
        try {
	    	return new ResponseEntity<String>(service.submitReview(approval), HttpStatus.OK);	
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	
		}
    }
}
