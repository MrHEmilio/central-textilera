package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FreighterCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FreighterUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FreighterHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FreighterResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.FreighterEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.FreighterHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.AdminHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.FreighterHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ImagesHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.FreighterHistoryRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.FreighterRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.FreighterService;

@Slf4j
@Service
public class FreighterServiceImplementation implements FreighterService {

	@Autowired
	private FreighterRepository freighterRepository;

	@Autowired
	private FreighterHistoryRepository freighterHistoryRepository;

	@Autowired
	private FreighterHelper freighterHelper;

	@Autowired
	private ImagesHelper imagesHelper;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private AdminHelper adminHelper;

	@Value("${path.images.freighters}")
	private String pathImagesFreighters;

	@Override
	public FreighterResponse createFreighter(final FreighterCreateRequest freighterCreateRequest, final UUID admin) throws DataBaseException, ExistException, ImageInvalidException {
		FreighterServiceImplementation.log.info("Starting created the freighter {}.", freighterCreateRequest.getName());
		this.validateFreighterNotExist(freighterCreateRequest.getName());
		this.imagesHelper.verifyImage(Controller.CATALOG_FREIGHTER, freighterCreateRequest.getImage());
		try {
			final var freighterEntity = new FreighterEntity();
			freighterEntity.setName(freighterCreateRequest.getName());
			freighterEntity.setCostPerDistance(freighterCreateRequest.getCostPerDistance());
			freighterEntity.setCostPerWeight(freighterCreateRequest.getCostPerWeight());
			freighterEntity.setActive(true);
			final var newFreighterEntity = this.freighterRepository.save(freighterEntity);
			FreighterServiceImplementation.log.info("Finished create the freighter {}.", freighterCreateRequest.getName());

			FreighterServiceImplementation.log.info("Starting saved the freighter image.");
			this.imagesHelper.saveImage(freighterCreateRequest.getImage(), this.pathImagesFreighters, newFreighterEntity.getId());
			FreighterServiceImplementation.log.info("Finished save the freighter image.");

			FreighterServiceImplementation.log.info("Starting created the create history {}.", freighterCreateRequest.getName());
			this.createFreighterHistory(newFreighterEntity, admin, DataBaseActionType.CREATE);
			FreighterServiceImplementation.log.info("Finished create the create history {}.", freighterCreateRequest.getName());

			return this.freighterHelper.convertFreighter(newFreighterEntity);
		} catch (final Exception exception) {
			FreighterServiceImplementation.log.error("The freighter {} could not been create.", freighterCreateRequest.getName(), exception);
			throw new DataBaseException(Controller.CATALOG_FREIGHTER, DataBaseActionType.CREATE, freighterCreateRequest.getName());
		}
	}

	@Override
	public FreighterResponse updateFreighter(final FreighterUpdateRequest freighterUpdateRequest, final UUID admin) throws DataBaseException, NotFoundException, ExistException {
		FreighterServiceImplementation.log.info("Starting updated the freighter with the id {}.", freighterUpdateRequest.getId());
		final var freighterEntity = this.freighterHelper.getFreighterEntity(freighterUpdateRequest.getId());
		freighterEntity.setName(freighterUpdateRequest.getName());
		freighterEntity.setCostPerDistance(freighterUpdateRequest.getCostPerDistance());
		freighterEntity.setCostPerWeight(freighterUpdateRequest.getCostPerWeight());
		freighterEntity.setActive(true);
		try {
			final var newFreighterEntity = this.freighterRepository.save(freighterEntity);
			FreighterServiceImplementation.log.info("Finished update the freighter with the id {}.", freighterUpdateRequest.getId());

			FreighterServiceImplementation.log.info("Starting created the update history of id {}.", freighterUpdateRequest.getId());
			this.createFreighterHistory(newFreighterEntity, admin, DataBaseActionType.UPDATE);
			FreighterServiceImplementation.log.info("Finished create the update history of id {}.", freighterUpdateRequest.getId());

			return this.freighterHelper.convertFreighter(newFreighterEntity);
		} catch (final Exception exception) {
			FreighterServiceImplementation.log.error("The freighter with the id {} could not been update.", freighterUpdateRequest.getId(), exception);
			throw new DataBaseException(Controller.CATALOG_FREIGHTER, DataBaseActionType.UPDATE, freighterUpdateRequest.getName());
		}
	}

	@Override
	public FreighterResponse reactivateFreighter(final UUID freighter, final UUID admin) throws DataBaseException, NotFoundException {
		FreighterServiceImplementation.log.info("Starting reactivated the freighter with the id {}.", freighter);
		final var freighterEntity = this.freighterHelper.getFreighterEntity(freighter);
		freighterEntity.setActive(true);
		try {
			final var newFreighterEntity = this.freighterRepository.save(freighterEntity);
			FreighterServiceImplementation.log.info("Finished reactivate the freighter with the id {}.", freighter);

			FreighterServiceImplementation.log.info("Starting created the reactivate history of id {}.", freighter);
			this.createFreighterHistory(newFreighterEntity, admin, DataBaseActionType.REACTIVATE);
			FreighterServiceImplementation.log.info("Finished create the reactivate history of id {}.", freighter);

			return this.freighterHelper.convertFreighter(newFreighterEntity);
		} catch (final Exception exception) {
			FreighterServiceImplementation.log.error("The freighter with the id {} could not been reactivate.", freighter, exception);
			throw new DataBaseException(Controller.CATALOG_FREIGHTER, DataBaseActionType.REACTIVATE, freighter.toString());
		}
	}

