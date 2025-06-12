package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import lombok.Data;

@Data
public class ElementResponse {

	private ElementDetailsResponse distance;

	private ElementDetailsResponse duration;

	private String status;

}
