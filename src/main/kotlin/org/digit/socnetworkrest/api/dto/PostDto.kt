package org.digit.socnetworkrest.api.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.digit.socnetworkrest.services.UserId
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class PostCreateDto(
    @NotBlank
    val title: String,
    @NotBlank
    val text: String,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val image: String?
)

data class PostCreationResult(
    val id: String
)

data class AuthorRef(
    val name: String,
    val id: UserId
)

data class PostDto(
    val author: AuthorRef,
    val title: String,
    val text: String,
    val image: String?,
    val id: String,
    val created: LocalDateTime
)

