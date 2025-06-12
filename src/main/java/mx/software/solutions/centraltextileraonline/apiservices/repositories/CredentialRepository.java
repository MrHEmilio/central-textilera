package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.CredentialEntity;

public interface CredentialRepository extends CrudRepository<CredentialEntity, UUID> {

	Optional<CredentialEntity> findByEmailIgnoreCase(String email);

}
