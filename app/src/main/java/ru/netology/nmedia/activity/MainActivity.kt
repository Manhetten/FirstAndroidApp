package ru.netology.nmedia.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.Group
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.AndroidUtils.focusAndShowKeyboard
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareCounter(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)

            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }
        }
        )
        binding.recyclerList.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.recyclerList.smoothScrollToPosition(0)
                }
            }
        }
        viewModel.edited.observe(this) {
            if (it.id != 0L) {
                binding.content.setText(it.content)
                binding.content.focusAndShowKeyboard()
                binding.editContent.text = it.content
                binding.group.visibility = View.VISIBLE
            }
        }
        binding.save.setOnClickListener {
            val text = binding.content.text.toString()
            if (text.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.changeContentAndSave(text)

            binding.group.visibility = View.GONE
            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(it)
        }
        binding.cancel.setOnClickListener {
            binding.group.visibility = View.GONE
            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(it)
        }
    }
}


