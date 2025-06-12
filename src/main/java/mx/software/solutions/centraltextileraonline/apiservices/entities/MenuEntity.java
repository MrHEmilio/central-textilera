package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "\"Menu\"")
public class MenuEntity {

	@Id
	private UUID id;

	@Column(name = "\"name\"")
	private String name;

	@Column(name = "\"icon\"")
	private String icon;

	@Column(name = "\"path\"")
	private String path;

	@Column(name = "\"order\"")
	private int order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"permission\"")
	private PermissionEntity permissionEntity;

	@Column(name = "\"active\"")
	private boolean active;

}
