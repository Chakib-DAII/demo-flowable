package com.flowableexample.demoflowable.configuration;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("deprecation")
//@Configuration
public class FlowableEngineConfiguration /*implements ProcessEngineConfigurationConfigurer*/{

	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	ProcessEngineConfiguration processEngineConfiguration;

	  
	
	
}
