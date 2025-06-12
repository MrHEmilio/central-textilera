package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.BoxEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.BoxHistoryEntity;

public interface BoxHistoryRepository extends PagingAndSortingRepository<BoxHistoryEntity, UUID> {

	Page<BoxHistoryEntity> findAllByBoxEntity(BoxEntity boxEntity, Pageable pageable);

}
