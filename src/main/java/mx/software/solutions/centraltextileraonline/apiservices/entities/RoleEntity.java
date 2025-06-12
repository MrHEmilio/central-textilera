package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "\"Role\"")
public class RoleEntity {

	@Id
	private UUID id;

	@Column(name = "\"key\"")
	private String key;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "\"Role_Permission\"", joinColumns = @JoinColumn(name = "role"), inverseJoinColumns = @JoinColumn(name = "permission"))
	Set<PermissionEntity> permissionEntities;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "\"Role_Authority\"", joinColumns = @JoinColumn(name = "role"), inverseJoinColumns = @JoinColumn(name = "authority"))
	Set<AuthorityEntity> authorityEntities;
}
