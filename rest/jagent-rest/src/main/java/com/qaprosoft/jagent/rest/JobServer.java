package com.qaprosoft.jagent.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.qaprosoft.jagent.db.DBService;
import com.qaprosoft.jagent.db.domain.Job;

@Path("/job")
public class JobServer {

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response create(@FormParam("project_name") String projectName, @FormParam("job_name") String jobName) {
		try {
			Job job = new Job();
			job.setProjectName(projectName);
			job.setJobName(jobName);
			new DBService().getJobsQueueDAO().addJob(job);
		} catch (Exception e) {
			return Response.status(400).entity(e.getMessage()).build();
		}

		return Response.status(201).build();

	}

}
