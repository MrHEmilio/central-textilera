package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportClothMostSoldResponse {

	private ClothResponse cloth;

	private BigDecimal amount;

}
