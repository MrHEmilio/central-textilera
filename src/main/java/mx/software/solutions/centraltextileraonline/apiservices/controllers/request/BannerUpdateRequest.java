package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BannerUpdateRequest {

	@NotNull(message = "banner.id.not.null")
	private UUID id;

	@NotNull(message = "banner.description.not.null")
	@NotBlank(message = "banner.description.not.blank")
	private String description;

	@NotNull(message = "banner.wait.time.not.null")
	private int waitTime;

}
