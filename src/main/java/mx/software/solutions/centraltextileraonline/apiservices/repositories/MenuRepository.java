package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.MenuEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.PermissionEntity;

public interface MenuRepository extends CrudRepository<MenuEntity, UUID> {

	List<MenuEntity> findByPermissionEntityInAndActiveTrueOrderByOrder(List<PermissionEntity> permissionEntities);

}
