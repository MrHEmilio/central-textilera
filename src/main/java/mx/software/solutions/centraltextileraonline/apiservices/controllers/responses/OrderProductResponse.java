package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class OrderProductResponse {

	private UUID id;
	private String image;
	private String name;
	private String color;
	private String sale;
	private BigDecimal amount;
	private BigDecimal sellPrice;
	private BigDecimal totalSellPrice;
	private BigDecimal discount;

}
