package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.BoxEntity;

public interface BoxRepository extends PagingAndSortingRepository<BoxEntity, UUID> {

	@Query(value = "SELECT * FROM get_box(?1, ?2, ?3) ORDER BY active DESC", countQuery = "SELECT count(*) FROM get_box(?1, ?2, ?3)", nativeQuery = true)
	Page<BoxEntity> findAll(String search, Boolean active, String direction, Pageable pageable);

	Optional<BoxEntity> findByNameIgnoreCase(String name);

	Optional<BoxEntity> findByColorCodeIgnoreCase(String colorCode);

	Optional<BoxEntity> findByWidthAndHeightAndDepth(BigDecimal width, BigDecimal height, BigDecimal depth);
}
