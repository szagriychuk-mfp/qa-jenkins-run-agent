package com.qaprosoft.jagent.rest;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.qaprosoft.jagent.db.DBService;
import com.qaprosoft.jagent.db.domain.Job;

@Path("/job")
public class JobServer {

	private final static Logger LOGGER = Logger.getLogger(JobServer.class.getSimpleName());

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
			LOGGER.warning(Status.BAD_REQUEST + ": " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

		LOGGER.info(String.format("New job '%s' was created", jobName));
		return Response.status(Status.CREATED).build();

	}

}
