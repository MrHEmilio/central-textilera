package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ColorHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ColorResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ColorEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ColorHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ColorRepository;

@Slf4j
@Component
public class ColorHelper {

	@Autowired
	private ColorRepository colorRepository;

	@Autowired
	private AdminHelper adminHelper;

	public ColorEntity getColorEntity(final UUID color) throws DataBaseException, NotFoundException {
		Optional<ColorEntity> optionalColorEntity = Optional.empty();
		try {
			ColorHelper.log.info("Starting searched the color with the id {}.", color);
			optionalColorEntity = this.colorRepository.findById(color);
		} catch (final Exception exception) {
			ColorHelper.log.error("The color with the id {} could not read.", color, exception);
			throw new DataBaseException(Controller.CATALOG_COLOR, DataBaseActionType.READ, color.toString());
		}

		if (optionalColorEntity.isEmpty()) {
			ColorHelper.log.error("The color not found with the id {}.", color);
			throw new NotFoundException(Controller.CATALOG_COLOR, "id", color.toString());
		}
		ColorHelper.log.info("Finished search the color with the id {}.", color);
		return optionalColorEntity.get();
	}

	public List<ColorEntity> getColorEntities(final List<UUID> colors) {
		return colors.stream().map(color -> {
			try {
				return this.getColorEntity(color);
			} catch (DataBaseException | NotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}).collect(Collectors.toList());
	}

	public ColorResponse convertColor(final ColorEntity colorEntity) {
		final var id = colorEntity.getId();
		final var name = colorEntity.getName();
		final var code = colorEntity.getCode();
		final var isActive = colorEntity.isActive();
		return new ColorResponse(id, name, code, isActive);
	}

	public ColorHistoryResponse convertColorHistory(final ColorHistoryEntity colorHistoryEntity) {
		final var id = colorHistoryEntity.getId();
		final var colorResponse = this.convertColor(colorHistoryEntity.getColorEntity());
		final var adminResponse = this.adminHelper.convertAdmin(colorHistoryEntity.getAdminEntity());
		final var actionType = colorHistoryEntity.getActionType();
		final var date = colorHistoryEntity.getDate();
		final var object = colorHistoryEntity.getObject();
		return new ColorHistoryResponse(id, colorResponse, adminResponse, actionType, date, object);
	}

}
