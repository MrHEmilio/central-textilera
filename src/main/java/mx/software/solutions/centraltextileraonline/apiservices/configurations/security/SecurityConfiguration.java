package mx.software.solutions.centraltextileraonline.apiservices.configurations.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import mx.software.solutions.centraltextileraonline.apiservices.repositories.AuthorityRepository;

@Configuration
public class SecurityConfiguration {

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	@Bean
	AuthenticationManager autheManager(final HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).authenticationProvider(this.authenticationProvider).build();
	}

	@Bean
	SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
		this.setAllAuthorities(httpSecurity);
		this.setLoginForm(httpSecurity);
		httpSecurity.cors();
		httpSecurity.csrf().disable();
		httpSecurity.authorizeRequests(request -> request.antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml","/payment/notification").permitAll());
		httpSecurity.authorizeRequests(requests -> requests.anyRequest().denyAll());
		httpSecurity.httpBasic();
		httpSecurity.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
		return httpSecurity.build();
	}

	private void setAllAuthorities(final HttpSecurity httpSecurity) {
		final var listAuthorityEntities = this.authorityRepository.findAll();
		listAuthorityEntities.forEach(authorityEntity -> {
			try {
				httpSecurity.authorizeRequests(request -> request.antMatchers(authorityEntity.getHttpMethod(), authorityEntity.getPath()).hasAuthority(authorityEntity.getName()));
				if (authorityEntity.isPermitAnonymous())
					httpSecurity.authorizeRequests(request -> request.antMatchers(authorityEntity.getHttpMethod(), authorityEntity.getPath()).permitAll());
			} catch (final Exception exception) {
				exception.printStackTrace();
			}
		});
	}

	private void setLoginForm(final HttpSecurity httpSecurity) throws Exception {
		httpSecurity.formLogin().successHandler(this.authenticationSuccessHandler).failureHandler(this.authenticationFailureHandler);
	}

}
