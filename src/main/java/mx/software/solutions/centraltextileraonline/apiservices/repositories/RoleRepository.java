package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, UUID> {

	RoleEntity findByKey(String key);

}
