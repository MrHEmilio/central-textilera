package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FiberUpdateRequest {

	@NotNull(message = "fiber.id.not.null")
	private UUID id;

	@NotNull(message = "fiber.name.not.null")
	@NotBlank(message = "fiber.name.not.blank")
	private String name;

}
