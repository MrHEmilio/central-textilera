package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.http.HttpMethod;

import lombok.Data;

@Data
@Entity
@Table(name = "\"Authority\"")
public class AuthorityEntity {

	@Id
	private UUID id;

	@Column(name = "\"name\"")
	private String name;

	@Column(name = "\"path\"")
	private String path;

	@Column(name = "\"httpMethod\"")
	@Enumerated(EnumType.STRING)
	private HttpMethod httpMethod;

	@Column(name = "\"permitAnonymous\"")
	private boolean permitAnonymous;

}
