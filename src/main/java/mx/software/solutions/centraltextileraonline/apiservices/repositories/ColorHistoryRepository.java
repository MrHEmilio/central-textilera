package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.ColorEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ColorHistoryEntity;

public interface ColorHistoryRepository extends PagingAndSortingRepository<ColorHistoryEntity, UUID> {

	Page<ColorHistoryEntity> findAllByColorEntity(ColorEntity colorEntity, Pageable pageable);

}
