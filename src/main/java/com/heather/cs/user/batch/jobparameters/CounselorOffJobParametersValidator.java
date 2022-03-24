package com.heather.cs.user.batch.jobparameters;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class CounselorOffJobParametersValidator implements JobParametersValidator {

	@Override
	public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
		if(jobParameters == null) {
			return;
		}
		String date = jobParameters.getString("date");
		if(!date.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
			throw new JobParametersInvalidException("The date format is incorrect. (date : " + date + ")");
		}
	}
}
