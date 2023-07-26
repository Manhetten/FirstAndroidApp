package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Counter
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likeCount.text = Counter().count(post.likes)
                shareCount.text = Counter().count(post.shares)

                like.setImageResource(if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_sharp_favorite_border_24)
            }
        }

        binding.share.setOnClickListener {
            viewModel.share()
        }
        binding.like.setOnClickListener {
            viewModel.like()
        }
    }
}
