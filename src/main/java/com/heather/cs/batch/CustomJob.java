package com.heather.cs.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;

public interface CustomJob {

	Job job();
	Step step();
	String getJobName();
}
