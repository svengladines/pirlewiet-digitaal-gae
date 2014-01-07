package be.pirlewiet.registrations.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import be.pirlewiet.registrations.utils.MailUtils;
import be.pirlewiet.registrations.utils.PasswordUtils;

@RunWith(MockitoJUnitRunner.class)
public class PasswordUtilsTest {

	private static final String TEST_EMAIL = "test@test.com";

	@Mock
	private MailUtils mailUtils;

	@InjectMocks
	private PasswordUtils passwordUtils;

	private final static PasswordEncoder PASSWORD_ENCODER = new ShaPasswordEncoder();

	@Test
	public void generateAndEncodeAndSendPassword() {
		String resultsha1 = passwordUtils.generateAndEncodeAndSendPassword(TEST_EMAIL);

		ArgumentCaptor<String> argumentCaptorEmail = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> argumentCaptorPassword = ArgumentCaptor.forClass(String.class);
		verify(mailUtils).sendmail(argumentCaptorEmail.capture(), argumentCaptorPassword.capture());

		assertEquals(TEST_EMAIL, argumentCaptorEmail.getValue());
		assertEquals(8, argumentCaptorPassword.getValue().length());

		// ParseInt Mag geen exception gooien want de laatste 4 cijfers moeten
		// numeriek zijn
		String numericPart = argumentCaptorPassword.getValue().substring(4, 8);
		int numericPartInteger = Integer.parseInt(numericPart);
		assertTrue(numericPartInteger > 1000);

		// valideer of het verzonden paswoord geldig is volgend de retourneerde
		// sha
		assertTrue(PASSWORD_ENCODER.isPasswordValid(resultsha1, argumentCaptorPassword.getValue(), ""));

	}
}
