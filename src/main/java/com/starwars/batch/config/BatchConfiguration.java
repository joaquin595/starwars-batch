package com.starwars.batch.config;

import com.starwars.batch.tasklet.HelloWorldTasklet;
import com.sun.tools.javac.api.ClientCodeWrapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by joaquinanton on 21/7/17.
 */

public class BatchConfiguration {

    @Bean
    public Step helloWorldStep(StepBuilderFactory stepBuilderFactory,
                               HelloWorldTasklet helloWorldTasklet){

        return stepBuilderFactory
                .get("helloWorldStep")
                .tasklet(helloWorldTasklet)
                .build();
    }

    @Bean
    public Job hellowWorldJob(JobBuilderFactory jobBuilderFactory,
                              Step hellowWorldStep) {

        return jobBuilderFactory
                .get("hellowWorldJob")
                .incrementer(new RunIdIncrementer())
                .start(hellowWorldStep)
                .build();
    }
}
