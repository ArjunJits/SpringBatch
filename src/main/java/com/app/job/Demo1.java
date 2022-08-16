package com.app.job;

//import java.io.File;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;

import com.app.dto.EmployeeDTO;
import com.app.mapper.EmployeeFileRowMapper;
import com.app.model.Employee;
import com.app.processor.EmployeeProcessor;

@Configuration
@ComponentScan(basePackages = {"com.app.processor"})
public class Demo1 {

private JobBuilderFactory jobBuilderFactory;
private StepBuilderFactory stepBuilderFactory;
private EmployeeProcessor employeeProcessor;
private DataSource dataSource;

@Autowired
public Demo1(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
		EmployeeProcessor employeeProcessor, DataSource dataSource) {
	this.jobBuilderFactory = jobBuilderFactory;
	this.stepBuilderFactory = stepBuilderFactory;
	this.employeeProcessor = employeeProcessor;
	this.dataSource = dataSource;
}

@Qualifier(value="demo1")
@Bean
public Job demo1Job() throws Exception
{
	return this.jobBuilderFactory.get("demo1")
			   .start(step1Demo1())
			   .build();
}

@Bean
public Step step1Demo1()  throws Exception{
	
	return this.stepBuilderFactory.get("step1")	
			 .<EmployeeDTO,Employee>chunk(2)
			 .reader(employeeReader())
			 .processor(employeeProcessor)
			 .writer(employeeDBWriterDefault())
			 .build();
}



@Bean
@StepScope
public FlatFileItemReader<EmployeeDTO> employeeReader() throws Exception {
	FlatFileItemReader<EmployeeDTO> reader = new FlatFileItemReader<>();
	 reader.setResource(new ClassPathResource("employee.csv"));
	 reader.setLineMapper(new DefaultLineMapper<EmployeeDTO>() {{
	  setLineTokenizer(new DelimitedLineTokenizer() {{
		  setNames("employeeId", "firstName","lastName", "email","age");
	  }});
	  setFieldSetMapper(new EmployeeFileRowMapper());
	 }});
	return reader;
}

/*
 * @Bean
 * 
 * @StepScope Resource inputFileResource(@Value("#{jobParameters[fileName]}")
 * final String fileName) throws Exception{
 * 
 * return new ClassPathResource(fileName); }
 */
@Bean
public  JdbcBatchItemWriter<Employee> employeeDBWriterDefault() {
	JdbcBatchItemWriter<Employee> itemwriter = new JdbcBatchItemWriter<>();
	itemwriter.setDataSource(dataSource);
	itemwriter.setSql("insert into employee (employee_id,first_name,last_name,email,age) values"
			+ "(:employeeId,:firstName,:lastName,:email,:age)");
	itemwriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
	return itemwriter;
}


}

