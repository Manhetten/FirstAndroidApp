package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Query(
        """
    UPDATE PostEntity SET
               likeCounter = likeCounter + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = :id;
    """
    )
    fun likeById(id: Long)

    @Query(
        """
            UPDATE PostEntity SET
                shareCounter = shareCounter + 1
            WHERE id = :id;
        """
    )
    fun shareCounter(id: Long)

    @Insert
    fun insert(post: PostEntity)
    fun save(post: PostEntity) =
        if (post.id == 0L) insert(post) else changeContentById(post.id, post.content)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)

    @Query("UPDATE PostEntity SET content= :text WHERE id= :id")
    fun changeContentById(id: Long, text: String)
}