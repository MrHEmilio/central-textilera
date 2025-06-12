package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.util.Date;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "\"EmailToken\"")
public class EmailTokenEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "\"credential\"")
	private CredentialEntity credentialEntity;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "\"email_template\"")
	private EmailTemplateEntity emailTemplateEntity;

	@Column(name = "\"token\"")
	private String token;

	@Column(name = "\"email_content\"")
	private String emailContent;

	@Column(name = "\"date\"")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name = "\"active\"")
	private boolean active;

}
