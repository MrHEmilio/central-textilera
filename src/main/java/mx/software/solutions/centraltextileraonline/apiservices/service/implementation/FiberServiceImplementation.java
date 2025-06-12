package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FiberCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FiberUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FiberHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.FiberResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.FiberEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.FiberHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.AdminHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.FiberHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.FiberHistoryRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.FiberRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.FiberService;

@Slf4j
@Service
public class FiberServiceImplementation implements FiberService {

	@Autowired
	private FiberRepository fiberRepository;

	@Autowired
	private FiberHistoryRepository fiberHistoryRepository;

	@Autowired
	private FiberHelper fiberHelper;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Override
	public FiberResponse createFiber(final FiberCreateRequest fiberCreateRequest, final UUID admin) throws DataBaseException, ExistException {
		FiberServiceImplementation.log.info("Starting created the fiber {}.", fiberCreateRequest.getName());
		this.validateFiberNotExist(fiberCreateRequest.getName());
		try {
			final var fiberEntity = new FiberEntity();
			fiberEntity.setName(fiberCreateRequest.getName());
			fiberEntity.setActive(true);
			final var newFiberEntity = this.fiberRepository.save(fiberEntity);
			FiberServiceImplementation.log.info("Finished create the fiber {}.", fiberCreateRequest.getName());

			FiberServiceImplementation.log.info("Starting created the create history {}.", fiberCreateRequest.getName());
			this.createFiberHistory(newFiberEntity, admin, DataBaseActionType.CREATE);
			FiberServiceImplementation.log.info("Finished create the create history {}.", fiberCreateRequest.getName());

			return this.fiberHelper.convertFiber(newFiberEntity);
		} catch (final Exception exception) {
			FiberServiceImplementation.log.error("The fiber {} could not been create.", fiberCreateRequest.getName(), exception);
			throw new DataBaseException(Controller.CATALOG_FIBER, DataBaseActionType.CREATE, fiberCreateRequest.getName());
		}
	}

	@Override
	public FiberResponse updateFiber(final FiberUpdateRequest fiberUpdateRequest, final UUID admin) throws DataBaseException, NotFoundException, ExistException {
		FiberServiceImplementation.log.info("Starting updated the fiber with the id {}.", fiberUpdateRequest.getId());
		final var fiberEntity = this.fiberHelper.getFiberEntity(fiberUpdateRequest.getId());
		fiberEntity.setName(fiberUpdateRequest.getName());
		fiberEntity.setActive(true);
		try {
			final var newFiberEntity = this.fiberRepository.save(fiberEntity);
			FiberServiceImplementation.log.info("Finished update the fiber with the id {}.", fiberUpdateRequest.getId());

			FiberServiceImplementation.log.info("Starting created the update history of id {}.", fiberUpdateRequest.getId());
			this.createFiberHistory(newFiberEntity, admin, DataBaseActionType.UPDATE);
			FiberServiceImplementation.log.info("Finished create the update history of id {}.", fiberUpdateRequest.getId());

			return this.fiberHelper.convertFiber(newFiberEntity);
		} catch (final Exception exception) {
			FiberServiceImplementation.log.error("The fiber with the id {} could not been update.", fiberUpdateRequest.getId(), exception);
			throw new DataBaseException(Controller.CATALOG_FIBER, DataBaseActionType.UPDATE, fiberUpdateRequest.getName());
		}
	}

	@Override
	public FiberResponse reactivateFiber(final UUID fiber, final UUID admin) throws DataBaseException, NotFoundException {
		FiberServiceImplementation.log.info("Starting reactivated the fiber with the id {}.", fiber);
		final var fiberEntity = this.fiberHelper.getFiberEntity(fiber);
		fiberEntity.setActive(true);
		try {
			final var newFiberEntity = this.fiberRepository.save(fiberEntity);
			FiberServiceImplementation.log.info("Finished reactivate the fiber with the id {}.", fiber);

			FiberServiceImplementation.log.info("Starting created the reactivate history of id {}.", fiber);
			this.createFiberHistory(newFiberEntity, admin, DataBaseActionType.REACTIVATE);
			FiberServiceImplementation.log.info("Finished create the reactivate history of id {}.", fiber);

			return this.fiberHelper.convertFiber(newFiberEntity);
		} catch (final Exception exception) {
			FiberServiceImplementation.log.error("The fiber with the id {} could not been reactivate.", fiber, exception);
			throw new DataBaseException(Controller.CATALOG_FIBER, DataBaseActionType.REACTIVATE, fiber.toString());
		}
	}

