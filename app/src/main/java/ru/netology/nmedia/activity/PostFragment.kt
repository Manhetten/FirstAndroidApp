package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.util.AndroidUtils.focusAndShowKeyboard
import ru.netology.nmedia.util.AndroidUtils.hideKeyboard
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {
    companion object {
        var Bundle.text by StringArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostBinding.inflate(layoutInflater)

        val viewModel: PostViewModel by activityViewModels()

        val text = arguments?.text

        if (text != null) {
            binding.edit.setText(text)
        } else {
            binding.edit.setText(viewModel.edited.value?.content)
        }

        binding.edit.focusAndShowKeyboard()
        binding.ok.setOnClickListener {
            hideKeyboard(it)

            if (!binding.edit.text.isNullOrBlank()) {
                val content = binding.edit.text.toString()
                viewModel.changeContentAndSave(content)
            }

            findNavController().navigateUp()
        }

        return binding.root
    }

}