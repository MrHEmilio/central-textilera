package mx.software.solutions.centraltextileraonline.apiservices.entities;

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

import lombok.Data;

@Data
@Entity
@Table(name = "\"Client\"")
public class ClientEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "\"credential\"")
	private CredentialEntity credentialEntity;

	@Column(name = "\"name\"")
	private String name;

	@Column(name = "\"first_lastname\"")
	private String firstLastname;

	@Column(name = "\"second_lastname\"")
	private String secondLastname;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"country_code\"")
	private CountryCodeEntity countryCodeEntity;

	@Column(name = "\"phone\"")
	private String phone;

	@Column(name = "\"email_validated\"")
	private boolean emailValidated;

	@Column(name = "\"rfc\"")
	private String rfc;

	@Column(name = "\"company_name\"")
	private String companyName;

	@Column(name = "\"fiscal_regimen\"")
	private String fiscalRegimen;

}
