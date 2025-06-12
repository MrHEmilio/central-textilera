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
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CollectionCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CollectionUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CollectionHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CollectionResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CollectionEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CollectionHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ImageInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.AdminHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.CollectionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.ImagesHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SessionHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.CollectionHistoryRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.CollectionRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.CollectionService;

@Slf4j
@Service
public class CollectionServiceImplementation implements CollectionService {

	@Autowired
	private CollectionRepository collectionRepository;

	@Autowired
	private CollectionHistoryRepository collectionHistoryRepository;

	@Autowired
	private CollectionHelper collectionHelper;

	@Autowired
	private ImagesHelper imagesHelper;

	@Autowired
	private PaginationHelper paginationHelper;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private SessionHelper sessionHelper;

	@Value("${path.images.collections}")
	private String pathImagesCollections;

	@Override
	public CollectionResponse createCollection(final CollectionCreateRequest collectionCreateRequest, final UUID admin) throws DataBaseException, ExistException, ImageInvalidException {
		CollectionServiceImplementation.log.info("Starting created the collection {}.", collectionCreateRequest.getName());
		this.validateCollectionNotExist(collectionCreateRequest.getName());
		this.imagesHelper.verifyImage(Controller.CATALOG_COLLECTION, collectionCreateRequest.getImage());
		try {
			final var collectionEntity = new CollectionEntity();
			collectionEntity.setName(collectionCreateRequest.getName());
			collectionEntity.setActive(true);
			final var newCollectionEntity = this.collectionRepository.save(collectionEntity);
			CollectionServiceImplementation.log.info("Finished create the collection {}.", collectionCreateRequest.getName());

			CollectionServiceImplementation.log.info("Starting saved the use image.");
			this.imagesHelper.saveImage(collectionCreateRequest.getImage(), this.pathImagesCollections, newCollectionEntity.getId());
			CollectionServiceImplementation.log.info("Finished save the use image.");

			CollectionServiceImplementation.log.info("Starting created the create history {}.", collectionCreateRequest.getName());
			this.createCollectionHistory(newCollectionEntity, admin, DataBaseActionType.CREATE);
			CollectionServiceImplementation.log.info("Finished create the create history {}.", collectionCreateRequest.getName());

			return this.collectionHelper.convertCollection(newCollectionEntity);
		} catch (final Exception exception) {
			CollectionServiceImplementation.log.error("The collection {} could not been create.", collectionCreateRequest.getName(), exception);
			throw new DataBaseException(Controller.CATALOG_COLLECTION, DataBaseActionType.CREATE, collectionCreateRequest.getName());
		}
	}

	@Override
	public CollectionResponse updateCollection(final CollectionUpdateRequest collectionUpdateRequest, final UUID admin) throws DataBaseException, NotFoundException, ExistException {
		CollectionServiceImplementation.log.info("Starting updated the collection with the id {}.", collectionUpdateRequest.getId());
		final var collectionEntity = this.collectionHelper.getCollectionEntity(collectionUpdateRequest.getId());
		collectionEntity.setName(collectionUpdateRequest.getName());
		collectionEntity.setActive(true);
		try {
			final var newCollectionEntity = this.collectionRepository.save(collectionEntity);
			CollectionServiceImplementation.log.info("Finished update the collection with the id {}.", collectionUpdateRequest.getId());

			CollectionServiceImplementation.log.info("Starting created the update history of id {}.", collectionUpdateRequest.getId());
			this.createCollectionHistory(newCollectionEntity, admin, DataBaseActionType.UPDATE);
			CollectionServiceImplementation.log.info("Finished create the update history of id {}.", collectionUpdateRequest.getId());

			return this.collectionHelper.convertCollection(newCollectionEntity);
		} catch (final Exception exception) {
			CollectionServiceImplementation.log.error("The collection with the id {} could not been update.", collectionUpdateRequest.getId(), exception);
			throw new DataBaseException(Controller.CATALOG_COLLECTION, DataBaseActionType.UPDATE, collectionUpdateRequest.getName());
		}
	}

	@Override
	public CollectionResponse reactivateCollection(final UUID collection, final UUID admin) throws DataBaseException, NotFoundException {
		CollectionServiceImplementation.log.info("Starting reactivated the collection with the id {}.", collection);
		final var collectionEntity = this.collectionHelper.getCollectionEntity(collection);
		collectionEntity.setActive(true);
		try {
			final var newCollectionEntity = this.collectionRepository.save(collectionEntity);
			CollectionServiceImplementation.log.info("Finished reactivate the collection with the id {}.", collection);

			CollectionServiceImplementation.log.info("Starting created the reactivate history of id {}.", collection);
			this.createCollectionHistory(newCollectionEntity, admin, DataBaseActionType.REACTIVATE);
			CollectionServiceImplementation.log.info("Finished create the reactivate history of id {}.", collection);

			return this.collectionHelper.convertCollection(newCollectionEntity);
		} catch (final Exception exception) {
			CollectionServiceImplementation.log.error("The collection with the id {} could not been reactivate.", collection, exception);
			throw new DataBaseException(Controller.CATALOG_COLLECTION, DataBaseActionType.REACTIVATE, collection.toString());
		}
	}

