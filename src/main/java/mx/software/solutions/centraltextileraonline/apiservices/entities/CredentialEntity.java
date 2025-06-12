package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "\"Credential\"")
public class CredentialEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"email\"")
	private String email;

	@Column(name = "\"password\"")
	private String password;

	@Column(name = "\"active\"")
	private boolean active;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "\"role\"")
	private RoleEntity roleEntity;

	@Column(name = "\"date\"")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "\"Permission_Credential\"", joinColumns = @JoinColumn(name = "credential"), inverseJoinColumns = @JoinColumn(name = "permission"))
	private Set<PermissionEntity> permissionEntities;

}
