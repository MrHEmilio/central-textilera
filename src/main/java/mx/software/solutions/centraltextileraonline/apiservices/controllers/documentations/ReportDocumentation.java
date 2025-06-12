package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.DateFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CountReportResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ReportClothMostSoldResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ReportClothSoldOutResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.TotalSaleReportResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;

@Tag(name = "Report", description = "Endpoints to manage the reports in Central Textilera Ecommerce.")
public interface ReportDocumentation {

	@Operation(summary = "Client registered", description = "Get report the total number of clients registered.")
	CountReportResponse getClient() throws DataBaseException;

	@Operation(summary = "Cloths registered", description = "Get report the total number of cloths registered.")
	CountReportResponse getCloth() throws DataBaseException;

	@Operation(summary = "Cloths most sold", description = "Get report the cloth most sold.")
	GetResponse<ReportClothMostSoldResponse> getClothsMostSold(DateFilterRequest dateFilterRequest, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Cloths sould out", description = "Get report of cloths sold out.")
	GetResponse<ReportClothSoldOutResponse> getClothsSoldOut(PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Sale registered", description = "Get report the total number of sales.")
	TotalSaleReportResponse getSale(DateFilterRequest dateFilterRequest) throws DataBaseException;

}
