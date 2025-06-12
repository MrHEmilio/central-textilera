package mx.software.solutions.centraltextileraonline.apiservices.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportClothSoldOutEntity {

	private ClothEntity clothEntity;

	private ColorEntity colorEntity;

}
