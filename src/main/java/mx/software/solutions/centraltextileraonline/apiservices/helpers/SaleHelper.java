package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SaleHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.SaleResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.SaleEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.SaleHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.SaleRepository;

@Slf4j
@Component
public class SaleHelper {
	@Autowired
	private SaleRepository saleRepository;
	@Autowired
	private AdminHelper adminHelper;

	public SaleEntity getSaleEntity(final UUID sale) throws DataBaseException, NotFoundException {
		Optional<SaleEntity> optionalSaleEntity;
		try {
			SaleHelper.log.info("Starting searched the sale with the id {}.", sale);
			optionalSaleEntity = this.saleRepository.findById(sale);
		} catch (final Exception exception) {
			SaleHelper.log.error("The sale with the id {} could not read.", sale, exception);
			throw new DataBaseException(Controller.CATALOG_SALE, DataBaseActionType.READ, sale.toString());
		}

		if (optionalSaleEntity.isEmpty()) {
			SaleHelper.log.error("The sale not found with the id {}.", sale);
			throw new NotFoundException(Controller.EMAIL_NEWSLETTER, "id", sale.toString());
		}
		SaleHelper.log.info("Finished search the sale with the id {}.", sale);
		return optionalSaleEntity.get();
	}

	public List<SaleEntity> getSaleEntities(final List<UUID> sales) {
		return sales.stream().map(sale -> {
			try {
				return this.getSaleEntity(sale);
			} catch (DataBaseException | NotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}).collect(Collectors.toList());
	}

	public SaleResponse convertSale(final SaleEntity saleEntity) {
		final var id = saleEntity.getId();
		final var name = saleEntity.getName();
		final var abbreviation = saleEntity.getAbbreviation();
		final var isActive = saleEntity.isActive();
		return new SaleResponse(id, name, abbreviation, isActive);
	}

	public SaleHistoryResponse convertSaleHistory(final SaleHistoryEntity saleHistoryEntity) {
		final var id = saleHistoryEntity.getId();
		final var saleResponse = this.convertSale(saleHistoryEntity.getSaleEntity());
		final var adminResponse = this.adminHelper.convertAdmin(saleHistoryEntity.getAdminEntity());
		final var actionType = saleHistoryEntity.getActionType();
		final var date = saleHistoryEntity.getDate();
		final var object = saleHistoryEntity.getObject();
		return new SaleHistoryResponse(id, saleResponse, adminResponse, actionType, date, object);
	}

}