	@Override
	public FreighterResponse deleteFreighter(final UUID freighter, final UUID admin) throws DataBaseException, NotFoundException {
		FreighterServiceImplementation.log.info("Starting deleted the freighter with the id {}.", freighter);
		final var freighterEntity = this.freighterHelper.getFreighterEntity(freighter);
		freighterEntity.setActive(false);
		try {
			final var newFreighterEntity = this.freighterRepository.save(freighterEntity);
			FreighterServiceImplementation.log.info("Finished delete the freighter with the id {}.", freighter);

			FreighterServiceImplementation.log.info("Starting created the delete history of id {}.", freighter);
			this.createFreighterHistory(newFreighterEntity, admin, DataBaseActionType.DELETE);
			FreighterServiceImplementation.log.info("Finished create the delete history of id {}.", freighter);

			return this.freighterHelper.convertFreighter(newFreighterEntity);
		} catch (final Exception exception) {
			FreighterServiceImplementation.log.error("The freighter with the id {} could not been delete.", freighter, exception);
			throw new DataBaseException(Controller.CATALOG_FREIGHTER, DataBaseActionType.DELETE, freighter.toString());
		}
	}

	@Override
	public GetResponse<FreighterResponse> getAllFreighter(final FilterRequest filterRequest, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		FreighterServiceImplementation.log.info("Starting searched of all freighters.");
		Page<FreighterEntity> pageFreighterEntity = null;
		try {
			final var search = filterRequest.getSearch();
			final var active = filterRequest.getActive();
			String direction = null;
			if (paginationRequest.getTypeSort() != null)
				direction = paginationRequest.getTypeSort().name();
			paginationRequest.setColumnSort(null);
			paginationRequest.setTypeSort(null);
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageFreighterEntity = this.freighterRepository.findAll(search, active, direction, pageable);
		} catch (final Exception exception) {
			FreighterServiceImplementation.log.error("The freighter could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_FREIGHTER, DataBaseActionType.READ);
		}
		final var listFreighterResponse = pageFreighterEntity.get().map(this.freighterHelper::convertFreighter).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageFreighterEntity);
		if (listFreighterResponse.isEmpty()) {
			FreighterServiceImplementation.log.error("The freighter not found.");
			throw new NotFoundException(Controller.CATALOG_FREIGHTER, "all");
		}
		FreighterServiceImplementation.log.info("Finished search the freighter.");
		return new GetResponse<>(listFreighterResponse, paginationResponse);
	}

	@Override
	public GetResponse<FreighterHistoryResponse> getFreighterHistory(final UUID freighter, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		FreighterServiceImplementation.log.info("Starting searched history of freighter with the id {}.", freighter);
		final var freighterEntity = this.freighterHelper.getFreighterEntity(freighter);
		Page<FreighterHistoryEntity> pageFreighterHistoryEntity = null;
		try {
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageFreighterHistoryEntity = this.freighterHistoryRepository.findAllByFreighterEntity(freighterEntity, pageable);
		} catch (final Exception exception) {
			FreighterServiceImplementation.log.error("The history freighter could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_FREIGHTER, DataBaseActionType.READ);
		}
		final var listFreighterHistoryResponse = pageFreighterHistoryEntity.get().map(this.freighterHelper::convertFreighterHistory).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageFreighterHistoryEntity);
		FreighterServiceImplementation.log.info("Finished search history of freighter with the id {}.", freighter);
		return new GetResponse<>(listFreighterHistoryResponse, paginationResponse);
	}

	private void validateFreighterNotExist(final String name) throws ExistException {
		FreighterServiceImplementation.log.info("Starting validate the freighter if exist {}.", name);
		final var optionalFreighterEntity = this.freighterRepository.findByNameIgnoreCase(name);
		if (optionalFreighterEntity.isPresent()) {
			FreighterServiceImplementation.log.error("The freighter {} exist.", name);
			throw new ExistException(Controller.CATALOG_FREIGHTER, "name", name);
		}
	}

	private void createFreighterHistory(final FreighterEntity freighterEntity, final UUID admin, final DataBaseActionType dataBaseActionType) {
		try {
			final var freighterHistoryEntity = new FreighterHistoryEntity();
			final var adminEntity = this.adminHelper.getAdminEntity(admin);
			final var objectMapper = new ObjectMapper();
			final var object = objectMapper.writeValueAsString(freighterEntity);
			freighterHistoryEntity.setFreighterEntity(freighterEntity);
			freighterHistoryEntity.setAdminEntity(adminEntity);
			freighterHistoryEntity.setActionType(dataBaseActionType);
			freighterHistoryEntity.setDate(new Date());
			freighterHistoryEntity.setObject(object);
			this.freighterHistoryRepository.save(freighterHistoryEntity);
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

}
