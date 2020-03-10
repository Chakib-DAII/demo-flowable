package com.flowableexample.demoflowable.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutionIdentifierService implements JavaDelegate{

	private static final Logger log = LoggerFactory.getLogger(PublishArticleService.class);

	@Override
	public void execute(DelegateExecution execution) {
		log.info("Gateway Execution Info: "+ execution.getProcessDefinitionId() + " Task "+ execution.getCurrentFlowElement().getName());
	}
}