	@Override
	public FiberResponse deleteFiber(final UUID fiber, final UUID admin) throws DataBaseException, NotFoundException {
		FiberServiceImplementation.log.info("Starting deleted the fiber with the id {}.", fiber);
		final var fiberEntity = this.fiberHelper.getFiberEntity(fiber);
		fiberEntity.setActive(false);
		try {
			final var newFiberEntity = this.fiberRepository.save(fiberEntity);
			FiberServiceImplementation.log.info("Finished delete the fiber with the id {}.", fiber);

			FiberServiceImplementation.log.info("Starting created the delete history of id {}.", fiber);
			this.createFiberHistory(newFiberEntity, admin, DataBaseActionType.DELETE);
			FiberServiceImplementation.log.info("Finished create the delete history of id {}.", fiber);

			return this.fiberHelper.convertFiber(newFiberEntity);
		} catch (final Exception exception) {
			FiberServiceImplementation.log.error("The fiber with the id {} could not been delete.", fiber, exception);
			throw new DataBaseException(Controller.CATALOG_FIBER, DataBaseActionType.DELETE, fiber.toString());
		}
	}

	@Override
	public GetResponse<FiberResponse> getAllFiber(final FilterRequest filterRequest, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		FiberServiceImplementation.log.info("Starting searched of all fibers.");
		Page<FiberEntity> pageFiberEntity = null;
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
			pageFiberEntity = this.fiberRepository.findAll(search, active, direction, pageable);
		} catch (final Exception exception) {
			FiberServiceImplementation.log.error("The fibers could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_FIBER, DataBaseActionType.READ);
		}
		final var listFiberResponse = pageFiberEntity.get().map(this.fiberHelper::convertFiber).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageFiberEntity);
		if (listFiberResponse.isEmpty()) {
			FiberServiceImplementation.log.error("The fibers not found.");
			throw new NotFoundException(Controller.CATALOG_FIBER, "all");
		}
		FiberServiceImplementation.log.info("Finished search the fibers.");
		return new GetResponse<>(listFiberResponse, paginationResponse);
	}

	@Override
	public GetResponse<FiberHistoryResponse> getFiberHistory(final UUID fiber, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		FiberServiceImplementation.log.info("Starting searched history of fiber with the id {}.", fiber);
		final var fiberEntity = this.fiberHelper.getFiberEntity(fiber);
		Page<FiberHistoryEntity> pageFiberHistoryEntity = null;
		try {
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageFiberHistoryEntity = this.fiberHistoryRepository.findAllByFiberEntity(fiberEntity, pageable);
		} catch (final Exception exception) {
			FiberServiceImplementation.log.error("The history fiber could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_FIBER, DataBaseActionType.READ);
		}
		final var listFiberHistoryResponse = pageFiberHistoryEntity.get().map(this.fiberHelper::convertFiberHistory).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageFiberHistoryEntity);
		FiberServiceImplementation.log.info("Finished search history of fiber with the id {}.", fiber);
		return new GetResponse<>(listFiberHistoryResponse, paginationResponse);
	}

	private void validateFiberNotExist(final String name) throws ExistException {
		FiberServiceImplementation.log.info("Starting validate the fiber if exist {}.", name);
		final var optionalFiberEntity = this.fiberRepository.findByNameIgnoreCase(name);
		if (optionalFiberEntity.isPresent()) {
			FiberServiceImplementation.log.error("The fiber {} exist.", name);
			throw new ExistException(Controller.CATALOG_FIBER, "name", name);
		}
	}

	private void createFiberHistory(final FiberEntity fiberEntity, final UUID admin, final DataBaseActionType dataBaseActionType) {
		try {
			final var fiberHistoryEntity = new FiberHistoryEntity();
			final var adminEntity = this.adminHelper.getAdminEntity(admin);
			final var objectMapper = new ObjectMapper();
			final var object = objectMapper.writeValueAsString(fiberEntity);
			fiberHistoryEntity.setFiberEntity(fiberEntity);
			fiberHistoryEntity.setAdminEntity(adminEntity);
			fiberHistoryEntity.setActionType(dataBaseActionType);
			fiberHistoryEntity.setDate(new Date());
			fiberHistoryEntity.setObject(object);
			this.fiberHistoryRepository.save(fiberHistoryEntity);
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

}
