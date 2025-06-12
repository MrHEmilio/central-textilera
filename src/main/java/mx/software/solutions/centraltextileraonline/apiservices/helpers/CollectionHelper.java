package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CollectionHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CollectionResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CollectionEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CollectionHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.CollectionRepository;

@Slf4j
@Component
public class CollectionHelper {

	@Autowired
	private CollectionRepository collectionRepository;

	@Autowired
	private AdminHelper adminHelper;

	@Autowired
	private ImagesHelper imagesHelper;

	@Value("${url.images.collections}")
	private String urlImagesCollections;

	public CollectionEntity getCollectionEntity(final UUID collection) throws DataBaseException, NotFoundException {
		Optional<CollectionEntity> optionalCollectionEntity = Optional.empty();
		try {
			CollectionHelper.log.info("Starting searched the collection with the id {}.", collection);
			optionalCollectionEntity = this.collectionRepository.findById(collection);
		} catch (final Exception exception) {
			CollectionHelper.log.error("The collection with the id {} could not read.", collection, exception);
			throw new DataBaseException(Controller.CATALOG_COLLECTION, DataBaseActionType.READ, collection.toString());
		}

		if (optionalCollectionEntity.isEmpty()) {
			CollectionHelper.log.error("The collection not found with the id {}.", collection);
			throw new NotFoundException(Controller.CATALOG_COLLECTION, "id", collection.toString());
		}
		CollectionHelper.log.info("Finished search the collection with the id {}.", collection);
		return optionalCollectionEntity.get();
	}

	public List<CollectionEntity> getCollectionEntities(final List<UUID> collections) {
		return collections.stream().map(collection -> {
			try {
				return this.getCollectionEntity(collection);
			} catch (DataBaseException | NotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}).collect(Collectors.toList());
	}

	public CollectionResponse convertCollection(final CollectionEntity collectionEntity) {
		final var id = collectionEntity.getId();
		final var name = collectionEntity.getName();
		final var image = this.imagesHelper.getUrlImage(this.urlImagesCollections, collectionEntity.getId());
		final var isActive = collectionEntity.isActive();
		return new CollectionResponse(id, name, image, isActive);
	}

	public CollectionHistoryResponse convertCollectionHistory(final CollectionHistoryEntity collectionHistoryEntity) {
		final var id = collectionHistoryEntity.getId();
		final var collectionResponse = this.convertCollection(collectionHistoryEntity.getCollectionEntity());
		final var adminResponse = this.adminHelper.convertAdmin(collectionHistoryEntity.getAdminEntity());
		final var actionType = collectionHistoryEntity.getActionType();
		final var date = collectionHistoryEntity.getDate();
		final var object = collectionHistoryEntity.getObject();
		return new CollectionHistoryResponse(id, collectionResponse, adminResponse, actionType, date, object);
	}

}
