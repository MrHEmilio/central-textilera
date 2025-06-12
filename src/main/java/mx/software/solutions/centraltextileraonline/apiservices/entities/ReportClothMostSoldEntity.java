package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportClothMostSoldEntity {

	private ClothEntity clothEntity;

	private BigDecimal amount;

}
