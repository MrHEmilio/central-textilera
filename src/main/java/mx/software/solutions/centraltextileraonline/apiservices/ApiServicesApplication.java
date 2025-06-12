package mx.software.solutions.centraltextileraonline.apiservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Central Textilera API", version = "4.0"))
@EnableWebSecurity
@EnableRedisHttpSession
public class ApiServicesApplication {
	public static void main(final String[] args) {
		SpringApplication.run(ApiServicesApplication.class, args);
	}
}
