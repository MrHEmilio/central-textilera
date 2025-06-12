package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.DateFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CountReportResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ReportClothMostSoldResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ReportClothSoldOutResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.TotalSaleReportResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ReportClothMostSoldEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ReportClothSoldOutEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ClothResponseStructure;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ClothHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ColorHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.DateHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClientRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClothReportRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.OrderReportRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.ReportService;

@Slf4j
@Service
public class ReportServiceImplementation implements ReportService {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ClothReportRepository clothReportRepository;

	@Autowired
	private OrderReportRepository orderReportRepository;

	@Autowired
	private ClothHelper clothHelper;

	@Autowired
	private ColorHelper colorHelper;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private DateHelper dateHelper;

	@Override
	public CountReportResponse getClient() throws DataBaseException {
		try {
			ReportServiceImplementation.log.info("Starting counted total number of client.");
			final var total = this.clientRepository.count();
			ReportServiceImplementation.log.info("Finished count total number of client.");
			return new CountReportResponse(total);
		} catch (final Exception exception) {
			ReportServiceImplementation.log.error("The client count could not been read.", exception);
			throw new DataBaseException(Controller.REPORT_CLIENT, DataBaseActionType.READ);
		}
	}

	@Override
	public CountReportResponse getCloth() throws DataBaseException {
		try {
			ReportServiceImplementation.log.info("Starting counted total number of cloths.");
			final var total = this.clothReportRepository.countByActive();
			ReportServiceImplementation.log.info("Finished count total number of cloths.");
			return new CountReportResponse(total);
		} catch (final Exception exception) {
			ReportServiceImplementation.log.error("The cloth count could not been read.", exception);
			throw new DataBaseException(Controller.REPORT_CLOTH, DataBaseActionType.READ);
		}
	}

	@Override
	public GetResponse<ReportClothMostSoldResponse> getClothsMostSold(final DateFilterRequest dateFilterRequest, final List<ClothResponseStructure> listClothResponseStructure, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		ReportServiceImplementation.log.info("Starting searched cloths most sold.");
		Page<ReportClothMostSoldEntity> pageReportClothMostSoldEntity = null;
		this.dateHelper.setDateFilter(dateFilterRequest);
		try {
			final var dateStart = dateFilterRequest.getDateStart();
			final var dateEnd = dateFilterRequest.getDateEnd();
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageReportClothMostSoldEntity = this.clothReportRepository.findByMostSold(dateStart, dateEnd, pageable);
		} catch (final Exception exception) {
			ReportServiceImplementation.log.error("The cloths most sold could not been read.", exception);
			throw new DataBaseException(Controller.REPORT_CLOTH, DataBaseActionType.READ);
		}
		final var listReportClothMostSoldResponse = pageReportClothMostSoldEntity.get()
				.map(reportClothMostSoldEntity ->
						new ReportClothMostSoldResponse(this.clothHelper.convertCloth(reportClothMostSoldEntity.getClothEntity(), listClothResponseStructure), reportClothMostSoldEntity.getAmount())).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageReportClothMostSoldEntity);
		if (listReportClothMostSoldResponse.isEmpty()) {
			ReportServiceImplementation.log.error("The cloths most sold not found.");
			throw new NotFoundException(Controller.REPORT_CLOTH, "all");
		}
		ReportServiceImplementation.log.info("Finished search cloths most sold.");
		return new GetResponse<>(listReportClothMostSoldResponse, paginationResponse);
	}

	@Override
	public GetResponse<ReportClothSoldOutResponse> getClothsSoldOut(final List<ClothResponseStructure> listClothResponseStructure, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		
		ReportServiceImplementation.log.info("Starting searched cloths most sold.");
		Page<ReportClothSoldOutEntity> pageReportClothSoldOutEntity = null;
		try {
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageReportClothSoldOutEntity = this.clothReportRepository.findBySoldOut(pageable);
		} catch (final Exception exception) {
			ReportServiceImplementation.log.error("The cloths sold out could not been read.", exception);
			throw new DataBaseException(Controller.REPORT_CLOTH, DataBaseActionType.READ);
		}
		final var listReportClothSoldOutResponse = pageReportClothSoldOutEntity.get().map(reportClothSoldOutEntity -> new ReportClothSoldOutResponse(this.clothHelper.convertCloth(reportClothSoldOutEntity.getClothEntity(), listClothResponseStructure), this.colorHelper.convertColor(reportClothSoldOutEntity.getColorEntity()))).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageReportClothSoldOutEntity);
		if (listReportClothSoldOutResponse.isEmpty()) {
			ReportServiceImplementation.log.error("The cloths sold out not found.");
			throw new NotFoundException(Controller.REPORT_CLOTH, "all");
		}

		return new GetResponse<>(listReportClothSoldOutResponse, paginationResponse);
	}

	@Override
	public TotalSaleReportResponse getSale(final DateFilterRequest dateFilterRequest) throws DataBaseException {
		this.dateHelper.setDateFilter(dateFilterRequest);
		try {
			ReportServiceImplementation.log.info("Starting sum total orders.");
			final var dateStart = dateFilterRequest.getDateStart();
			final var dateEnd = dateFilterRequest.getDateEnd();

			final var total = this.orderReportRepository.sumTotal(dateStart, dateEnd);

			ReportServiceImplementation.log.info("Finished sum total orders.");
			return new TotalSaleReportResponse(total);
		} catch (final Exception exception) {
			ReportServiceImplementation.log.error("The client count could not been read.", exception);
			throw new DataBaseException(Controller.REPORT_ORDER, DataBaseActionType.READ);
		}
	}

}
