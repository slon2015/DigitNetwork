package org.digit.socnetworkrest.services

import org.digit.socnetworkrest.Profile
import org.digit.socnetworkrest.ProfileRepository
import org.digit.socnetworkrest.api.dto.CreateProfileDto
import org.digit.socnetworkrest.api.dto.ProfileDto
import org.digit.socnetworkrest.api.dto.UpdateProfileDto
import org.digit.socnetworkrest.mappers.ProfileMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.security.Principal

@Service
class ProfileService(
    val keycloakService: KeycloakService,
    val repository: ProfileRepository,
    val mapper: ProfileMapper
) {

    @Transactional
    fun register(dto: CreateProfileDto): ProfileDto {
        val userId = keycloakService.registerUser(dto)

        var profile = Profile(
            userId,
            dto.nickname,
            null
        )

        profile.isNewlyCreated = true

        profile = repository.save(profile)
        return mapper.toDto(profile)
    }

    private fun getProfile(securityId: UserId) =
        (repository.findProfileByOwnerName(securityId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND))

    fun update(principal: Principal, dto: UpdateProfileDto): ProfileDto {
        val securityId = keycloakService.extractUserId(principal)

        val profile = getProfile(securityId)

        if (dto.nickname != null) {
            profile.nickname = dto.nickname
        }
        if (dto.status != null) {
            profile.statusText = dto.status
        }

        return mapper.toDto(repository.save(profile))

    }


    fun get(principal: Principal): ProfileDto {
        val profile = getProfile(keycloakService.extractUserId(principal))

        return mapper.toDto(profile)
    }

    fun get(id: UserId): ProfileDto {
        val profile = getProfile(id)

        return mapper.toDto(profile)
    }
}