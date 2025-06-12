package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.List;

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

public interface ReportService {

	CountReportResponse getClient() throws DataBaseException;

	CountReportResponse getCloth() throws DataBaseException;

	GetResponse<ReportClothMostSoldResponse> getClothsMostSold(DateFilterRequest dateFilterRequest, List<ClothResponseStructure> listClothResponseStructure, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	GetResponse<ReportClothSoldOutResponse> getClothsSoldOut(List<ClothResponseStructure> listClothResponseStructure, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	TotalSaleReportResponse getSale(DateFilterRequest dateFilterRequest) throws DataBaseException;

}
