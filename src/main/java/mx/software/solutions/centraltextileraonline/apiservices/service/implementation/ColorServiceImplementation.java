package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ColorCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.ColorUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ColorHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.ColorResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ColorEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ColorHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.AdminHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ColorHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ColorHistoryRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ColorRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.ColorService;

@Slf4j
@Service
public class ColorServiceImplementation implements ColorService {

	@Autowired
	private ColorRepository colorRepository;

	@Autowired
	private ColorHistoryRepository colorHistoryRepository;

	@Autowired
	private ColorHelper colorHelper;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Override
	public ColorResponse createColor(final ColorCreateRequest colorCreateRequest, final UUID admin) throws DataBaseException, ExistException {
		ColorServiceImplementation.log.info("Starting created the color {}.", colorCreateRequest.getName());
		this.validateNameNotExist(colorCreateRequest.getName());
		this.validateCodeNotExist(colorCreateRequest.getCode());
		try {
			final var colorEntity = new ColorEntity();
			colorEntity.setName(colorCreateRequest.getName());
			colorEntity.setCode(colorCreateRequest.getCode());
			colorEntity.setActive(true);
			final var newColorEntity = this.colorRepository.save(colorEntity);
			ColorServiceImplementation.log.info("Finished create the color {}.", colorCreateRequest.getName());

			ColorServiceImplementation.log.info("Starting created the create history {}.", colorCreateRequest.getName());
			this.createColorHistory(newColorEntity, admin, DataBaseActionType.CREATE);
			ColorServiceImplementation.log.info("Finished create the create history {}.", colorCreateRequest.getName());

			return this.colorHelper.convertColor(newColorEntity);
		} catch (final Exception exception) {
			ColorServiceImplementation.log.error("The color {} could not been create.", colorCreateRequest.getName(), exception);
			throw new DataBaseException(Controller.CATALOG_COLOR, DataBaseActionType.CREATE, colorCreateRequest.getName());
		}
	}

	@Override
	public ColorResponse updateColor(final ColorUpdateRequest colorUpdateRequest, final UUID admin) throws DataBaseException, NotFoundException, ExistException {
		ColorServiceImplementation.log.info("Starting updated the color with the id {}.", colorUpdateRequest.getId());
		final var colorEntity = this.colorHelper.getColorEntity(colorUpdateRequest.getId());
		colorEntity.setName(colorUpdateRequest.getName());
		colorEntity.setCode(colorUpdateRequest.getCode());
		colorEntity.setActive(true);
		try {
			final var newColorEntity = this.colorRepository.save(colorEntity);
			ColorServiceImplementation.log.info("Finished update the color with the id {}.", colorUpdateRequest.getId());

			ColorServiceImplementation.log.info("Starting created the update history of id {}.", colorUpdateRequest.getId());
			this.createColorHistory(newColorEntity, admin, DataBaseActionType.UPDATE);
			ColorServiceImplementation.log.info("Finished create the update history of id {}.", colorUpdateRequest.getId());

			return this.colorHelper.convertColor(newColorEntity);
		} catch (final Exception exception) {
			ColorServiceImplementation.log.error("The color with the id {} could not been update.", colorUpdateRequest.getId(), exception);
			throw new DataBaseException(Controller.CATALOG_COLOR, DataBaseActionType.UPDATE, colorUpdateRequest.getName());
		}
	}

	@Override
	public ColorResponse reactivateColor(final UUID color, final UUID admin) throws DataBaseException, NotFoundException {
		ColorServiceImplementation.log.info("Starting reactivated the color with the id {}.", color);
		final var colorEntity = this.colorHelper.getColorEntity(color);
		colorEntity.setActive(true);
		try {
			final var newColorEntity = this.colorRepository.save(colorEntity);
			ColorServiceImplementation.log.info("Finished reactivate the color with the id {}.", color);

			ColorServiceImplementation.log.info("Starting created the reactivate history of id {}.", color);
			this.createColorHistory(newColorEntity, admin, DataBaseActionType.REACTIVATE);
			ColorServiceImplementation.log.info("Finished create the reactivate history of id {}.", color);

			return this.colorHelper.convertColor(newColorEntity);
		} catch (final Exception exception) {
			ColorServiceImplementation.log.error("The color with the id {} could not been reactivate.", color, exception);
			throw new DataBaseException(Controller.CATALOG_COLOR, DataBaseActionType.REACTIVATE, color.toString());
		}
	}

