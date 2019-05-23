package de.rhocas.keycloakissue.shared;

import org.keycloak.adapters.springboot.KeycloakAutoConfiguration;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@ComponentScan( basePackageClasses = KeycloakSecurityComponents.class, excludeFilters = {
		@ComponentScan.Filter( type = FilterType.REGEX, pattern = "org.keycloak.adapters.springsecurity.management.HttpSessionManager" )
} )
@Import( {
		WebMvcAutoConfiguration.class,
		ServletWebServerFactoryAutoConfiguration.class,
		EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
		DispatcherServletAutoConfiguration.class,
		MultipartAutoConfiguration.class,
		KeycloakAutoConfiguration.class
} )
@EnableWebSecurity
public class SharedConfiguration extends KeycloakWebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobal( final AuthenticationManagerBuilder auth ) {
		final KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider( );
		keycloakAuthenticationProvider.setGrantedAuthoritiesMapper( new SimpleAuthorityMapper( ) );
		auth.authenticationProvider( keycloakAuthenticationProvider );
	}

	@Bean
	public KeycloakSpringBootConfigResolver KeycloakConfigResolver( ) {
		return new KeycloakSpringBootConfigResolver( );
	}

	@Bean
	@Override
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy( ) {
		return new RegisterSessionAuthenticationStrategy( new SessionRegistryImpl( ) );
	}

	@Override
	protected void configure( final HttpSecurity http ) throws Exception {
		super.configure( http );
		http.authorizeRequests( )
				.antMatchers( "/**" )
				.authenticated( );
	}

}
