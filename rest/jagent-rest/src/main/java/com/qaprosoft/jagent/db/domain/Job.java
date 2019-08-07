package com.qaprosoft.jagent.db.domain;

public class Job {

	private String jobName;

	private String projectName;

	public Job() {
	}

	public Job(String jobName, String projectName) {
		super();
		this.jobName = jobName;
		this.projectName = projectName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
