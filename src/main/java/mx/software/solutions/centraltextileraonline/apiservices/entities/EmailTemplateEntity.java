package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.EmailTemplate;

@Data
@Entity
@Table(name = "\"EmailTemplate\"")
public class EmailTemplateEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"name\"")
	@Enumerated(EnumType.STRING)
	private EmailTemplate name;

	@Column(name = "\"subject\"")
	private String subject;

	@Column(name = "\"content\"")
	private String content;

}
