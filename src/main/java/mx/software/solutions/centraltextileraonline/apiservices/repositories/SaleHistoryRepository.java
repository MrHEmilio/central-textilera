package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.SaleEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.SaleHistoryEntity;

public interface SaleHistoryRepository extends PagingAndSortingRepository<SaleHistoryEntity, UUID> {

	Page<SaleHistoryEntity> findAllBySaleEntity(SaleEntity saleEntity, Pageable pageable);

}
