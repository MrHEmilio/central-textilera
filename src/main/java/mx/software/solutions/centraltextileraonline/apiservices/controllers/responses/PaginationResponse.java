package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationResponse {

	private int page;

	private int size;

	private int totalPage;

	private int totalRecords;

}