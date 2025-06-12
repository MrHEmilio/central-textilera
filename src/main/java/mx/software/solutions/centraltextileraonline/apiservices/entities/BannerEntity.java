package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "\"Banner\"")
public class BannerEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"description\"")
	private String description;

	@Column(name = "\"order\"")
	private int order;

	@Column(name = "\"wait_time\"")
	private int waitTime;

	@Column(name = "\"active\"")
	private boolean active;

}
