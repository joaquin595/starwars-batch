package com.starwars.batch.listener;

/**
 * Created by joaquinanton on 21/7/17.
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RestListener {

    @AfterStep
    public void afterStep(StepExecution stepExecution) {
        log.info(stepExecution.getSummary());
    }
}
