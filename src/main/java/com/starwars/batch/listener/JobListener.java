package com.starwars.batch.listener;

import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import org.springframework.batch.core.JobExecution;

/**
 * Created by joaquinanton on 21/7/17.
 */
@Component
public class JobListener implements JobExecutionListener {


    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
    }
}
