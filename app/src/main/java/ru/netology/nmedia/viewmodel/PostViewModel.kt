package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    video = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryRoomImpl(
        AppDb.getInstance(application).postDao
    )

    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    private val _draft = MutableLiveData<String>()
    val draft: LiveData<String> = _draft

    fun changeDraft(newDraft: String) {
        _draft.value = newDraft
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareCounter(id: Long) = repository.shareCounter(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContentAndSave(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content != text) {
                repository.save(it.copy(content = text))
            }
            edited.value = empty
        }
    }
}