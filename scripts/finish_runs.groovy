@Grab(group='org.apache.httpcomponents', module='httpclient', version='4.5.9')
@Grab(group='commons-httpclient', module='commons-httpclient', version='3.1')

import groovy.json.*

import org.apache.http.client.methods.*
import org.apache.http.entity.*
import org.apache.http.impl.client.*
import org.apache.http.message.BasicNameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.commons.httpclient.*

def restUrl = ''
def jenkinsUrl = ''
def jenkinsAuth = ("" + ":" + "").bytes.encodeBase64().toString()
def client = HttpClientBuilder.create().build()

def getRunsUrl = restUrl + '/jagent-rest/rest/run?status=IN_PROGRESS'
def getRuns = new HttpGet(getRunsUrl)
def response = client.execute(getRuns)
def bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
def jsonResponse = bufferedReader.getText()
println "Pending runs: \n" + jsonResponse

def slurper = new JsonSlurper()
def list = slurper.parseText(jsonResponse)

list.each {
	def ciRunId = it["ciRunId"]
	if(ciRunId != null)
	{
		// GET RUN STATUS
		def runId = it["runId"]
		def jobName = it["jobName"]
		def projectName = it["projectName"]
		def jobUrl = jenkinsUrl + "/job/" + projectName + "/job/" + jobName + "/" + ciRunId + "/api/json"
		def getJob = new HttpGet(jobUrl)	
 		getJob.setHeader("Authorization", "Basic " + jenkinsAuth);
		def responseJob = client.execute(getJob)
		bufferedReader = new BufferedReader(new InputStreamReader(responseJob.getEntity().getContent()))
		jsonResponse = bufferedReader.getText()
		assert 200 == responseJob.getStatusLine().getStatusCode()
		def obj = slurper.parseText(jsonResponse)	
		def result = obj["result"]
		println "run result from Jenkins: " + result
		
		// SET RUN STATUS
		def updateRunUrl = restUrl + '/jagent-rest/rest/run/' + runId
		def updateRun = new HttpPut(updateRunUrl)
		updateRun.setHeader("Authorization", "Basic " + jenkinsAuth)
		ArrayList<NameValuePair> updParameters = new ArrayList<NameValuePair>()
		updParameters.add(new BasicNameValuePair("status", result))
		updateRun.setEntity(new UrlEncodedFormEntity(updParameters, "UTF-8"))
		def responseUpd = client.execute(updateRun)
		assert 204 == responseUpd.getStatusLine().getStatusCode() 
		println "SETTING OF BUILD STATUS COMPLETED!"
	}
}
