package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.BoxEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.BoxHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.BoxRepository;

@Slf4j
@Component
public class BoxHelper {

	@Autowired
	private BoxRepository boxRepository;

	@Autowired
	private AdminHelper adminHelper;

	public BoxEntity getBoxEntity(final UUID box) throws DataBaseException, NotFoundException {
		Optional<BoxEntity> optionalBoxEntity = Optional.empty();
		try {
			BoxHelper.log.info("Starting searched the box with the id {}.", box);
			optionalBoxEntity = this.boxRepository.findById(box);
		} catch (final Exception exception) {
			BoxHelper.log.error("The box with the id {} could not read.", box, exception);
			throw new DataBaseException(Controller.CATALOG_BOX, DataBaseActionType.READ, box.toString());
		}

		if (optionalBoxEntity.isEmpty()) {
			BoxHelper.log.error("The box not found with the id {}.", box);
			throw new NotFoundException(Controller.CATALOG_BOX, "id", box.toString());
		}
		BoxHelper.log.info("Finished search the box with the id {}.", box);
		return optionalBoxEntity.get();
	}

	public BoxResponse convertBox(final BoxEntity boxEntity) {
		final var id = boxEntity.getId();
		final var name = boxEntity.getName();
		final var width = boxEntity.getWidth();
		final var height = boxEntity.getHeight();
		final var depth = boxEntity.getDepth();
		final var colorCode = boxEntity.getColorCode();
		final var isActive = boxEntity.isActive();
		return new BoxResponse(id, name, width, height, depth, colorCode, isActive);
	}

	public BoxHistoryResponse convertBoxHistory(final BoxHistoryEntity boxHistoryEntity) {
		final var id = boxHistoryEntity.getId();
		final var boxResponse = this.convertBox(boxHistoryEntity.getBoxEntity());
		final var adminResponse = this.adminHelper.convertAdmin(boxHistoryEntity.getAdminEntity());
		final var actionType = boxHistoryEntity.getActionType();
		final var date = boxHistoryEntity.getDate();
		final var object = boxHistoryEntity.getObject();
		return new BoxHistoryResponse(id, boxResponse, adminResponse, actionType, date, object);
	}

}
