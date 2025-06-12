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
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.UseCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.UseUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.UseHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.UseResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.UseEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.UseHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.AdminHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.UseHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.UseHistoryRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.UseRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.UseService;

@Slf4j
@Service
public class UseServiceImplementation implements UseService {

	@Autowired
	private UseRepository useRepository;

	@Autowired
	private UseHistoryRepository useHistoryRepository;

	@Autowired
	private UseHelper useHelper;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Autowired
	private PaginationHelper paginationHelper;

	@Override
	public UseResponse createUse(final UseCreateRequest useCreateRequest, final UUID admin) throws DataBaseException, ExistException {
		UseServiceImplementation.log.info("Starting created the use {}.", useCreateRequest.getName());
		this.validateUseNotExist(useCreateRequest.getName());
		try {
			final var useEntity = new UseEntity();
			useEntity.setName(useCreateRequest.getName());
			useEntity.setActive(true);
			final var newUseEntity = this.useRepository.save(useEntity);
			UseServiceImplementation.log.info("Finished create the use {}.", useCreateRequest.getName());

			UseServiceImplementation.log.info("Starting created the create history for {}.", useCreateRequest.getName());
			this.createUseHistory(newUseEntity, admin, DataBaseActionType.CREATE);
			UseServiceImplementation.log.info("Finished create the create history for {}.", useCreateRequest.getName());

			return this.useHelper.convertUse(newUseEntity);
		} catch (final Exception exception) {
			UseServiceImplementation.log.error("The use {} could not been create.", useCreateRequest.getName(), exception);
			throw new DataBaseException(Controller.CATALOG_USE, DataBaseActionType.CREATE, useCreateRequest.getName());
		}
	}

	@Override
	public UseResponse updateUse(final UseUpdateRequest useUpdateRequest, final UUID admin) throws DataBaseException, NotFoundException, ExistException {
		UseServiceImplementation.log.info("Starting updated the use with the id {}.", useUpdateRequest.getId());
		final var useEntity = this.useHelper.getUseEntity(useUpdateRequest.getId());
		useEntity.setName(useUpdateRequest.getName());
		useEntity.setActive(true);
		try {
			final var newUseEntity = this.useRepository.save(useEntity);
			UseServiceImplementation.log.info("Finished update the use with the id {}.", useUpdateRequest.getId());

			UseServiceImplementation.log.info("Starting created the update history for id {}.", useUpdateRequest.getId());
			this.createUseHistory(newUseEntity, admin, DataBaseActionType.UPDATE);
			UseServiceImplementation.log.info("Finished create the update history for id {}.", useUpdateRequest.getId());

			return this.useHelper.convertUse(newUseEntity);
		} catch (final Exception exception) {
			UseServiceImplementation.log.error("The use with the id {} could not been update.", useUpdateRequest.getId(), exception);
			throw new DataBaseException(Controller.CATALOG_USE, DataBaseActionType.UPDATE, useUpdateRequest.getName());
		}
	}

	@Override
	public UseResponse reactivateUse(final UUID use, final UUID admin) throws DataBaseException, NotFoundException, ExistException {
		UseServiceImplementation.log.info("Starting reactivated the use with the id {}.", use);
		final var useEntity = this.useHelper.getUseEntity(use);
		useEntity.setActive(true);
		try {
			final var newUseEntity = this.useRepository.save(useEntity);
			UseServiceImplementation.log.info("Finished reactivate the use with the id {}.", use);

			UseServiceImplementation.log.info("Starting created the reactivate history for id {}.", use);
			this.createUseHistory(newUseEntity, admin, DataBaseActionType.REACTIVATE);
			UseServiceImplementation.log.info("Finished create the reactivate history for id {}.", use);

			return this.useHelper.convertUse(newUseEntity);
		} catch (final Exception exception) {
			UseServiceImplementation.log.error("The use with the id {} could not been reactivate.", use, exception);
			throw new DataBaseException(Controller.CATALOG_USE, DataBaseActionType.REACTIVATE, use.toString());
		}
	}

