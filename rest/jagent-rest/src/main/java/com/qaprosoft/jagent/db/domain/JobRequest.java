package com.qaprosoft.jagent.db.domain;

import java.util.Date;

public class JobRequest {

	private String runId;

	private String jobName;

	private String jobStatus;

	private String jobParams;

	private Date createdAt;

	public JobRequest() {
	}

	public JobRequest(String jobName, String jobStatus, String jobParams, Date createdAt) {
		this.jobName = jobName;
		this.jobStatus = jobStatus;
		this.jobParams = jobParams;
		this.createdAt = createdAt;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getJobParams() {
		return jobParams;
	}

	public void setJobParams(String jobParams) {
		this.jobParams = jobParams;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	@Override
	public String toString() {
		return "JobRequest [runId=" + runId + ", jobName=" + jobName + ", jobStatus=" + jobStatus + ", jobParams="
				+ jobParams + ", createdAt=" + createdAt + "]";
	}

}
