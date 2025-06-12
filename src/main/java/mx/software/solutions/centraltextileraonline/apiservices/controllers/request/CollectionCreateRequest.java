package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CollectionCreateRequest {

	@NotNull(message = "collection.name.not.null")
	@NotBlank(message = "collection.name.not.blank")
	private String name;

	@NotNull(message = "collection.image.not.null")
	@NotBlank(message = "collection.image.not.blank")
	private String image;

}
