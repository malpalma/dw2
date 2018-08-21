package dw2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
import mp.dw.db.param.Param;
import mp.dw.db.param.ParamDAOImpl;
import mp.dw.db.user.UserDAOImpl;
import mp.dw.db.user.UserE;

@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@SpringBootTest(properties = {"spring.datasource.url=jdbc:postgresql://localhost:5432/db_example?useUnicode=true&characterEncoding=UTF-8&serverTimezone=CET",
								"spring.datasource.username=springuser",
								"spring.datasource.password=spr1ngVser"})
@ContextConfiguration(classes = {DWApp.class, DWService.class, 
									DocumentDAOImpl.class, DocItemDAOImpl.class, DocSumDAOImpl.class, DocStageDAOImpl.class, AttachmentDAOImpl.class,
									ContractorDAOImpl.class, ParamDAOImpl.class, UserDAOImpl.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class DWIntegrationTests {

	//user for testing
	private final String testUserName = "user999";
	private final String testUserPass = "user999";
	
	//params
	private final String[] testUnitType = {"ut", "testUnitType", "test unit type"}; 
	private final String[] testPaymentMethod = {"pm", "przelew 99 dni"};
	private final Float testPaymentMethodValue = 99f;
	
	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
	@Autowired
	private DWService dwService;
	
	@Test
	public void testUserConfiguration() {
		//add new user
		UserE testUser = new UserE(testUserName, testUserPass);
		dwService.saveUser(testUser);
		UserE result = dwService.getUserByName(testUserName);
		assertNotNull(result);
		assertEquals(testUserName, result.getName());
		assertTrue(bCryptPasswordEncoder.matches(testUserPass, result.getPass()));
		//the user should be active, no canAccept, no canEdit, no isAdmin
		assertTrue(result.getActive());
		assertFalse(result.getCanAccept());
		assertFalse(result.getCanEdit());
		assertFalse(result.getIsAdmin());
		
		//configure user for edit
		result.setCanEdit(true);
		dwService.saveUser(result);
		UserE resultWithEditPerm = dwService.getUserByName(testUserName);
		assertNotNull(resultWithEditPerm);
		assertTrue(resultWithEditPerm.getCanEdit());
		
		//configure user for accepting
		result.setCanAccept(true);
		dwService.saveUser(result);
		UserE resultWithAcceptPerm = dwService.getUserByName(testUserName);
		assertNotNull(resultWithAcceptPerm);
		assertTrue(resultWithAcceptPerm.getCanAccept());
		
		//configure admin user
		result.setIsAdmin(true);
		dwService.saveUser(result);
		UserE resultWithAdminPerm = dwService.getUserByName(testUserName);
		assertNotNull(resultWithAdminPerm);
		assertTrue(resultWithAdminPerm.getIsAdmin());
		
		//delete user
		dwService.deleteUserById(result.getId());
		UserE resultDeleted = dwService.getUserByName(testUserName);
		assertNull(resultDeleted);
	}
	
	@Test
	public void testParamConfiguration() {
		//add new unit type
		Param testUT = new Param(testUnitType[0], testUnitType[1], testUnitType[2]);
		dwService.saveParam(testUT);
		Param resultUT = dwService.getParamByTypeAndDescr(testUnitType[0], testUnitType[2]);
		assertNotNull(resultUT);
		assertEquals(testUnitType[1], resultUT.getCode());
		
		//add new payment method
		Param testPM = new Param(testPaymentMethod[0], testPaymentMethod[1], testPaymentMethodValue);
		dwService.saveParam(testPM);
		Param resultPM = dwService.getParamByTypeAndDescr(testPaymentMethod[0], testPaymentMethod[1]);
		assertNotNull(resultPM);
		assertEquals(testPaymentMethodValue, resultPM.getValue());
		
		//delete test params
		dwService.deleteParamById(resultUT.getId());
		Param resultDeletedUT = dwService.getParamByTypeAndDescr(testUnitType[0], testUnitType[2]);
		assertNull(resultDeletedUT);
		dwService.deleteParamById(resultPM.getId());
		Param resultDeletedPM = dwService.getParamByTypeAndDescr(testPaymentMethod[0], testPaymentMethod[1]);
		assertNull(resultDeletedPM);
	}
	
	@After
	public void deleteTestData() {
		UserE testUser = dwService.getUserByName(testUserName);
		if(testUser != null) {
			dwService.deleteUserById(testUser.getId());
		}
		
		Param testUT = dwService.getParamByTypeAndDescr(testUnitType[0], testUnitType[2]);
		if(testUT != null) {
			dwService.deleteParamById(testUT.getId());
		}
	}
}
