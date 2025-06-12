package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "\"Permission\"")
public class PermissionEntity {

	@Id
	private UUID id;

	@Column(name = "\"key\"")
	private String key;

}
