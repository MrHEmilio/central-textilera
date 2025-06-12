package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.FreighterEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.FreighterHistoryEntity;

public interface FreighterHistoryRepository extends PagingAndSortingRepository<FreighterHistoryEntity, UUID> {

	Page<FreighterHistoryEntity> findAllByFreighterEntity(FreighterEntity freighterEntity, Pageable pageable);

}
