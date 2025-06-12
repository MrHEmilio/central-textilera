package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.ColorEntity;

public interface ColorRepository extends PagingAndSortingRepository<ColorEntity, UUID> {

	@Query(value = "SELECT * FROM get_color(?1, ?2, ?3)", countQuery = "SELECT count(*) FROM get_color(?1, ?2, ?3)", nativeQuery = true)
	Page<ColorEntity> findAll(String search, Boolean active, String direction, Pageable pageable);

	Optional<ColorEntity> findByNameIgnoreCase(String name);

	Optional<ColorEntity> findByCodeIgnoreCase(String code);

}
