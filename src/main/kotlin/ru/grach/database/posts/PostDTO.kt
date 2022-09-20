package ru.grach.database.posts

import ru.grach.features.posts.models.CreatePostRequest
import ru.grach.features.posts.models.CreatePostResponse
import ru.grach.features.posts.models.PostResponse
import java.util.*

class PostDTO (
    val id: String,
    val authorId: String,
    val post: String,
    val date:String
)

fun CreatePostRequest.mapToPostDTO(): PostDTO =
    PostDTO(
        id = UUID.randomUUID().toString(),
        authorId = authorId,
        post = post,
        date = date
    )

fun PostDTO.mapToCreatePostResponse(): CreatePostResponse =
    CreatePostResponse(
        id = id,
        authorId = authorId,
        post = post,
        date = date
    )
fun PostDTO.mapToPostResponse(): PostResponse = PostResponse(
    id = id,
    authorId = authorId,
    post = post,
    date = date
)