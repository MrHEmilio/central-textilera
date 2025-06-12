package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ChangePasswordRequest {

	@NotNull(message = "security.change.password.old.password.not.null")
	@NotBlank(message = "security.change.password.old.password.not.blank")
	private String oldPassword;

	@NotNull(message = "security.change.password.new.password.not.null")
	@NotBlank(message = "security.change.password.new.password.not.blank")
	private String newPassword;

}
