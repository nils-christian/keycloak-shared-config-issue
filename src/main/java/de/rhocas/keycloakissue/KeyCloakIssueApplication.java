package de.rhocas.keycloakissue;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;

import de.rhocas.keycloakissue.firstcontext.FirstConfiguration;
import de.rhocas.keycloakissue.secondcontext.SecondConfiguration;

@Configuration
public class KeyCloakIssueApplication {

	public static void main( final String[] args ) {
		new SpringApplicationBuilder( )
				.sources( KeyCloakIssueApplication.class )

				.child( FirstConfiguration.class )
				.web( WebApplicationType.SERVLET )

				.sibling( SecondConfiguration.class )
				.web( WebApplicationType.SERVLET )

				.run( args );
	}

}
