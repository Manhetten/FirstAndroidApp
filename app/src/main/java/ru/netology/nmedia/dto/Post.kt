package ru.netology.nmedia.dto

import java.net.URI
import java.net.URL

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likeCounter: Int = 0,
    val shareCounter: Int = 0,
    val video: String? = null
)