package com.qaprosoft.jagent.db;

import com.qaprosoft.jagent.db.domain.RunStatus;

public class DBMain {
	public static void main(String[] args) {
//		List<JobRequest> jobRequests = new DBService().getJobsQueueDAO().getNewJobRequests();
//		System.err.println(Arrays.toString(jobRequests.toArray()));

//		JobRequest rq = new JobRequest();
//		rq.setJobName("MFP-WEB-Default");
//		rq.setJobStatus("NEW");
//		rq.setJobParams("env=PREPROD&branch=kuan");
//		new DBService().getJobsQueueDAO().addNewRun(rq);

//		Job job = new Job();
//		job.setJobName("MFP-WEB-Admin");
//		job.setProjectName("qa");
//		new DBService().getJobsQueueDAO().addJob(job);

		new DBService().getJobsQueueDAO().updateRunStatus(1L, RunStatus.IN_PROGRESS);

//		Create Job:
//		curl -d "project_name=qa-api&job_name=MFP-API-INTEG" -H "Content-Type: application/x-www-form-urlencoded" -X POST http://localhost:8080/jagent-rest/rest/job
//		Create Run:
//		curl -d "job_name=MFP-API-INTEG&job_params=env%3DPREPROD%26branch%3Dinteg" -H "Content-Type: application/x-www-form-urlencoded" -X POST http://localhost:8080/jagent-rest/rest/run
//		Get runs:
//		curl http://localhost:8080/jagent-rest/rest/run
//		Update Run Status:
//		curl -d "status=IN_PROGRESS" -H "Content-Type: application/x-www-form-urlencoded" -X PUT http://localhost:8080/jagent-rest/rest/run/1
	}
}
