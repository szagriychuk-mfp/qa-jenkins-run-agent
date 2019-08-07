package com.qaprosoft.jagent.db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.qaprosoft.jagent.db.dao.JobsQueueDAO;

public class DBService {

	private static final String CNFG = "mybatis.config.xml";
	private static final String ENV = "default";

	private SqlSessionFactory sf;

	private JobsQueueDAO jobsQueueDAO;

	public DBService() {
		try {
			sf = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(CNFG), ENV);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		jobsQueueDAO = new JobsQueueDAO(sf);
	}

	public JobsQueueDAO getJobsQueueDAO() {
		return jobsQueueDAO;
	}

}
