package mx.software.solutions.centraltextileraonline.apiservices.controllers;

import java.util.Arrays;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.ReportDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.DateFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CountReportResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ReportClothMostSoldResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ReportClothSoldOutResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.TotalSaleReportResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ClothResponseStructure;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.service.ReportService;

@RestController
@RequestMapping("/report")
public class ReportController implements ReportDocumentation {

	@Autowired
	private ReportService reportService;

	@Override
	@GetMapping("/client")
	public CountReportResponse getClient() throws DataBaseException {
		return this.reportService.getClient();
	}

	@Override
	@GetMapping("/cloth")
	public CountReportResponse getCloth() throws DataBaseException {
		return this.reportService.getCloth();
	}

	@Override
	@GetMapping("/cloth/most/sold")
	public GetResponse<ReportClothMostSoldResponse> getClothsMostSold(@Valid final DateFilterRequest dateFilterRequest, @Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		final var listClothResponseType = Arrays.asList(ClothResponseStructure.values());
		return this.reportService.getClothsMostSold(dateFilterRequest, listClothResponseType, paginationRequest);
	}

	@Override
	@GetMapping("/cloth/sold/out")
	public GetResponse<ReportClothSoldOutResponse> getClothsSoldOut(@Valid final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		final var listClothResponseType = Arrays.asList(ClothResponseStructure.values());
		return this.reportService.getClothsSoldOut(listClothResponseType, paginationRequest);
	}

	@Override
	@GetMapping("/sale")
	public TotalSaleReportResponse getSale(@Valid final DateFilterRequest dateFilterRequest) throws DataBaseException {
		return this.reportService.getSale(dateFilterRequest);
	}

}
