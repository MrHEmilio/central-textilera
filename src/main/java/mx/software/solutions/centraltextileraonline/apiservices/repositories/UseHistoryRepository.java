package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.UseEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.UseHistoryEntity;

public interface UseHistoryRepository extends PagingAndSortingRepository<UseHistoryEntity, UUID> {

	Page<UseHistoryEntity> findAllByUseEntity(UseEntity useEntity, Pageable pageable);

}
