package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailContactUsRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailDataRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailOrderCreatedRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailOrderUpdatedRequest;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

public interface EmailService {

	void sendEmail(String email, String subject, String content);

	void sendEmailWelcomeClient(SendEmailDataRequest sendEmailDataRequest) throws DataBaseException, NotFoundException;

	void sendEmailWelcomeAdmin(SendEmailDataRequest sendEmailDataRequest) throws DataBaseException, NotFoundException;

	void sendEmailChangePasswordRequest(String email) throws DataBaseException, NotFoundException;

	void sendEmailChangePasswordInfo(SendEmailDataRequest sendEmailDataRequest) throws DataBaseException, NotFoundException;

	void sendEmailVerifyEmail(UUID client) throws DataBaseException, NotFoundException;

	void sendEmailDeleteClient(SendEmailDataRequest sendEmailDataRequest) throws DataBaseException, NotFoundException;

	void sendEmailDeleteAdmin(SendEmailDataRequest sendEmailDataRequest);

	void sendEmailReactiveAccount(UUID client) throws DataBaseException, NotFoundException;

	void sendEmailContactUsClient(SendEmailContactUsRequest sendEmailContactUsRequest);

	void sendEmailContactUsCtx(SendEmailContactUsRequest sendEmailContactUsRequest);

	void sendEmailOrderCreated(SendEmailOrderCreatedRequest sendEmailOrderCreatedRequest);

	void sendEmailOrderUpdated(SendEmailOrderUpdatedRequest sendEmailOrderUpdatedRequest);

}
