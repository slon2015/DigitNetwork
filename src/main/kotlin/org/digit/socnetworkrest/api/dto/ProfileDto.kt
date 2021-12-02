package org.digit.socnetworkrest.api.dto

import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class CreateProfileDto(
    @field:NotBlank
    val nickname: String,
    @field:NotBlank
    val password: String
)

data class ProfileDto(
    val ownerName: String,
    val nickname: String,
    val status: String?
)

data class UpdateProfileDto(
    @field:Size(message = "Длинна никнейма от 4 до 10 символов", min = 4, max = 10)
    @field:JsonInclude(value=JsonInclude.Include.NON_EMPTY)
    val nickname: String?,
    @field:Size(message = "Длинна статуса до 50 символов", max = 50)
    @field:JsonInclude(value=JsonInclude.Include.NON_EMPTY)
    val status: String?
)