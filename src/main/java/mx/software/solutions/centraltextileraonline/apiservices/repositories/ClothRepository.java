package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothEntity;

public interface ClothRepository extends PagingAndSortingRepository<ClothEntity, UUID> {

	@Query(value = "SELECT * FROM get_cloth(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9) ORDER BY active DESC", countQuery = "SELECT count(*) FROM get_cloth(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9)", nativeQuery = true)
	Page<ClothEntity> findAll(String search, String searchUrl, String fibers, String sales, String collections, String uses, Boolean active, String columnSort, String direction, Pageable pageable);

	Optional<ClothEntity> findByNameIgnoreCaseOrNameUrlIgnoreCase(String name, String nameUrl);

}
