package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.SaleEntity;

public interface SaleRepository extends PagingAndSortingRepository<SaleEntity, UUID> {

	@Query(value = "SELECT * FROM get_sale(?1, ?2, ?3)", countQuery = "SELECT count(*) FROM get_sale(?1, ?2, ?3)", nativeQuery = true)
	Page<SaleEntity> findAll(String search, Boolean active, String direction, Pageable pageable);

	Optional<SaleEntity> findByNameIgnoreCase(String name);

}
