package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.MenuResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CredentialEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.PermissionEntity;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.MenuRepository;

@Slf4j
@Component
public class MenuHelper {

	@Autowired
	private MenuRepository menuRepository;

	public List<MenuResponse> getMenuResponse(final CredentialEntity credentialEntity, final List<PermissionEntity> listPermissionEntity) {
		MenuHelper.log.info("Starting search the menu with the credential id {}.", credentialEntity.getId());
		final var listMenuEntities = this.menuRepository.findByPermissionEntityInAndActiveTrueOrderByOrder(listPermissionEntity);
		MenuHelper.log.info("Finished search the menu with the credential id {} with menu size {}.", credentialEntity.getId(), listMenuEntities.size());
		return listMenuEntities.stream().map(menuEntity -> new MenuResponse(menuEntity.getName(), menuEntity.getIcon(), menuEntity.getPath())).collect(Collectors.toList());
	}

}