	@Override
	public ColorResponse deleteColor(final UUID color, final UUID admin) throws DataBaseException, NotFoundException {
		ColorServiceImplementation.log.info("Starting deleted the color with the id {}.", color);
		final var colorEntity = this.colorHelper.getColorEntity(color);
		colorEntity.setActive(false);
		try {
			final var newColorEntity = this.colorRepository.save(colorEntity);
			ColorServiceImplementation.log.info("Finished delete the color with the id {}.", color);

			ColorServiceImplementation.log.info("Starting created the delete history of id {}.", color);
			this.createColorHistory(newColorEntity, admin, DataBaseActionType.DELETE);
			ColorServiceImplementation.log.info("Finished create the delete history of id {}.", color);

			return this.colorHelper.convertColor(newColorEntity);
		} catch (final Exception exception) {
			ColorServiceImplementation.log.error("The color with the id {} could not been delete.", color, exception);
			throw new DataBaseException(Controller.CATALOG_COLOR, DataBaseActionType.DELETE, color.toString());
		}
	}

	@Override
	public GetResponse<ColorResponse> getAllColor(final FilterRequest filterRequest, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		ColorServiceImplementation.log.info("Starting searched of all colors.");
		Page<ColorEntity> pageColorEntity = null;
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
			pageColorEntity = this.colorRepository.findAll(search, active, direction, pageable);
		} catch (final Exception exception) {
			ColorServiceImplementation.log.error("The colors could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_COLOR, DataBaseActionType.READ);
		}
		final var listColorResponse = pageColorEntity.get().map(this.colorHelper::convertColor).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageColorEntity);
		if (listColorResponse.isEmpty()) {
			ColorServiceImplementation.log.error("The colors not found.");
			throw new NotFoundException(Controller.CATALOG_COLOR, "all");
		}
		ColorServiceImplementation.log.info("Finished search the colors.");
		return new GetResponse<>(listColorResponse, paginationResponse);
	}

	@Override
	public GetResponse<ColorHistoryResponse> getColorHistory(final UUID color, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		ColorServiceImplementation.log.info("Starting searched history of color with the id {}.", color);
		final var colorEntity = this.colorHelper.getColorEntity(color);
		Page<ColorHistoryEntity> pageColorHistoryEntity = null;
		try {
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageColorHistoryEntity = this.colorHistoryRepository.findAllByColorEntity(colorEntity, pageable);
		} catch (final Exception exception) {
			ColorServiceImplementation.log.error("The history color could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_COLOR, DataBaseActionType.READ);
		}
		final var listColorHistoryResponse = pageColorHistoryEntity.get().map(this.colorHelper::convertColorHistory).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageColorHistoryEntity);
		ColorServiceImplementation.log.info("Finished search history of color with the id {}.", color);
		return new GetResponse<>(listColorHistoryResponse, paginationResponse);
	}

	private void validateNameNotExist(final String name) throws ExistException {
		ColorServiceImplementation.log.info("Starting validate the color name if exist {}.", name);
		final var optionalColorEntity = this.colorRepository.findByNameIgnoreCase(name);
		if (optionalColorEntity.isPresent()) {
			ColorServiceImplementation.log.error("The color name {} exist.", name);
			throw new ExistException(Controller.CATALOG_COLOR, "name", name);
		}
	}

	private void validateCodeNotExist(final String code) throws ExistException {
		ColorServiceImplementation.log.info("Starting validate the color code if exist {}.", code);
		final var optionalColorEntity = this.colorRepository.findByCodeIgnoreCase(code);
		if (optionalColorEntity.isPresent()) {
			ColorServiceImplementation.log.error("The color code {} exist.", code);
			throw new ExistException(Controller.CATALOG_COLOR, "code", code);
		}
	}

	private void createColorHistory(final ColorEntity colorEntity, final UUID admin, final DataBaseActionType dataBaseActionType) {
		try {
			final var colorHistoryEntity = new ColorHistoryEntity();
			final var adminEntity = this.adminHelper.getAdminEntity(admin);
			final var objectMapper = new ObjectMapper();
			final var object = objectMapper.writeValueAsString(colorEntity);
			colorHistoryEntity.setColorEntity(colorEntity);
			colorHistoryEntity.setAdminEntity(adminEntity);
			colorHistoryEntity.setActionType(dataBaseActionType);
			colorHistoryEntity.setDate(new Date());
			colorHistoryEntity.setObject(object);
			this.colorHistoryRepository.save(colorHistoryEntity);
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

}
