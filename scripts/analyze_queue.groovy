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

def getRunsUrl = restUrl + '/jagent-rest/rest/run'
def getRuns = new HttpGet(getRunsUrl)
def response = client.execute(getRuns)
def bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
def jsonResponse = bufferedReader.getText()
println "Pending runs: \n" + jsonResponse

def slurper = new JsonSlurper()
def list = slurper.parseText(jsonResponse)

list.each {
	// TRIGGER NEW RUN
	def runId = it["runId"]
	def jobName = it["jobName"]
	def projectName = it["projectName"]
	def jobUrl = jenkinsUrl + "/job/" + projectName + "/job/" + jobName + "/buildWithParameters?token=ciStart"
	
	def jobParams = it["jobParams"]
	if(jobParams != null) {
		jobUrl = jobUrl.concat('&').concat(jobParams)
	}
	println jobUrl

	def postJob = new HttpPost(jobUrl)	
	postJob.setHeader("Authorization", "Basic " + jenkinsAuth);
	def responseJob = client.execute(postJob)
	bufferedReader = new BufferedReader(new InputStreamReader(responseJob.getEntity().getContent()))
	jsonResponse = bufferedReader.getText()
	println "response: \n" + jsonResponse
	def location = responseJob.getLastHeader("Location")
	println location
	def queueId = location.getValue().find(/(?!item\/)\d+/)
	println "queueId: " + queueId
	println "JOB TRIGGERING COMPLETED!"
	
	// SET RUN STATUS IN_PROGRESS
	def updateRunUrl = restUrl + '/jagent-rest/rest/run/' + runId
	def updateRun = new HttpPut(updateRunUrl)
	updateRun.setHeader("Authorization", "Basic " + jenkinsAuth)
	ArrayList<NameValuePair> updParameters = new ArrayList<NameValuePair>()
	updParameters.add(new BasicNameValuePair("status", "IN_PROGRESS"))
	updateRun.setEntity(new UrlEncodedFormEntity(updParameters, "UTF-8"))
	def responseUpd = client.execute(updateRun)
	assert 204 == responseUpd.getStatusLine().getStatusCode() 
	println "SETTING OF BUILD IN_PROGRESS COMPLETED!"
	
	// RETRIEVE BUILD_NUMBER
	def getQueueUrl = jenkinsUrl + '/job/' + projectName + '/job/' + jobName + '/api/xml?tree=builds[number,queueId]&xpath=//build[queueId=' + queueId + ']'
	def getQueueJob = new HttpPost(getQueueUrl)
	getQueueJob.setHeader("Authorization", "Basic " + jenkinsAuth);
	def responseQueueJob = client.execute(getQueueJob)
	bufferedReader = new BufferedReader(new InputStreamReader(responseQueueJob.getEntity().getContent()))
	jsonResponse = bufferedReader.getText()
	while(jsonResponse.length() <= 0)
	{
		Thread.sleep(1000)
	}
//	println "build_num xml: " + jsonResponse
	def build = new XmlSlurper().parseText(jsonResponse)
	assert build instanceof groovy.util.slurpersupport.GPathResult
	def jobNumber = build.number
	println "jobNumber: " + jobNumber
	
	// UPDATE RUN STATUS
	updParameters = new ArrayList<NameValuePair>()
	updParameters.add(new BasicNameValuePair("status", "IN_PROGRESS"))
	updParameters.add(new BasicNameValuePair("ci_run_id", jobNumber.toString()))
	updateRun.setEntity(new UrlEncodedFormEntity(updParameters, "UTF-8"))
	responseUpd = client.execute(updateRun)
	assert 204 == responseUpd.getStatusLine().getStatusCode() 
	println "SETTING OF BUILD_NUMBER COMPLETED!"
}
