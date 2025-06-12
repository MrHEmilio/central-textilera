package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UseCreateRequest {

	@NotNull(message = "use.name.not.null")
	@NotBlank(message = "use.name.not.blank")
	private String name;

}
