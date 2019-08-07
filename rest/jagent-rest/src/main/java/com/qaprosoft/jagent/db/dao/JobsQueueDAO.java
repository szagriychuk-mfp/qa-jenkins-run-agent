package com.qaprosoft.jagent.db.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.qaprosoft.jagent.db.domain.Job;
import com.qaprosoft.jagent.db.domain.JobRequest;
import com.qaprosoft.jagent.db.domain.RunStatus;

public class JobsQueueDAO {
	
	protected static final String NAMESPACE = "mappers";
	protected SqlSessionFactory sf;

	public JobsQueueDAO(SqlSessionFactory sf) {
		this.sf = sf;
	}

	public List<JobRequest> getNewJobRequests() {
		List<JobRequest> rqs = null;
		final SqlSession session = sf.openSession();
		try {
			final String query = NAMESPACE + ".getNewJobRequests";
			rqs = session.selectList(query);
		} finally {
			session.close();
		}
		return rqs;
	}
	
	public void addNewRun(JobRequest jobRequest) {
		final SqlSession session = sf.openSession();
		try {
			final String query = NAMESPACE + ".addNewRun";
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("rq", jobRequest);
			session.insert(query, args);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void addJob(Job job) {
		final SqlSession session = sf.openSession();
		try {
			final String query = NAMESPACE + ".addJob";
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("job", job);
			session.insert(query, args);
			session.commit();
		} finally {
			session.close();
		}
	}

	public void updateRunStatus(Long runId, RunStatus runStatus) {
		final SqlSession session = sf.openSession();
		try {
			final String query = NAMESPACE + ".updateRunStatus";
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("runId", runId);
			args.put("runStatus", runStatus.toString());
			session.update(query, args);
			session.commit();
		} finally {
			session.close();
		}
	}

}
