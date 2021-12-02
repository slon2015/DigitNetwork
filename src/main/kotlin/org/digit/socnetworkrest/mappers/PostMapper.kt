package org.digit.socnetworkrest.mappers

import org.digit.socnetworkrest.Post
import org.digit.socnetworkrest.PostWithAuthor
import org.digit.socnetworkrest.Profile
import org.digit.socnetworkrest.api.dto.PostCreateDto
import org.digit.socnetworkrest.api.dto.PostCreationResult
import org.digit.socnetworkrest.api.dto.PostDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget


@Mapper(componentModel = "spring")
interface PostMapper {

    fun toEntity(@MappingTarget entity: Post, dto: PostCreateDto): Post

    @Mapping(source = "postId", target = "id")
    fun toPostCreationResult(entity: Post): PostCreationResult

    @Mapping(source = "postId", target = "id")
    @Mapping(source = "nickname", target = "author.name")
    @Mapping(source = "profileId", target = "author.id")
    fun mapToDto(entity: PostWithAuthor): PostDto
}