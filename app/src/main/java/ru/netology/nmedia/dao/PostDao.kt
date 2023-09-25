package ru.netology.nmedia.dao

import ru.netology.nmedia.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun shareCounter(id: Long)
    fun save(post: Post): Post
    fun removeById(id: Long)
}