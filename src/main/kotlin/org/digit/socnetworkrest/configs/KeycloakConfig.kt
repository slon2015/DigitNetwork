package org.digit.socnetworkrest.configs

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.beans.factory.annotation.Value
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.OAuth2Constants

@Configuration
class KeycloakConfig {

    @Bean
    fun keycloakAdmin(
        @Value("\${keycloak.auth-server-url}") url: String,
        @Value("\${app.registrator.clientId}") client: String,
        @Value("\${app.registrator.secret}") secret: String,
        @Value("\${keycloak.realm}") realm: String

    ): Keycloak = KeycloakBuilder
            .builder()
                .serverUrl(url)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(client)
                .clientSecret(secret)
            .build()

}