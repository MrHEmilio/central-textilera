package mx.software.solutions.centraltextileraonline.apiservices.service;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.TokenInfoResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CredentialEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.EmailTemplate;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.TokenInvalidException;

public interface TokenService {

	String createToken(String email, EmailTemplate emailTemplate);

	void saveEmailToken(String token, String emailContent) throws DataBaseException, NotFoundException;

	TokenInfoResponse validateToken(String token) throws DataBaseException, NotFoundException, TokenInvalidException;

	void invalidLastToken(final CredentialEntity credentialEntity) throws DataBaseException;

}
