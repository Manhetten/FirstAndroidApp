package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likedByMe: Boolean,
    val likeCounter: Int = 0,
    val shareCounter: Int = 0,
    val video: String? = null
) {
    fun toDto() = Post(id, author, published, content, likedByMe, likeCounter, shareCounter, video)

    companion object {
        fun fromDto(post: Post) = PostEntity(
            post.id,
            post.author,
            post.published,
            post.content,
            post.likedByMe,
            post.likeCounter,
            post.shareCounter,
            post.video
        )
    }
}

