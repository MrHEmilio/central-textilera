package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothHistoryEntity;

public interface ClothHistoryRepository extends PagingAndSortingRepository<ClothHistoryEntity, UUID> {

	Page<ClothHistoryEntity> findAllByClothEntity(ClothEntity clothEntity, Pageable pageable);

}
