package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import java.util.List;

import lombok.Data;

@Data
public class ShippmentResponse {

	private Object data;
	private List<IncludedResponse> included;

}
