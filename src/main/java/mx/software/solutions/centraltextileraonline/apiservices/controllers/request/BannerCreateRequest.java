package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class BannerCreateRequest {

	@NotNull(message = "banner.image.not.null")
	@NotNull(message = "banner.image.not.blank")
	private String image;

	@NotNull(message = "banner.description.not.null")
	@NotBlank(message = "banner.description.not.blank")
	private String description;

	@NotNull(message = "banner.wait.time.not.null")
	private int waitTime;

}
