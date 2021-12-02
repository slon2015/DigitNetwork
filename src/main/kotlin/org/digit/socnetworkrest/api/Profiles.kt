package org.digit.socnetworkrest.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.Authorization
import org.digit.socnetworkrest.api.dto.CreateProfileDto
import org.digit.socnetworkrest.api.dto.ProfileDto
import org.digit.socnetworkrest.api.dto.UpdateProfileDto
import org.digit.socnetworkrest.configs.SwaggerConfig
import org.digit.socnetworkrest.services.ProfileService
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@Api(tags = [SwaggerConfig.PROFILES_TAG])
interface ProfilesContract {

    @ApiOperation("Получение данных профиля")
    fun get(
        @ApiParam(value = "Идентификатор профиля") id: String
    ): ProfileDto

    @ApiOperation("Получение данных своего профиля", authorizations =
        [Authorization(SwaggerConfig.SECURITY_SCHEME)]
    )
    fun get(
        principal: Principal
    ): ProfileDto

    @ApiOperation("Изменение данных профиля", authorizations =
        [Authorization(SwaggerConfig.SECURITY_SCHEME)]
    )
    fun update(
        @ApiParam(value = "Новые данные профиля") dto: UpdateProfileDto,
        principal: Principal
    ): ProfileDto

    @ApiOperation("Регистрация нового профиля")
    fun register(
        @ApiParam(value = "Данные профиля") dto: CreateProfileDto
    ): ProfileDto?
}

@RestController
@RequestMapping("/profile")
class Profiles(
    val service: ProfileService
) : ProfilesContract {

    @GetMapping("/self")
    override fun get(
        principal: Principal
    ) = service.get(principal)

    @GetMapping("/public/{id}")
    override fun get(
        @PathVariable id: String
    ) = service.get(id)

    @PutMapping
    override fun update(
        @RequestBody @Valid dto: UpdateProfileDto,
        principal: Principal
    ) = service.update(principal, dto)

    @PostMapping("/register")
    override fun register(
        @Valid @RequestBody dto: CreateProfileDto
    ) = service.register(dto)

}