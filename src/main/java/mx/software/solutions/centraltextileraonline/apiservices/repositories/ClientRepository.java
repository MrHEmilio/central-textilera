package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.ClientEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CountryCodeEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CredentialEntity;

public interface ClientRepository extends PagingAndSortingRepository<ClientEntity, UUID> {

	@Query(value = "SELECT * FROM get_client(?1, ?2)", countQuery = "SELECT count(*) FROM get_client(?1, ?2)", nativeQuery = true)
	Page<ClientEntity> findAll(String search, String direction, Pageable pageable);

	Optional<ClientEntity> findByCredentialEntity(CredentialEntity credentialEntity);

	Optional<ClientEntity> findByCountryCodeEntityAndPhone(CountryCodeEntity countryCodeEntity, String phone);

	Optional<ClientEntity> findByRfcIgnoreCase(String rfc);

}
