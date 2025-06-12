package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.AdminEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CredentialEntity;

public interface AdminRepository extends PagingAndSortingRepository<AdminEntity, UUID> {

	@Query(value = "SELECT * FROM get_admin(?1, ?2)", countQuery = "SELECT count(*) FROM get_admin(?1, ?2)", nativeQuery = true)
	Page<AdminEntity> findAll(String search, String direction, Pageable pageable);

	Optional<AdminEntity> findByCredentialEntity(CredentialEntity credentialEntity);

}
