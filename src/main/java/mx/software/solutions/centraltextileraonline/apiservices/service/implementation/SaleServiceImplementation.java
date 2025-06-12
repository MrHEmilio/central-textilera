package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SaleCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.SaleUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SaleHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SaleResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.SaleEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.SaleHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.AdminHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SaleHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.SaleHistoryRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.SaleRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.SaleService;

@Slf4j
@Service
public class SaleServiceImplementation implements SaleService {

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private SaleHistoryRepository saleHistoryRepository;

	@Autowired
	private SaleHelper saleHelper;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Override
	public SaleResponse createSale(final SaleCreateRequest saleCreateRequest, final UUID admin) throws DataBaseException, ExistException {
		SaleServiceImplementation.log.info("Starting created the sale {}.", saleCreateRequest.getName());
		this.validateSaleNotExist(saleCreateRequest.getName());
		try {
			final var saleEntity = new SaleEntity();
			saleEntity.setName(saleCreateRequest.getName());
			saleEntity.setAbbreviation(saleCreateRequest.getAbbreviation());
			saleEntity.setActive(true);
			final var newSaleEntity = this.saleRepository.save(saleEntity);
			SaleServiceImplementation.log.info("Finished create the sale {}.", saleCreateRequest.getName());

			SaleServiceImplementation.log.info("Starting created the create history {}.", saleCreateRequest.getName());
			this.createSaleHistory(newSaleEntity, admin, DataBaseActionType.CREATE);
			SaleServiceImplementation.log.info("Finished create the create history {}.", saleCreateRequest.getName());

			return this.saleHelper.convertSale(newSaleEntity);
		} catch (final Exception exception) {
			SaleServiceImplementation.log.error("The sale {} could not been create.", saleCreateRequest.getName(), exception);
			throw new DataBaseException(Controller.CATALOG_SALE, DataBaseActionType.CREATE, saleCreateRequest.getName());
		}
	}

	@Override
	public SaleResponse updateSale(final SaleUpdateRequest saleUpdateRequest, final UUID admin) throws DataBaseException, NotFoundException, ExistException {
		SaleServiceImplementation.log.info("Starting updated the sale with the id {}.", saleUpdateRequest.getId());
		final var saleEntity = this.saleHelper.getSaleEntity(saleUpdateRequest.getId());
		saleEntity.setName(saleUpdateRequest.getName());
		saleEntity.setAbbreviation(saleUpdateRequest.getAbbreviation());
		saleEntity.setActive(true);
		try {
			final var newSaleEntity = this.saleRepository.save(saleEntity);
			SaleServiceImplementation.log.info("Finished update the sale with the id {}.", saleUpdateRequest.getId());

			SaleServiceImplementation.log.info("Starting created the update history of id {}.", saleUpdateRequest.getId());
			this.createSaleHistory(newSaleEntity, admin, DataBaseActionType.UPDATE);
			SaleServiceImplementation.log.info("Finished create the update history of id {}.", saleUpdateRequest.getId());

			return this.saleHelper.convertSale(newSaleEntity);
		} catch (final Exception exception) {
			SaleServiceImplementation.log.error("The sale with the id {} could not been update.", saleUpdateRequest.getId(), exception);
			throw new DataBaseException(Controller.CATALOG_SALE, DataBaseActionType.UPDATE, saleUpdateRequest.getName());
		}
	}

	@Override
	public SaleResponse reactivateSale(final UUID sale, final UUID admin) throws DataBaseException, NotFoundException {
		SaleServiceImplementation.log.info("Starting reactivated the sale with the id {}.", sale);
		final var saleEntity = this.saleHelper.getSaleEntity(sale);
		saleEntity.setActive(true);
		try {
			final var newSaleEntity = this.saleRepository.save(saleEntity);
			SaleServiceImplementation.log.info("Finished reactivate the sale with the id {}.", sale);

			SaleServiceImplementation.log.info("Starting created the reactivate history of id {}.", sale);
			this.createSaleHistory(newSaleEntity, admin, DataBaseActionType.REACTIVATE);
			SaleServiceImplementation.log.info("Finished create the reactivate history of id {}.", sale);

			return this.saleHelper.convertSale(newSaleEntity);
		} catch (final Exception exception) {
			SaleServiceImplementation.log.error("The sale with the id {} could not been reactivate.", sale, exception);
			throw new DataBaseException(Controller.CATALOG_SALE, DataBaseActionType.REACTIVATE, sale.toString());
		}
	}

