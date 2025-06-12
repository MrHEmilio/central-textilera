package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.FiberEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.FiberHistoryEntity;

public interface FiberHistoryRepository extends PagingAndSortingRepository<FiberHistoryEntity, UUID> {

	Page<FiberHistoryEntity> findAllByFiberEntity(FiberEntity fiberEntity, Pageable pageable);

}
