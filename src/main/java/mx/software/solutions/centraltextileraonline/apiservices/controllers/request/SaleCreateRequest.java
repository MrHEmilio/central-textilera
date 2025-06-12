package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SaleCreateRequest {

	@NotNull(message = "sale.name.not.null")
	@NotBlank(message = "sale.name.not.blank")
	private String name;

	@NotNull(message = "sale.abbreviation.not.null")
	@NotBlank(message = "sale.abbreviation.not.blank")
	private String abbreviation;

}
