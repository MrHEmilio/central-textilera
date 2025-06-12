package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.CollectionEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CollectionHistoryEntity;

public interface CollectionHistoryRepository extends PagingAndSortingRepository<CollectionHistoryEntity, UUID> {

	Page<CollectionHistoryEntity> findAllByCollectionEntity(CollectionEntity collectionEntity, Pageable pageable);

}
