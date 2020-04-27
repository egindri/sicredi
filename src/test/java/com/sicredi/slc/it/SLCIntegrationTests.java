package com.sicredi.slc.it;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

import com.sicredi.slc.generated.model.DOCComplexType;

@ActiveProfiles("test")
@SpringBatchTest
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SLCIntegrationTests {

	private static final ParameterizedTypeReference<List<DOCComplexType>> TYPE_RESPONSE_FIND_ALL = new ParameterizedTypeReference<List<DOCComplexType>>(){};
	private static final String FIELD_STATUS = "Status";

	@Autowired
	protected TestRestTemplate testRestTemplate;
	
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    
	@LocalServerPort
	protected int port;

   
	@Test
	void emptyDBTest() {
		String url = buildBaseUrl();
		HttpMethod method = HttpMethod.GET;
		HttpEntity<Object> entity = new HttpEntity<>(new HttpHeaders());
		
		ResponseEntity<List<DOCComplexType>> response = testRestTemplate.exchange(url, method, entity, TYPE_RESPONSE_FIND_ALL);
		
		assertEquals(FIELD_STATUS, HttpStatus.OK, response.getStatusCode());
		assertEquals("Size", 0, response.getBody().size());
	}
	
	@Test
	void afterJobTest() throws Exception {
		String url = buildBaseUrl();
		HttpMethod method = HttpMethod.GET;
		HttpEntity<Object> entity = new HttpEntity<>(new HttpHeaders());
		JobExecution job = jobLauncherTestUtils.launchJob();

		ResponseEntity<List<DOCComplexType>> response = testRestTemplate.exchange(url, method, entity, TYPE_RESPONSE_FIND_ALL);
		
		assertEquals("Job Status", ExitStatus.COMPLETED, job.getExitStatus());
		assertEquals(FIELD_STATUS, HttpStatus.OK, response.getStatusCode());
		assertEquals("Size", 1, response.getBody().size());
	}
	
	@Test
	void getTest() throws Exception {
		String url = buildBaseUrl();
		HttpMethod method = HttpMethod.GET;
		HttpEntity<Object> entity = new HttpEntity<>(new HttpHeaders());
		jobLauncherTestUtils.launchJob();
		ResponseEntity<List<DOCComplexType>> responseGetAll = testRestTemplate.exchange(url, method, entity, TYPE_RESPONSE_FIND_ALL);
		Long id = responseGetAll.getBody().get(0).getHjid();
		url += id;

		ResponseEntity<DOCComplexType> response = testRestTemplate.exchange(url, method, entity, DOCComplexType.class);
		
		assertEquals(FIELD_STATUS, HttpStatus.OK, response.getStatusCode());
		assertEquals("Id", id, response.getBody().getHjid());
	}
	
	String buildBaseUrl() {
		return "http://localhost:" + port + "/slc/";
	}
	
	void assertEquals(String fieldName, Object expected, Object actual) {
		Assert.isTrue(expected.equals(actual), fieldName + " should be " + expected + " but is " + actual + "!");
	}
}
