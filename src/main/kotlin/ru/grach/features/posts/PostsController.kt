package ru.grach.features.posts

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.grach.database.posts.Posts
import ru.grach.database.posts.mapToCreatePostResponse
import ru.grach.database.posts.mapToPostDTO
import ru.grach.database.posts.mapToPostResponse
import ru.grach.features.posts.models.CreatePostRequest
import ru.grach.features.posts.models.FetchPostsRequest
import ru.grach.features.posts.models.FetchPostsResponse
import ru.grach.utils.TokenCheck

class PostsController (private val call: ApplicationCall) {
    suspend fun performSearch(){
        val request = call.receive<FetchPostsRequest>()
        val token = call.request.headers["Bearer-Authorization"]

        if(TokenCheck.isTokenValid(token.orEmpty())||TokenCheck.isTokenAdmin(token.orEmpty())){
            call.respond(FetchPostsResponse(
                posts = Posts.fetchPosts().filter {it.authorId.contains(request.searchQuery, ignoreCase = true)}
                    .map{it.mapToPostResponse()}
            ))
        }
        else{
            call.respond(HttpStatusCode.Unauthorized, "Token expired")
        }
    }

    suspend fun createGame(){
        val token = call.request.headers["Bearer-Authorization"]
        if(TokenCheck.isTokenAdmin(token.orEmpty())){
            val request = call.receive<CreatePostRequest>()
            val game = request.mapToPostDTO()
            Posts.insert(game)
            call.respond(game.mapToCreatePostResponse())
        }
        else{
            call.respond(HttpStatusCode.Unauthorized, "Token expired")

        }
    }

}