package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.AuthorityEntity;

public interface AuthorityRepository extends CrudRepository<AuthorityEntity, UUID> {

}
