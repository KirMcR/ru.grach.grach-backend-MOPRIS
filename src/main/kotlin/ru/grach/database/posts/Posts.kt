package ru.grach.database.posts

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.grach.database.tokens.TokenDTO
import ru.grach.database.tokens.Tokens
import ru.grach.database.users.UserDTO
import ru.grach.database.users.Users

object Posts : Table() {

    private val id = Tokens.varchar("id", 51)
    private val authorId = Tokens.varchar("author_id", 25)
    private val post = Tokens.varchar("post", 355)
    private val date = Tokens.varchar("date", 21)

    fun insert(postDTO: PostDTO) {
        transaction {
            insert {
                it[id] = postDTO.id
                it[authorId] = postDTO.authorId
                it[post] = postDTO.post
                it[date] = postDTO.date
            }
        }
    }

    fun fetchPosts(): List<PostDTO> {
        return try {
            transaction {
                Posts.selectAll().toList()
                    .map {
                        PostDTO(
                            id = it[Posts.id],
                            authorId = it[authorId],
                            post = it[post],
                            date = it[date]
                        )
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    fun fetchPostByAuthorId(authorId: String): PostDTO? {
        return try {
            transaction {
                val postModel = Posts.select { Posts.authorId.eq(authorId) }.single()
                PostDTO(
                    id = postModel[Posts.id],
                    authorId = postModel[Posts.authorId],
                    post = postModel[Posts.post],
                    date = postModel[Posts.date]
                )
            }

        } catch (e: Exception) {
            null
        }
    }
}