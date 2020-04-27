package com.sicredi.slc.config.batch;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.xml.bind.JAXBElement;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.sicredi.slc.generated.model.DOCComplexType;

@EnableBatchProcessing
@Configuration
public class SLCBatchConfig {

	@Autowired
    private JobBuilderFactory jobBuilderFactory;
 
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
 
    @Autowired
    private EntityManagerFactory entityManagerFactory;
 
    @Autowired
    private ResourceLoader resourceLoader;
    

    @Bean
    public MultiResourceItemReader<JAXBElement<DOCComplexType>> multiResourceItemReader() throws IOException {
        MultiResourceItemReader<JAXBElement<DOCComplexType>> resourceItemReader = new MultiResourceItemReader<>();
        ResourcePatternResolver resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        Resource[] resources = resourcePatternResolver.getResources("file:" + System.getProperty("user.dir") + "/slc/*.xml");
		resourceItemReader.setResources(resources);
        resourceItemReader.setDelegate(reader());
        return resourceItemReader;
    }
 
    @Bean
    public StaxEventItemReader<JAXBElement<DOCComplexType>> reader() {
    	StaxEventItemReader<JAXBElement<DOCComplexType>> reader = new StaxEventItemReader<>();
    	reader.setFragmentRootElementNames(new String[]{"DOC"});
    	Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
    	unmarshaller.setPackagesToScan("com.sicredi.slc.generated");
		reader.setUnmarshaller(unmarshaller);
        return reader;
    }
 
    @Bean
    public JpaItemWriter<DOCComplexType> writer() {
        JpaItemWriter<DOCComplexType> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
 
    @Bean
    public ItemProcessor<JAXBElement<DOCComplexType>, DOCComplexType> processor() {
        return (item) -> {
        	return item.getValue();
        };
    }
 
    @Bean
    public Job importSLCJob(JobExecutionListener listener) throws IOException {
        return jobBuilderFactory.get("importSLCJob")
        						.incrementer(new RunIdIncrementer())
        						.listener(listener)
        						.flow(step1())
        						.end()
        						.build();
    }
 
    @Bean
    public Step step1() throws IOException {
        return stepBuilderFactory.get("step1")
        						 .<JAXBElement<DOCComplexType>, DOCComplexType>chunk(100)
        						 .reader(multiResourceItemReader())
        						 .processor(processor())
        						 .writer(writer())
        						 .build();
    }
 
    @Bean
    public JobExecutionListener listener() {
        return new JobExecutionListener() {
        	@Override
        	public void beforeJob(JobExecution jobExecution) {}
        	@Override
            public void afterJob(JobExecution jobExecution) {}
        };
    }
}
