package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils.focusAndShowKeyboard
import ru.netology.nmedia.util.AndroidUtils.hideKeyboard
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {
    companion object {
        var Bundle.text by StringArg
    }

    private val viewModel: PostViewModel by activityViewModels()

    private var _binding: FragmentNewPostBinding? = null
    val binding: FragmentNewPostBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPostBinding.inflate(layoutInflater)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.changeDraft(binding.edit.text.toString())
                    findNavController().navigateUp()
                }
            }
        )

        subscribeDraft()

        binding.edit.focusAndShowKeyboard()
        binding.ok.setOnClickListener {
            hideKeyboard(it)
            if (!binding.edit.text.isNullOrBlank()) {
                val content = binding.edit.text.toString()
                viewModel.changeContentAndSave(content)
                viewModel.changeDraft("")
            }
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun subscribeDraft() {
        viewModel.draft.observe(viewLifecycleOwner) { draft ->
            setEditContent(arguments?.text ?: "", draft)
        }
    }

    private fun setEditContent(text: String, draft: String) {
        if (text.isNotEmpty()) {
            binding.edit.setText(text)
        } else {
            binding.edit.setText(draft)
        }
    }
}