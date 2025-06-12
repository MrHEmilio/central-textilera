package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FreighterHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FreighterResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.FreighterEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.FreighterHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.FreighterRepository;

@Slf4j
@Component
public class FreighterHelper {

	@Autowired
	private FreighterRepository freighterRepository;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private ImagesHelper imagesHelper;

	@Value("${url.images.freighters}")
	private String urlImagesFreighters;

	public FreighterEntity getFreighterEntity(final UUID freighter) throws DataBaseException, NotFoundException {
		Optional<FreighterEntity> optionalFreighterEntity = Optional.empty();
		try {
			FreighterHelper.log.info("Starting searched the freighter with the id {}.", freighter);
			optionalFreighterEntity = this.freighterRepository.findById(freighter);
		} catch (final Exception exception) {
			FreighterHelper.log.error("The freighter with the id {} could not read.", freighter, exception);
			throw new DataBaseException(Controller.CATALOG_FREIGHTER, DataBaseActionType.READ, freighter.toString());
		}

		if (optionalFreighterEntity.isEmpty()) {
			FreighterHelper.log.error("The freighter not found with the id {}.", freighter);
			throw new NotFoundException(Controller.CATALOG_FREIGHTER, "id", freighter.toString());
		}
		FreighterHelper.log.info("Finished search the freighter with the id {}.", freighter);
		return optionalFreighterEntity.get();
	}

	public FreighterResponse convertFreighter(final FreighterEntity freighterEntity) {
		final var id = freighterEntity.getId();
		final var name = freighterEntity.getName();
		final var image = this.imagesHelper.getUrlImage(this.urlImagesFreighters, freighterEntity.getId());
		final var costPerDistance = freighterEntity.getCostPerDistance();
		final var costPerWeight = freighterEntity.getCostPerWeight();
		final var isActive = freighterEntity.isActive();
		return new FreighterResponse(id, name, image, costPerDistance, costPerWeight, isActive);
	}

	public FreighterHistoryResponse convertFreighterHistory(final FreighterHistoryEntity freighterHistoryEntity) {
		final var id = freighterHistoryEntity.getId();
		final var freighterResponse = this.convertFreighter(freighterHistoryEntity.getFreighterEntity());
		final var adminResponse = this.adminHelper.convertAdmin(freighterHistoryEntity.getAdminEntity());
		final var actionType = freighterHistoryEntity.getActionType();
		final var date = freighterHistoryEntity.getDate();
		final var object = freighterHistoryEntity.getObject();
		return new FreighterHistoryResponse(id, freighterResponse, adminResponse, actionType, date, object);
	}

}
