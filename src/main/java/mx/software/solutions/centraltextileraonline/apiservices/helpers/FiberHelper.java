package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FiberHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FiberResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.FiberEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.FiberHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.FiberRepository;

@Slf4j
@Component
public class FiberHelper {

	@Autowired
	private FiberRepository fiberRepository;

	@Autowired
	private AdminHelper adminHelper;

	public FiberEntity getFiberEntity(final UUID fiber) throws DataBaseException, NotFoundException {
		Optional<FiberEntity> optionalFiberEntity = Optional.empty();
		try {
			FiberHelper.log.info("Starting searched the fiber with the id {}.", fiber);
			optionalFiberEntity = this.fiberRepository.findById(fiber);
		} catch (final Exception exception) {
			FiberHelper.log.error("The fiber with the id {} could not read.", fiber, exception);
			throw new DataBaseException(Controller.CATALOG_FIBER, DataBaseActionType.READ, fiber.toString());
		}

		if (optionalFiberEntity.isEmpty()) {
			FiberHelper.log.error("The fiber not found with the id {}.", fiber);
			throw new NotFoundException(Controller.CATALOG_FIBER, "id", fiber.toString());
		}
		FiberHelper.log.info("Finished search the fiber with the id {}.", fiber);
		return optionalFiberEntity.get();
	}

	public List<FiberEntity> getFiberEntities(final List<UUID> fibers) {
		return fibers.stream().map(fiber -> {
			try {
				return this.getFiberEntity(fiber);
			} catch (DataBaseException | NotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}).collect(Collectors.toList());
	}

	public FiberResponse convertFiber(final FiberEntity fiberEntity) {
		final var id = fiberEntity.getId();
		final var name = fiberEntity.getName();
		final var isActive = fiberEntity.isActive();
		return new FiberResponse(id, name, isActive);
	}

	public FiberHistoryResponse convertFiberHistory(final FiberHistoryEntity fiberHistoryEntity) {
		final var id = fiberHistoryEntity.getId();
		final var fiberResponse = this.convertFiber(fiberHistoryEntity.getFiberEntity());
		final var adminResponse = this.adminHelper.convertAdmin(fiberHistoryEntity.getAdminEntity());
		final var actionType = fiberHistoryEntity.getActionType();
		final var date = fiberHistoryEntity.getDate();
		final var object = fiberHistoryEntity.getObject();
		return new FiberHistoryResponse(id, fiberResponse, adminResponse, actionType, date, object);
	}

}