	@Override
	public CollectionResponse deleteCollection(final UUID collection, final UUID admin) throws DataBaseException, NotFoundException {
		CollectionServiceImplementation.log.info("Starting deleted the collection with the id {}.", collection);
		final var collectionEntity = this.collectionHelper.getCollectionEntity(collection);
		collectionEntity.setActive(false);
		try {
			final var newCollectionEntity = this.collectionRepository.save(collectionEntity);
			CollectionServiceImplementation.log.info("Finished delete the collection with the id {}.", collection);

			CollectionServiceImplementation.log.info("Starting created the delete history of id {}.", collection);
			this.createCollectionHistory(newCollectionEntity, admin, DataBaseActionType.DELETE);
			CollectionServiceImplementation.log.info("Finished create the delete history of id {}.", collection);

			return this.collectionHelper.convertCollection(newCollectionEntity);
		} catch (final Exception exception) {
			CollectionServiceImplementation.log.error("The collection with the id {} could not been delete.", collection, exception);
			throw new DataBaseException(Controller.CATALOG_COLLECTION, DataBaseActionType.DELETE, collection.toString());
		}
	}

	@Override
	public GetResponse<CollectionResponse> getAllCollection(final FilterRequest filterRequest, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		CollectionServiceImplementation.log.info("Starting searched of all collections.");
		Page<CollectionEntity> pageCollectionEntity = null;
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
			pageCollectionEntity = this.collectionRepository.findAll(search, active, direction, pageable);
		} catch (final Exception exception) {
			CollectionServiceImplementation.log.error("The collections could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_COLLECTION, DataBaseActionType.READ);
		}
		final var listCollectionResponse = pageCollectionEntity.get().map(this.collectionHelper::convertCollection).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageCollectionEntity);
		if (listCollectionResponse.isEmpty()) {
			//Todo revisar ERROR
			//CollectionServiceImplementation.log.error("The clotections not found."+listCollectionResponse.stream().iterator().next().getName());
			CollectionServiceImplementation.log.error("The collections not found.");
			throw new NotFoundException(Controller.CATALOG_COLLECTION, "all");

		}
		CollectionServiceImplementation.log.info("Finished search the collections.");
		return new GetResponse<>(listCollectionResponse, paginationResponse);
	}

	@Override
	public GetResponse<CollectionHistoryResponse> getCollectionHistory(final UUID collection, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
		CollectionServiceImplementation.log.info("Starting searched history of collection with the id {}.", collection);
		final var collectionEntity = this.collectionHelper.getCollectionEntity(collection);
		Page<CollectionHistoryEntity> pageCollectionHistoryEntity = null;
		try {
			final var pageable = this.paginationHelper.getPageable(paginationRequest);
			pageCollectionHistoryEntity = this.collectionHistoryRepository.findAllByCollectionEntity(collectionEntity, pageable);
		} catch (final Exception exception) {
			CollectionServiceImplementation.log.error("The history collection could not been read.", exception);
			throw new DataBaseException(Controller.CATALOG_COLLECTION, DataBaseActionType.READ);
		}
		final var listCollectionHistoryResponse = pageCollectionHistoryEntity.get().map(this.collectionHelper::convertCollectionHistory).collect(Collectors.toList());
		final var paginationResponse = this.paginationHelper.getPaginationResponse(pageCollectionHistoryEntity);
		CollectionServiceImplementation.log.info("Finished search history of collection with the id {}.", collection);
		return new GetResponse<>(listCollectionHistoryResponse, paginationResponse);
	}

	private void validateCollectionNotExist(final String name) throws ExistException {
		CollectionServiceImplementation.log.info("Starting validate the collection if exist {}.", name);
		final var optionalCollectionEntity = this.collectionRepository.findByNameIgnoreCase(name);
		if (optionalCollectionEntity.isPresent()) {
			CollectionServiceImplementation.log.error("The collection {} exist.", name);
			throw new ExistException(Controller.CATALOG_COLLECTION, "name", name);
		}
	}

	private void createCollectionHistory(final CollectionEntity collectionEntity, final UUID admin, final DataBaseActionType dataBaseActionType) {
		try {
			final var collectionHistoryEntity = new CollectionHistoryEntity();
			final var adminEntity = this.adminHelper.getAdminEntity(admin);
			final var objectMapper = new ObjectMapper();
			final var object = objectMapper.writeValueAsString(collectionEntity);
			collectionHistoryEntity.setCollectionEntity(collectionEntity);
			collectionHistoryEntity.setAdminEntity(adminEntity);
			collectionHistoryEntity.setActionType(dataBaseActionType);
			collectionHistoryEntity.setDate(new Date());
			collectionHistoryEntity.setObject(object);
			this.collectionHistoryRepository.save(collectionHistoryEntity);
		} catch (final Exception exception) {
			exception.printStackTrace();
		}
	}

}
