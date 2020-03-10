package com.flowableexample.demoflowable.service;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleTaskListener implements TaskListener{


	private static final Logger log = LoggerFactory.getLogger(ExampleTaskListener.class);
	
	@Override
	public void notify(DelegateTask execution) {
		log.info("\nExecution Info: "+ execution.getProcessDefinitionId() 
		+ "\nTask: "+ execution.getName()
		+"\nEvent: "+ execution.getEventName());	
	}

}
