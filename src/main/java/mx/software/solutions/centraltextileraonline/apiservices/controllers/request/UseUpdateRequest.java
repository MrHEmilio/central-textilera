package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UseUpdateRequest {

	@NotNull(message = "use.id.not.null")
	private UUID id;

	@NotNull(message = "use.name.not.null")
	@NotBlank(message = "use.name.not.blank")
	private String name;

}
