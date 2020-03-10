package com.flowableexample.demoflowable;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.flowableexample.demoflowable.service.DMNOutputExampleExecutionListener;
import com.flowableexample.demoflowable.service.ExampleExecutionListener;
import com.flowableexample.demoflowable.service.ExampleTaskListener;
import com.flowableexample.demoflowable.service.ExecutionIdentifierService;
import com.flowableexample.demoflowable.service.PublishArticleService;
import com.flowableexample.demoflowable.service.SendMailService;

@SpringBootApplication
public class DemoFlowableApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoFlowableApplication.class, args);
	}

	@Bean
	public PublishArticleService publishArticleService() {
		return new PublishArticleService();
	}
	
	@Bean
	public SendMailService sendMailService() {
		return new SendMailService();
	} 
	
	@Bean
	public ExecutionIdentifierService executionIdentifierService() {
		return new ExecutionIdentifierService();

	} 
	
	@Bean
	public ExampleExecutionListener exampleExecutionListener() {
		return new ExampleExecutionListener();
	} 
	
	@Bean
	public ExampleTaskListener exampleTaskListener() {
		return new ExampleTaskListener();
	} 
	
	@Bean
	public DMNOutputExampleExecutionListener dmnOutputExampleExecutionListener(){
		return new DMNOutputExampleExecutionListener();
	}
	
	/*@Bean
    public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> customProcessEngineConfigurer() {
        return engineConfiguration -> {
            engineConfiguration.setValidateFlowable5EntitiesEnabled(false);
        };
    }*/
}
