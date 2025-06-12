package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ChangePasswordByTokenRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ChangePasswordRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailDataRequest;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.PasswordWrongException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SecurityHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.CredentialRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.EmailService;
import mx.software.solutions.centraltextileraonline.apiservices.service.SecurityService;
import mx.software.solutions.centraltextileraonline.apiservices.service.TokenService;

@Slf4j
@Service
public class SecurityServiceImplementation implements SecurityService {

	@Autowired
	private CredentialRepository credentialRepository;

	@Autowired
	private SecurityHelper securityHelper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	@Autowired
	private TokenService tokenService;

	@Override
	public void changePassword(final UUID credential, final ChangePasswordRequest changePasswordRequest) throws DataBaseException, NotFoundException, PasswordWrongException {
		final var credentialEntity = this.securityHelper.getCredentialEntity(credential);

		SecurityServiceImplementation.log.info("Starting verifying the password of email {}", credentialEntity.getEmail());
		if (!this.passwordEncoder.matches(changePasswordRequest.getOldPassword(), credentialEntity.getPassword())) {
			SecurityServiceImplementation.log.info("The password is invalid");
			throw new PasswordWrongException(credentialEntity.getEmail());
		}
		SecurityServiceImplementation.log.info("Finished verify the password of email {}", credentialEntity.getEmail());

		try {
			credentialEntity.setPassword(this.passwordEncoder.encode(changePasswordRequest.getNewPassword()));
			this.credentialRepository.save(credentialEntity);
		} catch (final Exception exception) {
			SecurityServiceImplementation.log.error("The email {} could not been change password.", credentialEntity.getEmail(), exception);
			throw new DataBaseException(Controller.PROFILE_SECURITY, DataBaseActionType.UPDATE, credentialEntity.getEmail());
		}

		SecurityServiceImplementation.log.info("Starting send email to client {}.", credentialEntity.getEmail());
		final var sendEmailDataRequest = new SendEmailDataRequest();
		sendEmailDataRequest.setEmail(credentialEntity.getEmail());
		sendEmailDataRequest.setName(credentialEntity.getEmail());
		this.emailService.sendEmailChangePasswordInfo(sendEmailDataRequest);
		SecurityServiceImplementation.log.info("Finished send email to client {}.", credentialEntity.getEmail());
	}

	@Override
	public void changePassword(final String email, final ChangePasswordByTokenRequest changePasswordByTokenRequest) throws DataBaseException, NotFoundException {
		final var credentialEntity = this.securityHelper.getCredentialEntityActive(email);
		try {
			credentialEntity.setPassword(this.passwordEncoder.encode(changePasswordByTokenRequest.getNewPassword()));
			this.credentialRepository.save(credentialEntity);
		} catch (final Exception exception) {
			SecurityServiceImplementation.log.error("The email {} could not been change password.", credentialEntity.getEmail(), exception);
			throw new DataBaseException(Controller.PROFILE_SECURITY, DataBaseActionType.UPDATE, credentialEntity.getEmail());
		}

		SecurityServiceImplementation.log.info("Starting invalid token for client {}.", credentialEntity.getEmail());
		this.tokenService.invalidLastToken(credentialEntity);
		SecurityServiceImplementation.log.info("Finished invalid token for client {}.", credentialEntity.getEmail());

		SecurityServiceImplementation.log.info("Starting send email to client {}.", credentialEntity.getEmail());
		final var sendEmailDataRequest = new SendEmailDataRequest();
		sendEmailDataRequest.setEmail(credentialEntity.getEmail());
		sendEmailDataRequest.setName(credentialEntity.getEmail());
		this.emailService.sendEmailChangePasswordInfo(sendEmailDataRequest);
		SecurityServiceImplementation.log.info("Finished send email to client {}.", credentialEntity.getEmail());
	}

}
