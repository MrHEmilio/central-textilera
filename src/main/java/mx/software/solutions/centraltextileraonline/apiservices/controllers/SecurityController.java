package mx.software.solutions.centraltextileraonline.apiservices.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.SecurityDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ChangePasswordByTokenRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ChangePasswordRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.TokenActionResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.MessageLangType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.PasswordWrongException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.TokenInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.MessageLangHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.service.SecurityService;
import mx.software.solutions.centraltextileraonline.apiservices.service.TokenService;

@RestController
@RequestMapping("/security")
public class SecurityController implements SecurityDocumentation {

	@Autowired
	private MessageLangHelper messageLangHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private SecurityService securityService;

	@Override
	@PostMapping("/validate/session")
	public boolean validateSession() {
		return true;
	}

	@Override
	@PutMapping("/password")
	public TokenActionResponse changePassword(@RequestBody final ChangePasswordRequest changePasswordRequest) throws DataBaseException, NotFoundException, PasswordWrongException {
		final var sessionDto = this.sessionHelper.getSessionDto();
		this.securityService.changePassword(sessionDto.getIdCredential(), changePasswordRequest);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_SECURITY, MessageLangType.RESPONSE, "change.password");
		return new TokenActionResponse(messageResponse);
	}

	@Override
	@PutMapping("/password/{token}")
	public TokenActionResponse changePassword(@PathVariable final String token, @RequestBody final ChangePasswordByTokenRequest changePasswordByTokenRequest) throws DataBaseException, NotFoundException, TokenInvalidException {
		final var tokenInfoResponse = this.tokenService.validateToken(token);
		this.securityService.changePassword(tokenInfoResponse.getEmail(), changePasswordByTokenRequest);
		final var messageResponse = this.messageLangHelper.getMessageLang(Controller.PROFILE_SECURITY, MessageLangType.RESPONSE, "change.password");
		return new TokenActionResponse(messageResponse);
	}

}
