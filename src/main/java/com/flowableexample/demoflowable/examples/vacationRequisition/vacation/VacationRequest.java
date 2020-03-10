package com.flowableexample.demoflowable.examples.vacationRequisition.vacation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.flowable.engine.RuntimeService;
import org.springframework.stereotype.Component;

@Component
public class VacationRequest implements Consumer<VacationRequestInput> {

    protected final RuntimeService runtimeService;

    public VacationRequest(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Override
    public void accept(VacationRequestInput vacationRequestInput) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeName", vacationRequestInput.getEmployeeName());
        variables.put("numberOfDays", vacationRequestInput.getNumberOfDays());
        variables.put("vacationMotivation", vacationRequestInput.getVacationMotivation());
        runtimeService.signalEventReceived("Start process instance", variables);
    }
}
