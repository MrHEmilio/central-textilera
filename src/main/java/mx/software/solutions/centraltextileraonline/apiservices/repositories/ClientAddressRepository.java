package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.ClientAddressEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClientEntity;

public interface ClientAddressRepository extends PagingAndSortingRepository<ClientAddressEntity, UUID> {

	Page<ClientAddressEntity> findByClientEntity(ClientEntity client, Pageable pageable);

	Optional<ClientAddressEntity> findByClientEntityAndNameAndActiveTrue(ClientEntity client, String name);

	Page<ClientAddressEntity> findByClientEntityAndActiveTrue(ClientEntity client, Pageable pageable);

	Optional<ClientAddressEntity> findByClientEntityAndPredeterminedTrue(ClientEntity client);

	Optional<ClientAddressEntity> findByClientEntityAndBillingAddressTrue(ClientEntity client);

}
