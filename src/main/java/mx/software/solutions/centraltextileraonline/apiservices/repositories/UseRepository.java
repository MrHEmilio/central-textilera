package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.SaleEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.UseEntity;

public interface UseRepository extends PagingAndSortingRepository<UseEntity, UUID> {

	@Query(value = "SELECT * FROM get_use(?1, ?2, ?3)", countQuery = "SELECT count(*) FROM get_use(?1, ?2, ?3)", nativeQuery = true)
	Page<UseEntity> findAll(String search, Boolean active, String direction, Pageable pageable);

	Optional<SaleEntity> findByNameIgnoreCase(String name);

}
