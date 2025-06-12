package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.EmailExistWithoutPasswordException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.CredentialRepository;

@Slf4j
@Component
public class EmailHelper {

	@Autowired
	private CredentialRepository credentialRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${central-textilera.generic-password}")
	private String genericPassword;

	public void validateEmailNotExist(final String email) throws ExistException, EmailExistWithoutPasswordException {
		EmailHelper.log.info("Starting validate the email if exist {}.", email);
		final var optionalCredentialEntity = this.credentialRepository.findByEmailIgnoreCase(email);
		if (optionalCredentialEntity.isPresent()) {
			EmailHelper.log.error("The email {} exist.", email);

			EmailHelper.log.error("Starting validated the email {} if password is generic.", email);
			final var credentialEntity = optionalCredentialEntity.get();
			if (this.passwordEncoder.matches(this.genericPassword, credentialEntity.getPassword())) {
				EmailHelper.log.error("The email exist but created without password. {}", email);
				throw new EmailExistWithoutPasswordException();
			}

			throw new ExistException(Controller.PROFILE_CLIENT, "email", email);
		}
	}

}
