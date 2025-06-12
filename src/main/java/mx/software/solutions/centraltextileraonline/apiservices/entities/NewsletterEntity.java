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
@Table(name = "\"Newsletter\"")
public class NewsletterEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"email\"")
	private String email;

	@Column(name = "\"active\"")
	private boolean active;

}
