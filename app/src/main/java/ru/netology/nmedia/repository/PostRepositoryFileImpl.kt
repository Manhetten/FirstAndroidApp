package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryFileImpl(
    private val context: Context,
) : PostRepository {
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val postsFileName = "posts.json"
    private var nextId = 1L
    private val nextIdFileName = "next_id.json"
    private var posts = emptyList<Post>()

    private val data = MutableLiveData(posts)

    init {
        val postsFile = context.filesDir.resolve(postsFileName)

        posts = if (postsFile.exists()) {
            postsFile.reader().buffered().use {
                gson.fromJson(it, type)
            }
        } else {
            emptyList()
        }

        val nextIdFile = context.filesDir.resolve(nextIdFileName)

        nextId = if (nextIdFile.exists()) {
            nextIdFile.reader().buffered().use {
                gson.fromJson(it, Long::class.java)
            }
        } else {
            nextId
        }
        data.value = posts
        sync()
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likeCounter = if (it.likedByMe) it.likeCounter - 1 else it.likeCounter + 1
            )
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    override fun shareCounter(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shareCounter = it.shareCounter + 1)
        }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    published = "Now",
                    likedByMe = false
                )
            ) + posts
        } else {
            posts.map { if (it.id != post.id) it else it.copy(content = post.content) }
        }
        data.value = posts
        sync()
    }

    private fun sync() {
        context.filesDir.resolve(postsFileName).writer().buffered().use {
            it.write(gson.toJson(posts))
        }

        context.filesDir.resolve(nextIdFileName).writer().buffered().use {
            it.write(gson.toJson(nextId))
        }
    }
}