	@Override
	public SaleResponse deleteSale(final UUID sale, final UUID admin) throws DataBaseException, NotFoundException {
		SaleServiceImplementation.log.info("Starting deleted the sale with the id {}.", sale);
		final var saleEntity = this.saleHelper.getSaleEntity(sale);
		saleEntity.setActive(false);
		try {
			final var newSaleEntity = this.saleRepository.save(saleEntity);
			SaleServiceImplementation.log.info("Finished delete the sale with the id {}.", sale);

			SaleServiceImplementation.log.info("Starting created the delete history of id {}.", sale);
			this.createSaleHistory(newSaleEntity, admin, DataBaseActionType.DELETE);
			SaleServiceImplementation.log.info("Finished create the delete history of id {}.", sale);

			return this.saleHelper.convertSale(newSaleEntity);
		} catch (final Exception exception) {
			SaleServiceImplementation.log.error("The sale with the id {} could not been delete.", sale, exception);
			throw new DataBaseException(Controller.CATALOG_SALE, DataBaseActionType.DELETE, sale.toString());
		}
	}

	@Override
	public GetResponse<SaleResponse> getAllSale(final FilterRequest filterRequest, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		SaleServiceImplementation.log.info("Starting searched of all sales.");
		Page<SaleEntity> pageSaleEntity = null;
		try {
			final var search = filterRequest.getSearch();
			var active = filterRequest.getActive();
			String direction = null;
			if (!this.sessionHelper.isAdmin())
				active = true;
			if (paginationRequest.getTypeSort() != null)
				direction = paginationRequest.getTypeSort().name();
			paginationRequest.setColumnSort(null);
			paginationRequest.setTypeSort(null);
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageSaleEntity = this.saleRepository.findAll(search, active, direction, pageable);
		} catch (final Exception exception) {
			SaleServiceImplementation.log.error("The sales could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_SALE, DataBaseActionType.READ);
		}
		final var listSaleResponse = pageSaleEntity.get().map(this.saleHelper::convertSale).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageSaleEntity);
		if (listSaleResponse.isEmpty()) {
			SaleServiceImplementation.log.error("The sales not found.");
			throw new NotFoundException(Controller.CATALOG_SALE, "all");
		}
		SaleServiceImplementation.log.info("Finished search the sales.");
		return new GetResponse<>(listSaleResponse, paginationResponse);
	}

	@Override
	public GetResponse<SaleHistoryResponse> getSaleHistory(final UUID sale, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		SaleServiceImplementation.log.info("Starting searched history of sale with the id {}.", sale);
		final var saleEntity = this.saleHelper.getSaleEntity(sale);
		Page<SaleHistoryEntity> pageSaleHistoryEntity = null;
		try {
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageSaleHistoryEntity = this.saleHistoryRepository.findAllBySaleEntity(saleEntity, pageable);
		} catch (final Exception exception) {
			SaleServiceImplementation.log.error("The history sale could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_SALE, DataBaseActionType.READ);
		}
		final var listSaleHistoryResponse = pageSaleHistoryEntity.get().map(this.saleHelper::convertSaleHistory).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageSaleHistoryEntity);
		SaleServiceImplementation.log.info("Finished search history of sale with the id {}.", sale);
		return new GetResponse<>(listSaleHistoryResponse, paginationResponse);
	}

	private void validateSaleNotExist(final String name) throws ExistException {
		SaleServiceImplementation.log.info("Starting validate the sale if exist {}.", name);
		final var optionalSaleEntity = this.saleRepository.findByNameIgnoreCase(name);
		if (optionalSaleEntity.isPresent()) {
			SaleServiceImplementation.log.error("The sale {} exist.", name);
			throw new ExistException(Controller.CATALOG_SALE, "name", name);
		}
	}

	private void createSaleHistory(final SaleEntity saleEntity, final UUID admin, final DataBaseActionType dataBaseActionType) {
		try {
			final var saleHistoryEntity = new SaleHistoryEntity();
			final var adminEntity = this.adminHelper.getAdminEntity(admin);
			final var objectMapper = new ObjectMapper();
			final var object = objectMapper.writeValueAsString(saleEntity);
			saleHistoryEntity.setSaleEntity(saleEntity);
			saleHistoryEntity.setAdminEntity(adminEntity);
			saleHistoryEntity.setActionType(dataBaseActionType);
			saleHistoryEntity.setDate(new Date());
			saleHistoryEntity.setObject(object);
			this.saleHistoryRepository.save(saleHistoryEntity);
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

}
