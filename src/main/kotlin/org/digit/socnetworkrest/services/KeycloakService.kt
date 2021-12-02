package org.digit.socnetworkrest.services

import lombok.extern.slf4j.Slf4j
import org.digit.socnetworkrest.api.dto.CreateProfileDto
import org.digit.socnetworkrest.exceptions.InvalidPrincipal
import org.digit.socnetworkrest.exceptions.UsernameAlreadyExists
import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.security.Principal
import javax.ws.rs.WebApplicationException

typealias UserId = String

@Service
@Slf4j
class KeycloakService(
    val adminClient: Keycloak,
    @Value("\${keycloak.realm}") val realm: String
) {

    fun registerUser(dto: CreateProfileDto): UserId {

        val user = UserRepresentation()
        user.isEnabled = true
        user.username = dto.nickname

        val password = CredentialRepresentation()
        password.isTemporary = false
        password.type = CredentialRepresentation.PASSWORD
        password.value = dto.password

        user.credentials = listOf(password)

        try {
            return CreatedResponseUtil.getCreatedId(
                adminClient.realm(realm).users().create(user)
            )
        } catch (e: WebApplicationException) {
            if (e.response.status == HttpStatus.CONFLICT.value()) {
                throw UsernameAlreadyExists()
            } else {
                throw e
            }
        }
    }

    fun extractUserId(principal: Principal): UserId {
        if (principal is KeycloakAuthenticationToken) {
            return (principal.principal as KeycloakPrincipal<*>).name
        } else {
            throw InvalidPrincipal()
        }
    }
}