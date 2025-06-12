package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.FreighterEntity;

public interface FreighterRepository extends PagingAndSortingRepository<FreighterEntity, UUID> {

	@Query(value = "SELECT * FROM get_freighter(?1, ?2, ?3)", countQuery = "SELECT count(*) FROM get_freighter(?1, ?2, ?3)", nativeQuery = true)
	Page<FreighterEntity> findAll(String search, Boolean active, String direction, Pageable pageable);

	Optional<FreighterEntity> findByNameIgnoreCase(String name);

	List<FreighterEntity> findAllByActiveTrue();

}
