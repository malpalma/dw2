package dw2;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import mp.dw.DWApp;
import mp.dw.DWMainController;
import mp.dw.DWRestController;
import mp.dw.DWSecurityConfig;
import mp.dw.DWService;
import mp.dw.DWUserSession;
import mp.dw.db.contr.ContractorDAOImpl;
import mp.dw.db.doc.AttachmentDAOImpl;
import mp.dw.db.doc.DocItemDAOImpl;
import mp.dw.db.doc.DocStageDAOImpl;
import mp.dw.db.doc.DocSumDAOImpl;
import mp.dw.db.doc.DocumentDAOImpl;
import mp.dw.db.param.ParamDAOImpl;
import mp.dw.db.user.UserDAOImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.datasource.url=jdbc:postgresql://localhost:5432/db_example?useUnicode=true&characterEncoding=UTF-8&serverTimezone=CET",
								"spring.datasource.username=springuser",
								"spring.datasource.password=spr1ngVser"},
				classes = DWApp.class,
				webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {MockInjectionConfiguration.class})
public class DWRequestMappingsTests {

	@LocalServerPort
	private int port;
	
	TestRestTemplate restTemplate;
	HttpHeaders headers;
	HttpEntity<String> entity;
	ResponseEntity<String> response;
	
	@Before
	public void setup() {
		headers = new HttpHeaders();
		restTemplate = new TestRestTemplate();
		entity = new HttpEntity<String>(null, headers);
	}
	
	@Test
	public void testRootMappingForNotAuthenticatedUser() {
		response = restTemplate.exchange(createURLWithPort("/"), HttpMethod.GET, entity, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void testLoginMappingForNotAuthenticatedUser() {
		response = restTemplate.exchange(createURLWithPort("/login"), HttpMethod.GET, entity, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void testGetDocumentsMappingForNotAuthenticatedUser() {
		response = restTemplate.exchange(createURLWithPort("/getDocuments"), HttpMethod.GET, entity, String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void testSaveDocumentForNotAuthenticatedUser() {
		response = restTemplate.exchange(createURLWithPort("/saveDocument"), HttpMethod.POST, entity, String.class);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
