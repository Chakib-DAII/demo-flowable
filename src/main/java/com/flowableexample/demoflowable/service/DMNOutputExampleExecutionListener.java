package com.flowableexample.demoflowable.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DMNOutputExampleExecutionListener implements ExecutionListener{

	private static final Logger log = LoggerFactory.getLogger(DMNOutputExampleExecutionListener.class);

	@Override
	public void notify(DelegateExecution execution) {
		log.info("\nExecution Info: "+ execution.getProcessDefinitionId() 
				+ "\nTask: "+ execution.getCurrentFlowElement().getName()
				+"\nEvent: "+ execution.getCurrentFlowableListener().getEvent()
				+"\nVariables: \n"+ execution.getVariables().toString());		
	}

}
