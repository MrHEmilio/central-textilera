package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SaleUpdateRequest {

	@NotNull(message = "sale.id.not.null")
	private UUID id;

	@NotNull(message = "sale.name.not.null")
	@NotBlank(message = "sale.name.not.blank")
	private String name;

	@NotNull(message = "sale.abbreviation.not.null")
	@NotBlank(message = "sale.abbreviation.not.blank")
	private String abbreviation;

}
