package com.app.runner;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.app.constant.Constants;

import lombok.extern.java.Log;


@Component
@Log
public class JobRunner {
	private JobLauncher simpleJobLauncher;
	private Job demo1;
	@Autowired
	public JobRunner(JobLauncher jobLauncher, Job demo1) {		
		this.simpleJobLauncher = jobLauncher;
		this.demo1 = demo1;
	}
@Async
public void runBatchJob()
{
	JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
	jobParametersBuilder.addString(Constants.FILE_NAME_CONTEXT_KEY,"employee.csv");
    jobParametersBuilder.addDate("date", new Date(),true);
    runJob(demo1,jobParametersBuilder.toJobParameters());
}

@SuppressWarnings("unused")
public void runJob(Job job, JobParameters parameters) {
	
		try {
			JobExecution jobExecution= simpleJobLauncher.run(job,parameters);
		} catch (JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
			log.info("job with filename {} is already completed "+parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));
			
		} catch (JobExecutionAlreadyRunningException e) {
			log.info("job with filename {} is already ruuning "+parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));
		}
		
	}
	

}
