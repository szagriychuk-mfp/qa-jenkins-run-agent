package com.qaprosoft.jagent.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.qaprosoft.jagent.db.DBService;
import com.qaprosoft.jagent.db.domain.JobRequest;
import com.qaprosoft.jagent.db.domain.RunStatus;

@Path("/run")
public class RunServer {

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response create(@FormParam("job_name") String jobName, @FormParam("job_params") String jobParams) {
		try {
			JobRequest rq = new JobRequest();
			rq.setJobName(jobName);
			rq.setJobStatus(RunStatus.NEW.toString());
			rq.setJobParams(jobParams);
			new DBService().getJobsQueueDAO().addNewRun(rq);
		} catch (Exception e) {
			return Response.status(400).entity(e.getMessage()).build();
		}

		return Response.status(201).build();
	}

	@GET
	@Path("/")
	@Produces("application/json")
	public Response getNewRuns() {
		String json;
		try {
			List<JobRequest> jobRequests = new DBService().getJobsQueueDAO().getNewJobRequests();
			json = new Gson().toJson(jobRequests);
		} catch (Exception e) {
			return Response.status(400).entity(e.getMessage()).build();
		}

		return Response.status(200).entity(json).build();
	}

	@PUT
	@Path("/{runId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setRunStatus(@PathParam("runId") Long runId, @FormParam("status") String status) {
		try {
			new DBService().getJobsQueueDAO().updateRunStatus(runId, RunStatus.valueOf(status));
		} catch (Exception e) {
			return Response.status(400).entity(e.getMessage()).build();
		}

		return Response.status(204).build();
	}

}
