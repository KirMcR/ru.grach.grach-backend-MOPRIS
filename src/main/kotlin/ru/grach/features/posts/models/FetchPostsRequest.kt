package ru.grach.features.posts.models

import kotlinx.serialization.Serializable

@Serializable
data class FetchPostsRequest(
    val searchQuery: String
)

@Serializable
data class FetchPostsResponse(
    val posts: List<PostResponse>
)

@Serializable
data class PostResponse(
    val id: String,
    val authorId: String,
    val post: String,
    val date:String
)