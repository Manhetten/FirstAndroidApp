package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentOpenPostBinding
import ru.netology.nmedia.dto.Counter
import ru.netology.nmedia.util.IdArg
import ru.netology.nmedia.viewmodel.PostViewModel

class OpenPostFragment : Fragment() {

    companion object {
        var Bundle.idArg by IdArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOpenPostBinding.inflate(layoutInflater)

        val viewModel: PostViewModel by activityViewModels()

        val postId = arguments?.idArg ?: -1

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content

                like.isChecked = post.likedByMe
                like.text = Counter().count(post.likeCounter)

                share.text = Counter().count(post.shareCounter)

//                playVideo.setOnClickListener {
//                    onInteractionListener.onPlayVideo(post)
//                }

                if (post.video != null) {
                    video.visibility = ImageView.VISIBLE
                    playVideo.visibility = ImageButton.VISIBLE
                } else {
                    video.visibility = ImageView.GONE
                    playVideo.visibility = ImageButton.GONE
                }

                like.setOnClickListener {
                    viewModel.likeById(post.id)
                }
                share.setOnClickListener {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                }
                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.menu_options)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    viewModel.removeById(post.id)
                                    viewModel.changeContentAndSave("")
                                    findNavController().navigateUp()
                                    true
                                }

                                R.id.edit -> {
                                    viewModel.edit(post)
                                    findNavController().navigate(R.id.action_openPostFragment_to_newPostFragment)
//                                    findNavController().navigateUp()
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }
            }
        }
        return binding.root
    }
}
