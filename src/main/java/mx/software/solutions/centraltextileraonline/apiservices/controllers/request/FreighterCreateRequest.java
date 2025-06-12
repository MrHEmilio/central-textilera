package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class FreighterCreateRequest {

	@NotNull(message = "freighter.name.not.null")
	@NotBlank(message = "freighter.name.not.blank")
	private String name;

	@NotNull(message = "freighter.image.not.null")
	@NotBlank(message = "freighter.image.not.blank")
	private String image;

	@NotNull(message = "freighter.cost.per.distance.not.null")
	@Positive(message = "freighter.cost.per.distance.positive")
	private BigDecimal costPerDistance;

	@NotNull(message = "freighter.cost.per.weight.not.null")
	@Positive(message = "freighter.cost.per.weight.positive")
	private BigDecimal costPerWeight;

}
