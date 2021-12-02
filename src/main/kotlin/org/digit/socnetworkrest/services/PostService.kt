package org.digit.socnetworkrest.services

import org.digit.socnetworkrest.Post
import org.digit.socnetworkrest.PostRepository
import org.digit.socnetworkrest.api.dto.PostCreateDto
import org.digit.socnetworkrest.api.dto.PostCreationResult
import org.digit.socnetworkrest.api.dto.PostDto
import org.digit.socnetworkrest.mappers.PostMapper
import org.springframework.stereotype.Service
import java.security.Principal
import java.time.LocalDateTime
import java.util.*

@Service
class PostService(
    val keycloakService: KeycloakService,
    val postRepository: PostRepository,
    val mapper: PostMapper
) {

    fun create(principal: Principal, dto: PostCreateDto): PostCreationResult {
        val author: UserId = keycloakService.extractUserId(principal)

        val post = Post(
            UUID.randomUUID().toString(),
            author
        )

        mapper.toEntity(post, dto)

        return mapper.toPostCreationResult(
            postRepository.save(post)
        )
    }

    fun getList(): List<PostDto> = postRepository
        .selectPosts(LocalDateTime.now()).map(mapper::mapToDto)
}