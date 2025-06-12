package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import lombok.Data;

@Data
public class IncludedResponse {

	private String id;
	private String type;
	private IncludedAttributesResponse attributes;

}
