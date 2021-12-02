package org.digit.socnetworkrest

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDateTime

@Table("Profile")
data class Profile(
    @field:Id @field:Column("owner_name")
    val ownerName: String,
    @field:Column("nickname")
    var nickname: String,
    @field:Column("status_text")
    var statusText: String?,
) : Persistable<String> {

    @Transient
    var isNewlyCreated: Boolean = false

    override fun isNew() = isNewlyCreated

    override fun getId() = ownerName
}

@Table("Post")
data class Post(
    @field:Id @field:Column("id") val postId: String,
    @field:Column("profile_id") val profileId: String,
    @field:Column("image") var image: String? = null,
    @field:Transient val isNewlyCreated: Boolean = true
) : Persistable<String> {

    @Column("created")
    @CreatedDate
    var created: LocalDateTime = LocalDateTime.now()

    @Column("title")
    lateinit var title: String

    @Column("post_text")
    lateinit var text: String

    override fun isNew() = isNewlyCreated

    override fun getId() = postId
}

@Repository
interface ProfileRepository : CrudRepository<Profile, String> {

    fun findProfileByOwnerName(ownerName: String): Profile?
}

data class PostWithAuthor(
    val postId: String,
    var image: String?,
    var created: LocalDateTime,
    val title: String,
    val text: String,
    val nickname: String,
    val profileId: String
)

class PostWithAuthorMapper : RowMapper<PostWithAuthor> {

    override fun mapRow(rs: ResultSet, rowNum: Int) = PostWithAuthor(
        rs.getString("post.id"),
        rs.getNString("post.image"),
        rs.getTimestamp("post.created").toLocalDateTime(),
        rs.getString("post.title"),
        rs.getString("post.post_text"),
        rs.getString("profile.nickname"),
        rs.getString("profile.owner_name")
    )
}

@Repository
interface PostRepository : CrudRepository<Post, String> {

    @Query(
        "SELECT \"post\".*, \"profile\".\"nickname\", \"profile\".\"owner_name\" FROM \"Post\" \"post\" JOIN " +
                "\"Profile\" \"profile\" ON \"post\".\"profile_id\" = \"profile\".\"owner_name\" WHERE \"post\".\"created\"" +
                " > DATEADD(day,-1, :time)",
        rowMapperClass = PostWithAuthorMapper::class
    )
    fun selectPosts(@Param("time") time: LocalDateTime): List<PostWithAuthor>
}