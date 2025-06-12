package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FiberCreateRequest {

	@NotNull(message = "fiber.name.not.null")
	@NotBlank(message = "fiber.name.not.blank")
	private String name;

}
