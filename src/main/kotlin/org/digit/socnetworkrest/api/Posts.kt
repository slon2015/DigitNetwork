package org.digit.socnetworkrest.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.Authorization
import org.digit.socnetworkrest.api.dto.PostCreateDto
import org.digit.socnetworkrest.api.dto.PostCreationResult
import org.digit.socnetworkrest.api.dto.PostDto
import org.digit.socnetworkrest.configs.SwaggerConfig
import org.digit.socnetworkrest.services.PostService
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.validation.Valid

@Api(tags = [SwaggerConfig.FEEDS_TAG])
interface PostsContract {

    @ApiOperation(
        "Создать пост", authorizations =
        [Authorization(SwaggerConfig.SECURITY_SCHEME)]
    )
    fun create(
        @ApiParam(value = "Параметры поста") dto: PostCreateDto,
        principal: Principal
    ): PostCreationResult

    @ApiOperation("Получить информацию о посте")
    fun get(): List<PostDto>
}

@RestController
@RequestMapping("/feed")
class Posts(
    val postService: PostService
) : PostsContract {

    @PostMapping
    override fun create(
        @RequestBody @Valid dto: PostCreateDto,
        principal: Principal
    ): PostCreationResult = postService.create(principal, dto)

    @GetMapping
    override fun get(): List<PostDto> = postService.getList()
}