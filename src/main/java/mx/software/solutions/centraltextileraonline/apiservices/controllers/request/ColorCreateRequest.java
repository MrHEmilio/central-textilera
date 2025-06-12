package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class ColorCreateRequest {

	@NotNull(message = "color.name.not.null")
	@NotBlank(message = "color.name.not.blank")
	private String name;

	@NotNull(message = "color.code.not.null")
	@NotBlank(message = "color.code.not.blank")
	@Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "color.code.not.valid")
	private String code;

}
