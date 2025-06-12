package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AdminUpdateRequest {

	@NotNull(message = "client.name.not.null")
	@NotBlank(message = "client.name.not.blank")
	private String name;

	@NotNull(message = "client.first.lastname.not.null")
	@NotBlank(message = "client.first.lastname.not.blank")
	private String firstLastname;

	private String secondLastname;

}
