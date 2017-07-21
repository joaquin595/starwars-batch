package com.starwars.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.stereotype.Component;

/**
 * Created by joaquinanton on 21/7/17.
 */

@Component
@Slf4j
public class PeopleStepListener {

    @AfterStep
    public void afterStep(StepExecution stepExecution){
        log.info(stepExecution.getSummary());
    }
}
