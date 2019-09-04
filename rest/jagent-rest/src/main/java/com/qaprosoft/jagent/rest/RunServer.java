package com.qaprosoft.jagent.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.GsonBuilder;
import com.qaprosoft.jagent.db.DBService;
import com.qaprosoft.jagent.db.domain.JobRequest;
import com.qaprosoft.jagent.db.domain.RunStatus;

@Path("/run")
public class RunServer {

	private final static Logger LOGGER = Logger.getLogger(RunServer.class.getSimpleName());

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
			LOGGER.warning(Status.BAD_REQUEST + ": " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

		LOGGER.info(String.format("New run for job '%s' was created", jobName));
		return Response.status(Status.CREATED).build();
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNewRuns(@QueryParam("status") String status) {
		String json;
		try {
			List<JobRequest> jobRequests;
			if (status == null) {
				jobRequests = new DBService().getJobsQueueDAO().getNewJobRequests();
			} else if (status != null && RunStatus.NEW.equals(RunStatus.valueOf(status))) {
				jobRequests = new DBService().getJobsQueueDAO().getNewJobRequests();
			} else if (status != null && RunStatus.IN_PROGRESS.equals(RunStatus.valueOf(status))) {
				jobRequests = new DBService().getJobsQueueDAO().getJobRequestsInProgress();
			} else {
				return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN)
						.entity(String.format("status %s is not supported for filtering", status)).build();
			}
			json = new GsonBuilder().setPrettyPrinting().serializeNulls().create().toJson(jobRequests).concat("\r\n");
			LOGGER.info(String.format("Sending %d run(s) with status '%s'", jobRequests.size(), status));
			return Response.ok().type(MediaType.APPLICATION_JSON).entity(json).build();
		} catch (Exception e) {
			LOGGER.warning(Status.BAD_REQUEST + ": " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path("/{runId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setRunStatusAndCiRunId(@PathParam("runId") Long runId, @FormParam("status") String status,
			@FormParam("ci_run_id") Integer ciRunId) {
		try {
			if (ciRunId == null) {
				new DBService().getJobsQueueDAO().updateRunStatus(runId, RunStatus.valueOf(status));
			} else {
				new DBService().getJobsQueueDAO().updateRunStatusAndCiRunId(runId, RunStatus.valueOf(status), ciRunId);
			}
		} catch (Exception e) {
			LOGGER.warning(Status.BAD_REQUEST + ": " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

		LOGGER.info(String.format("Run %d was updated", runId));
		return Response.status(Status.NO_CONTENT).build();
	}

}
