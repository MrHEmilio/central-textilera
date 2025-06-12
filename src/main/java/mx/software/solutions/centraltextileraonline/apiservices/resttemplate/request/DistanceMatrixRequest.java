package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import java.util.List;

import lombok.Data;

@Data
public class DistanceMatrixRequest {

	private List<String> origins;
	private List<String> destinations;
	private List<String> key;

}
