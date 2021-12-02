package org.digit.socnetworkrest.mappers

import org.digit.socnetworkrest.Profile
import org.digit.socnetworkrest.api.dto.ProfileDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface ProfileMapper {

    @Mapping(source = "statusText", target = "status")
    fun toDto(entity: Profile): ProfileDto
}