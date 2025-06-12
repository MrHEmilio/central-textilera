package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import lombok.Data;

@Data
public class BoxUpdateRequest {

	@NotNull(message = "box.id.not.null")
	private UUID id;

	@NotNull(message = "box.name.not.null")
	@NotBlank(message = "box.name.not.blank")
	private String name;

	@NotNull(message = "box.width.not.null")
	@Positive(message = "box.width.positive")
	private BigDecimal width;

	@NotNull(message = "box.height.not.null")
	@Positive(message = "box.height.positive")
	private BigDecimal height;

	@NotNull(message = "box.depth.not.null")
	@Positive(message = "box.depth.positive")
	private BigDecimal depth;

	@NotNull(message = "box.color.code.not.null")
	@NotBlank(message = "box.color.code.not.blank")
	@Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "box.color.code.not.valid")
	private String colorCode;

}
