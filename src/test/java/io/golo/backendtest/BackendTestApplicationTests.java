package io.golo.backendtest;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import io.golo.backendtest.service.utility.PeekWindow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BackendTestApplicationTests {
	
	private static final String MONITOR_URL = "https://api.test.paysafe.com/accountmanagement/monitor";

	@Autowired 
	MockMvc mockMvc;
	
	/**
	 * Before start/stop commands being issued
	 * NO_CONTENT 204 is returned - via mockMvc (without server started)
	 */
	@Test
	public void statusNoContent() throws Exception {
		mockMvc.perform(get("/monitor/status?url=" + MONITOR_URL))
		.andDo(print())
		.andExpect(status().isNoContent());
	}
	
	/**
	 * Full end-to-end integration test (app should be running locally), equivalent to sending status before start/stop commands:
	 * curl -X GET -H 'Content-Type: application/json' -i 'http://localhost:8080/monitor/status?url=https://api.test.paysafe.com/accountmanagement/monitor'
	 */
	@Ignore
	@Test
	public void statusBeforeStartStopShouldReturnNoContent() { // needs server started
		final TestRestTemplate restTemplate = new TestRestTemplate();
		
		final String urlGetStatus = "http://localhost:8080/monitor/status?url=" + MONITOR_URL;
		
		final List<PeekWindow> peekWindowList = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		ResponseEntity<List<PeekWindow>> responseGetStatus = (ResponseEntity<List<PeekWindow>>) restTemplate.getForEntity(urlGetStatus, peekWindowList.getClass());
		assertEquals(HttpStatus.NO_CONTENT, responseGetStatus.getStatusCode());
	}

}