	@Override
	public UseResponse deleteUse(final UUID use, final UUID admin) throws DataBaseException, NotFoundException {
		UseServiceImplementation.log.info("Starting updated the use with the id {}.", use);
		final var useEntity = this.useHelper.getUseEntity(use);
		useEntity.setActive(false);
		try {
			final var newUseEntity = this.useRepository.save(useEntity);
			UseServiceImplementation.log.info("Finished delete the use with the id {}.", use);

			UseServiceImplementation.log.info("Starting created the delete history for id {}.", use);
			this.createUseHistory(newUseEntity, admin, DataBaseActionType.DELETE);
			UseServiceImplementation.log.info("Finished create the delete history for id {}.", use);

			return this.useHelper.convertUse(newUseEntity);
		} catch (final Exception exception) {
			UseServiceImplementation.log.error("The use with the id {} could not been delete.", use, exception);
			throw new DataBaseException(Controller.CATALOG_USE, DataBaseActionType.DELETE, use.toString());
		}
	}

	@Override
	public GetResponse<UseResponse> getAllUse(final FilterRequest filterRequest, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		UseServiceImplementation.log.info("Starting searched of all use.");
		Page<UseEntity> pageUseEntity = null;
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
			pageUseEntity = this.useRepository.findAll(search, active, direction, pageable);
		} catch (final Exception exception) {
			UseServiceImplementation.log.error("The uses could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_USE, DataBaseActionType.READ);
		}
		final var listUseResponse = pageUseEntity.get().map(this.useHelper::convertUse).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageUseEntity);
		if (listUseResponse.isEmpty()) {
			UseServiceImplementation.log.error("The uses not found.");
			throw new NotFoundException(Controller.CATALOG_USE, "all");
		}
		UseServiceImplementation.log.info("Finished search the uses.");
		return new GetResponse<>(listUseResponse, paginationResponse);
	}

	@Override
	public GetResponse<UseHistoryResponse> getUseHistory(final UUID use, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		UseServiceImplementation.log.info("Starting searched history of use with the id {}.", use);
		final var useEntity = this.useHelper.getUseEntity(use);
		Page<UseHistoryEntity> pageUseHistoryEntity = null;
		try {
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageUseHistoryEntity = this.useHistoryRepository.findAllByUseEntity(useEntity, pageable);
		} catch (final Exception exception) {
			UseServiceImplementation.log.error("The history uses could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_USE, DataBaseActionType.READ);
		}
		final var listUseHistoryResponse = pageUseHistoryEntity.get().map(this.useHelper::convertUseHistory).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageUseHistoryEntity);
		UseServiceImplementation.log.info("Finished search history of use with the id {}.", use);
		return new GetResponse<>(listUseHistoryResponse, paginationResponse);
	}

	private void validateUseNotExist(final String name) throws ExistException {
		UseServiceImplementation.log.info("Starting validate the use if exist {}.", name);
		final var optionalUseEntity = this.useRepository.findByNameIgnoreCase(name);
		if (optionalUseEntity.isPresent()) {
			UseServiceImplementation.log.error("The use {} exist.", name);
			throw new ExistException(Controller.CATALOG_USE, "name", name);
		}
	}

	private void createUseHistory(final UseEntity useEntity, final UUID admin, final DataBaseActionType dataBaseActionType) {
		try {
			final var useHistoryEntity = new UseHistoryEntity();
			final var adminEntity = this.adminHelper.getAdminEntity(admin);
			final var objectMapper = new ObjectMapper();
			final var object = objectMapper.writeValueAsString(useEntity);
			useHistoryEntity.setUseEntity(useEntity);
			useHistoryEntity.setAdminEntity(adminEntity);
			useHistoryEntity.setActionType(dataBaseActionType);
			useHistoryEntity.setDate(new Date());
			useHistoryEntity.setObject(object);
			this.useHistoryRepository.save(useHistoryEntity);
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

}
