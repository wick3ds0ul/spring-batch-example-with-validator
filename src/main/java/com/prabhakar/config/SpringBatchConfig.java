package com.prabhakar.config;

import com.prabhakar.batch.Processor;
import com.prabhakar.batch.UserSkipPolicy;
import com.prabhakar.batch.UserValidationListener;
import com.prabhakar.batch.UserValidator;
import com.prabhakar.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<User> itemReader,
                   ItemWriter<User> itemWriter
    ) throws Exception {

        Step step = stepBuilderFactory.get("ETL-file-load")
                .<User, User>chunk(100)
                .reader(itemReader)
                .processor(compositeItemProcessor())
                .writer(itemWriter)
                .faultTolerant()
                .skipPolicy(jobSkipPolicy())
                .listener(userValidationListener())
                .listener(itemProcessor())
                .build();


        return jobBuilderFactory.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public FlatFileItemReader<User> itemReader() {

        FlatFileItemReader<User> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/users.csv"));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }


    @Bean
    public CompositeItemProcessor<User, User> compositeItemProcessor() throws Exception {
        CompositeItemProcessor<User, User> processor = new CompositeItemProcessor<>();
        List<ItemProcessor<User, User>> processors = Arrays.asList(itemProcessor(),validatingItemProcessor());
        processor.setDelegates(processors);
        processor.afterPropertiesSet();
        return processor;
    }

    @Bean
    public Processor itemProcessor() {
        return new Processor();
    }

    @Bean
    public LineMapper<User> lineMapper() {

        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "name", "dept", "salary");

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

    @Bean
    public ValidatingItemProcessor<User> validatingItemProcessor() {
        return new ValidatingItemProcessor<>(new UserValidator());
    }

    @Bean
    public UserValidationListener userValidationListener() {
        return new UserValidationListener();
    }

    @Bean
    public UserSkipPolicy jobSkipPolicy() {
        return new UserSkipPolicy();
    }

}
