package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.UseHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.UseResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.UseEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.UseHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.UseRepository;

@Slf4j
@Component
public class UseHelper {

	@Autowired
	private UseRepository useRepository;

	@Autowired
	private AdminHelper adminHelper;

	public UseEntity getUseEntity(final UUID use) throws DataBaseException, NotFoundException {
		Optional<UseEntity> optionalUseEntity = Optional.empty();
		try {
			UseHelper.log.info("Starting searched the use with the id {}.", use);
			optionalUseEntity = this.useRepository.findById(use);
		} catch (final Exception exception) {
			UseHelper.log.error("The use with the id {} could not read.", use, exception);
			throw new DataBaseException(Controller.CATALOG_USE, DataBaseActionType.READ, use.toString());
		}

		if (optionalUseEntity.isEmpty()) {
			UseHelper.log.error("The use not found with the id {}.", use);
			throw new NotFoundException(Controller.CATALOG_USE, "id", use.toString());
		}
		UseHelper.log.info("Finished search the use with the id {}.", use);
		return optionalUseEntity.get();
	}

	public List<UseEntity> getUseEntities(final List<UUID> uses) {
		return uses.stream().map(use -> {
			try {
				return this.getUseEntity(use);
			} catch (DataBaseException | NotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}).collect(Collectors.toList());
	}

	public UseResponse convertUse(final UseEntity useEntity) {
		final var id = useEntity.getId();
		final var name = useEntity.getName();
		final var isActive = useEntity.isActive();
		return new UseResponse(id, name, isActive);
	}

	public UseHistoryResponse convertUseHistory(final UseHistoryEntity useHistoryEntity) {
		final var id = useHistoryEntity.getId();
		final var useResponse = this.convertUse(useHistoryEntity.getUseEntity());
		final var adminResponse = this.adminHelper.convertAdmin(useHistoryEntity.getAdminEntity());
		final var actionType = useHistoryEntity.getActionType();
		final var date = useHistoryEntity.getDate();
		final var object = useHistoryEntity.getObject();
		return new UseHistoryResponse(id, useResponse, adminResponse, actionType, date, object);
	}

}
