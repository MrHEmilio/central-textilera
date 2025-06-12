package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportClothSoldOutResponse {

	private ClothResponse cloth;

	private ColorResponse color;

}
