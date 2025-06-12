package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CollectionUpdateRequest {

	@NotNull(message = "collection.id.not.null")
	private UUID id;

	@NotNull(message = "collecion.name.not.null")
	@NotBlank(message = "collection.name.not.blank")
	private String name;

}
