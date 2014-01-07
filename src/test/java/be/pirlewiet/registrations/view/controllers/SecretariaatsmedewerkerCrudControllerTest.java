package be.pirlewiet.registrations.view.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.MailSendException;

import be.pirlewiet.registrations.model.Secretariaatsmedewerker;
import be.pirlewiet.registrations.services.SecretariaatsmedewerkerService;
import be.pirlewiet.registrations.view.controllers.SecretariaatsmedewerkerCrudController;

@RunWith(MockitoJUnitRunner.class)
public class SecretariaatsmedewerkerCrudControllerTest {

	@Mock
	private SecretariaatsmedewerkerService secretariaatsmedewerkerService;
	@InjectMocks
	private SecretariaatsmedewerkerCrudController secretariaatsMedewerkerCrudController;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = MailSendException.class)
	public void test_resetMedewerker() throws MailSendException, Exception {
		Mockito.when(secretariaatsmedewerkerService.findById(Mockito.anyLong())).thenReturn(
				new Secretariaatsmedewerker());
		Mockito.when(secretariaatsmedewerkerService.reset(Mockito.any(Secretariaatsmedewerker.class))).thenThrow(
				new MailSendException("test"));

		secretariaatsMedewerkerCrudController.resetMedewerker(1);
	}

	// @Test(expected = MailSendException.class)
	// public void test_addSecretariaatsMedewerker() throws MailSendException, Exception {
	// Mockito.when(
	// secretariaatsmedewerkerService.createAndSendPasswordAndAssignRoleSecretariaat(Mockito
	// .any(Secretariaatsmedewerker.class))).thenThrow(new MailSendException("test"));
	//
	// secretariaatsMedewerkerCrudController.addSecretariaatsMedewerker(Mockito.any(Secretariaatsmedewerker.class));
	// }
}
