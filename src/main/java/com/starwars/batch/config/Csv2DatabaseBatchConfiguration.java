package com.starwars.batch.config;

import com.starwars.batch.domain.People;
import com.starwars.batch.listener.PeopleStepListener;
import com.starwars.batch.repository.PeopleRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Repository;

/**
 * Created by joaquinanton on 21/7/17.
 */

public class Csv2DatabaseBatchConfiguration {

    @Bean
    public ItemReader<People> peopleReader(){

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[] {"name" , "birthYear", "gender", "height", "mass", "eyeColor", "hairColor", "skinColor"});

        BeanWrapperFieldSetMapper<People> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(People.class);

        DefaultLineMapper<People> lineMapper = new DefaultLineMapper<>();
        lineMapper.setFieldSetMapper(fieldSetMapper);
        lineMapper.setLineTokenizer(lineTokenizer);

        FlatFileItemReader<People> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/people.csv"));
        itemReader.setLineMapper(lineMapper);
        itemReader.setLinesToSkip(1);

        return itemReader;
    }

    @Bean
    public ItemWriter<People> itemWriter(PeopleRepository peopleRepository){

        RepositoryItemWriter<People> itemWriter = new RepositoryItemWriter<>();
        itemWriter.setRepository(peopleRepository);
        itemWriter.setMethodName("save");

        return itemWriter;
    }

    @Bean
    public Step csvStep(StepBuilderFactory stepBuilderFactory,
                        ItemWriter itemWriter,
                        ItemReader itemReader,
                        ItemProcessor itemProcessor,
                        PeopleStepListener peopleStepListener){

        return stepBuilderFactory.get("csvStep")
                .chunk(10)
                .listener(peopleStepListener)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public ItemProcessor<People,People> itemProcessor(){

        return new ItemProcessor<People,People>() {
            @Override
            public People process(People p) throws Exception {

                if(p.getGender().equals("n/a")){
                    p.setGender("droid");
                }

                return p;
            }
        };

    }
    @Bean
    public Job csvJob(JobBuilderFactory jobBuilderFactory,
                      Step csvStep) {

        return jobBuilderFactory
                .get("csvStep")
                .incrementer(new RunIdIncrementer())
                .start(csvStep)
                .build();
    }
}
