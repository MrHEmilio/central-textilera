package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.TokenInfoResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.CredentialEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.EmailTokenEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.EmailTemplate;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.TokenInvalidException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.SecurityHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.EmailTemplateRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.EmailTokenRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.EmailService;
import mx.software.solutions.centraltextileraonline.apiservices.service.TokenService;

@Slf4j
@Service
public class TokenServiceImplementation implements TokenService {

	@Value("${token.send-email.secret-key}")
	private String tokenSecretKey;

	@Autowired
	private SecurityHelper securityHelper;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmailTokenRepository emailTokenRepository;

	@Autowired
	private EmailTemplateRepository emailTemplateRepository;

	@Override
	public String createToken(final String email, final EmailTemplate emailTemplate) {
		TokenServiceImplementation.log.info("Starting created token for email {}", email);
		final var calendarNow = Calendar.getInstance();
		calendarNow.add(Calendar.DATE, 1);
		final var signatureAlgorithm = SignatureAlgorithm.HS512;
		final var tokenSecretKeySecretBytes = DatatypeConverter.parseBase64Binary(this.tokenSecretKey);
		final var secretKey = new SecretKeySpec(tokenSecretKeySecretBytes, signatureAlgorithm.getJcaName());
		final var jwtBuilder = Jwts.builder();
		jwtBuilder.setId(UUID.randomUUID().toString());
		jwtBuilder.setIssuedAt(new Date());
		jwtBuilder.setSubject(emailTemplate.name());
		jwtBuilder.claim("email", email);
		jwtBuilder.setExpiration(calendarNow.getTime());
		jwtBuilder.signWith(signatureAlgorithm, secretKey);
		TokenServiceImplementation.log.info("Finished create token for email {}", email);
		return jwtBuilder.compact();
	}

	@Override
	public void saveEmailToken(final String token, final String emailContent) throws DataBaseException, NotFoundException {
		final var claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(this.tokenSecretKey)).parseClaimsJws(token).getBody();
		final var email = claims.get("email").toString();
		TokenServiceImplementation.log.info("Starting saved token for email {}", email);
		try {
			final var emailTemplate = EmailTemplate.valueOf(claims.getSubject());
			final var credentialEntity = this.securityHelper.getCredentialEntity(email);
			final var emailTemplateEntity = this.emailTemplateRepository.findByName(emailTemplate);
			this.invalidLastToken(credentialEntity);
			final var emailTokenEntity = new EmailTokenEntity();
			emailTokenEntity.setCredentialEntity(credentialEntity);
			emailTokenEntity.setEmailTemplateEntity(emailTemplateEntity);
			emailTokenEntity.setToken(token);
			emailTokenEntity.setEmailContent(emailContent);
			emailTokenEntity.setDate(new Date());
			emailTokenEntity.setActive(true);
			this.emailTokenRepository.save(emailTokenEntity);
			TokenServiceImplementation.log.info("Finished save token for email {}", email);
		} catch (final Exception exception) {
			TokenServiceImplementation.log.info("The email token not found with the token {}", token);
			throw new DataBaseException(Controller.EMAIL_SEND_EMAIL, DataBaseActionType.CREATE);
		}
	}

	@Override
	public TokenInfoResponse validateToken(final String token) throws DataBaseException, NotFoundException, TokenInvalidException {
		Optional<EmailTokenEntity> optionalEmailTokenEntity = Optional.empty();
		try {
			TokenServiceImplementation.log.info("Starting searched the email token with the token {}.", token);
			optionalEmailTokenEntity = this.emailTokenRepository.findByToken(token);
		} catch (final Exception exception) {
			TokenServiceImplementation.log.info("The email token {} could not read", token);
			throw new DataBaseException(Controller.EMAIL_SEND_EMAIL, DataBaseActionType.READ, token);
		}
		if (optionalEmailTokenEntity.isEmpty()) {
			TokenServiceImplementation.log.info("The email token not found with token {}", token);
			throw new NotFoundException(Controller.EMAIL_SEND_EMAIL, "token", token);
		}

		TokenServiceImplementation.log.info("Finished search the email token with the token {}.", token);
		final var emailTokenEntity = optionalEmailTokenEntity.get();

		Claims claims = null;
		try {
			claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(this.tokenSecretKey)).parseClaimsJws(token).getBody();
		} catch (final ExpiredJwtException expiredJwtException) {
			TokenServiceImplementation.log.info("The email token {} has expired.", token);
			this.resendEmail(emailTokenEntity);
			throw new TokenInvalidException(token);
		}

		if (!emailTokenEntity.isActive()) {
			TokenServiceImplementation.log.info("The email token {} is not active.", token);
			this.resendEmail(emailTokenEntity);
			throw new TokenInvalidException(token);
		}

		return new TokenInfoResponse(claims.get("email").toString());
	}

	@Override
	public void invalidLastToken(final CredentialEntity credentialEntity) throws DataBaseException {
		try {
			final var optionalEmailTokenEntity = this.emailTokenRepository.findByCredentialEntityAndActiveTrue(credentialEntity);
			if (optionalEmailTokenEntity.isEmpty())
				return;

			final var emailTokenEntity = optionalEmailTokenEntity.get();
			emailTokenEntity.setActive(false);
			this.emailTokenRepository.save(emailTokenEntity);
		} catch (final Exception exception) {
			throw new DataBaseException(Controller.EMAIL_SEND_EMAIL, DataBaseActionType.CREATE);
		}
	}

	private void resendEmail(final EmailTokenEntity emailTokenEntity) {
		try {
			final var credentialEntity = emailTokenEntity.getCredentialEntity();
			final var emailTemplateEntity = emailTokenEntity.getEmailTemplateEntity();
			final var newToken = this.createToken(credentialEntity.getEmail(), emailTemplateEntity.getName());
			TokenServiceImplementation.log.info("Starting resend the token again for email.", credentialEntity.getEmail());
			var content = emailTokenEntity.getEmailContent();
			content = content.replace(emailTokenEntity.getToken(), newToken);
			this.saveEmailToken(newToken, content);
			this.emailService.sendEmail(credentialEntity.getEmail(), emailTemplateEntity.getSubject(), content);
			TokenServiceImplementation.log.info("Finished resend the token again for email.", credentialEntity.getEmail());
		} catch (DataBaseException | NotFoundException exception) {
			exception.printStackTrace();
		}
	}

}
