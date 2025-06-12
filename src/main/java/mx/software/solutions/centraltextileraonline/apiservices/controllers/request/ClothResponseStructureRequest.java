package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.List;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ClothResponseStructure;

@Data
public class ClothResponseStructureRequest {

	List<ClothResponseStructure> responseStructure;

}
