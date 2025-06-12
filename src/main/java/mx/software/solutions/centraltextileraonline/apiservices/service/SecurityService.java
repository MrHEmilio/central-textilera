package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ChangePasswordByTokenRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ChangePasswordRequest;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.PasswordWrongException;

public interface SecurityService {

	void changePassword(UUID credential, ChangePasswordRequest changePasswordRequest) throws DataBaseException, NotFoundException, PasswordWrongException;

	void changePassword(String email, ChangePasswordByTokenRequest changePasswordByTokenRequest) throws DataBaseException, NotFoundException;

}
