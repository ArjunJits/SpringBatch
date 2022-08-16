package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.runner.JobRunner;

@RestController
@RequestMapping("/run")
// url:localhost:8080/run/job
public class JobController {
    @Autowired
	private JobRunner jobRunner;
    
	/*
	 * @Autowired public JobController(JobRunner jobrunner) {
	 * this.jobRunner=jobrunner; }
	 */
    @GetMapping("/job")
    public String runBatchJob()
    {  jobRunner.runBatchJob();
    	return String.format("Batch job  demo1 submitted successfully");
    }
	
	
}
