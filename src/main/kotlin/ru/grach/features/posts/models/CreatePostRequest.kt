package ru.grach.features.posts.models

import kotlinx.serialization.Serializable


@Serializable
data class CreatePostRequest(
    val authorId: String,
    val post: String,
    val date:String
)

@Serializable
data class CreatePostResponse(
    val id: String,
    val authorId: String,
    val post: String,
    val date:String
)