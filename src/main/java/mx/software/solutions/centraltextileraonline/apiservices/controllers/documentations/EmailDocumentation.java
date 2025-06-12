package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SendEmailContactUsRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SendEmailResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Email", description = "Endpoints to manage the send email in Central Textilera Ecommerce.")
public interface EmailDocumentation {

	@Operation(summary = "Send email contact us", description = "Send email contact us from the app.")
	SendEmailResponse sendEmailContactUs(SendEmailContactUsRequest sendEmailContactUsRequest);

	@Operation(summary = "Send email change password", description = "Send email change password from the app.")
	SendEmailResponse sendEmailChangePassword(String email) throws DataBaseException, NotFoundException;

	@Operation(summary = "Send email reactive client", description = "Send email reactive client from the app.")
	SendEmailResponse sendEmailReactiveClient(UUID client) throws DataBaseException, NotFoundException;

	@Operation(summary = "Send email verify email", description = "Send email verify client from the app.")
	SendEmailResponse sendEmailVerifyEmail() throws DataBaseException, NotFoundException;

	@Operation(summary = "Send email verify email by client", description = "Send email verify client from the app.")
	SendEmailResponse sendEmailVerifyEmail(UUID client) throws DataBaseException, NotFoundException;

	SendEmailResponse sendEmailTicket(@PathVariable final UUID order) throws DataBaseException, NotFoundException;

}
