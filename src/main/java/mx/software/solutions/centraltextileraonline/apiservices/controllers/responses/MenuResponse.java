package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuResponse {

	private String name;
	private String icon;
	private String path;

}
