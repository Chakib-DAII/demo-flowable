package com.flowableexample.demoflowable.examples.vacationRequisition.vacation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationService.class);

    public void confirm(String employeeName, int numberOfDays) {
        LOGGER.info("Vacation request for {} for {} days was confirmed", employeeName, numberOfDays);
    }
}
