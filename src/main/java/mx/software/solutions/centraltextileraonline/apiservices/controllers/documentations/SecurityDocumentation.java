package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ChangePasswordByTokenRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ChangePasswordRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.TokenActionResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.PasswordWrongException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.TokenInvalidException;

@Tag(name = "Security", description = "Endpoints to manage the security theme in Central Textilera Ecommerce.")
public interface SecurityDocumentation {

	boolean validateSession();

	@Operation(summary = "Change password", description = "Change Password for account Central Textilera.")
	TokenActionResponse changePassword(ChangePasswordRequest changePasswordRequest) throws DataBaseException, NotFoundException, PasswordWrongException;

	@Operation(summary = "Change password by token", description = "Change Password for account Central Textilera.")
	TokenActionResponse changePassword(String token, ChangePasswordByTokenRequest changePasswordByTokenRequest) throws DataBaseException, NotFoundException, TokenInvalidException;

}
