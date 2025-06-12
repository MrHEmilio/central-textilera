package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ChangePasswordByTokenRequest {

	@NotNull(message = "security.change.password.new.password.not.null")
	@NotBlank(message = "security.change.password.new.password.not.blank")
	private String newPassword;

}
