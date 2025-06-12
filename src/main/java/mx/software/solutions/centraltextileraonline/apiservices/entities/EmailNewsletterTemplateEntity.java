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
@Table(name = "\"EmailNewsletterTemplate\"")
public class EmailNewsletterTemplateEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@Column(name = "\"name\"")
	private String name;

	@Column(name = "\"subject\"")
	private String subject;

	@Column(name = "\"content_html\"")
	private String contentHtml;

	@Column(name = "\"content_json\"")
	private String contentJson;

	@Column(name = "\"active\"")
	private boolean active;

}
