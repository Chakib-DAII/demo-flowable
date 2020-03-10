package com.flowableexample.demoflowable.examples.migrationBpmnExample.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("reservationService")
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Override
    public void reserveTable() {
        LOGGER.info("\nTable reserved.");
    }

    @Override
    public void releaseTable() {
        LOGGER.info("\nTable released.");
    }

    @Override
    public boolean showUp() {
        //return true;
    	boolean showUp = new Random().nextBoolean();
        LOGGER.info("\n "+ (showUp == true ? "Show up":"Not show up"));
        return showUp;
    }

}
