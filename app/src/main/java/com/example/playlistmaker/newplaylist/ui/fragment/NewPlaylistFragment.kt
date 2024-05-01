package com.example.playlistmaker.newplaylist.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.newplaylist.data.storage.ExternalImagesStorage.Companion.FILE_DIRECTORY
import com.example.playlistmaker.newplaylist.ui.view_model.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

open class NewPlaylistFragment : Fragment() {

    open val viewModel: NewPlaylistViewModel by viewModel()
    protected var _binding: FragmentNewPlaylistBinding? = null
    protected val binding get() = _binding!!

    private val requester = PermissionRequester.instance()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private lateinit var editTextTextWatcher: TextWatcher
    private lateinit var editTextTextWatcher2: TextWatcher

    private lateinit var listener: (s: Editable?, textInputLayout: TextInputLayout) -> Unit

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backPressedHandle()
        }
    }

    var imageId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        binding.backToMediaButton.setOnClickListener {
            backPressedHandle()
        }

        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(this)
                        .load(uri)
                        .transform(
                            CenterCrop(),
                            RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin8dp))
                        )
                        .into(binding.newPlaylistIcon)
                    if (imageId == "") imageId = UUID.randomUUID().toString()
                    viewModel.mediaPicked(uri, imageId)
                }
            }

        binding.newPlaylistIcon.setOnClickListener {
            pickImage()
        }

        binding.newPlaylistAddButton.setOnClickListener {
            viewModel.onButtonSaveClick(
                playlistId = null,
                playlistName = binding.newPlaylistNameField.text.toString(),
                playlistDescription = binding.newPlaylistDescriptionField.text.toString(),
                playlistCoverPath = if (imageId != "") {
                    activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path +
                            "/$FILE_DIRECTORY/playlist-$imageId.jpg"
                } else {
                    ""
                }
            )
            findNavController().navigateUp()

            Toast.makeText(requireContext(),
                "Плейлист ${binding.newPlaylistNameField.text.toString()} создан", Toast.LENGTH_LONG).show()
        }

        listener =
            { s, textInputLayout ->
                if (s.isNullOrEmpty()) {
                    textInputLayout.setBoxStrokeColorStateList(
                        resources.getColorStateList(
                            R.color.ypBlue,
                            activity?.theme
                        )
                    )
                    textInputLayout.defaultHintTextColor =
                        resources.getColorStateList(R.color.white, activity?.theme)
                    textInputLayout.hintTextColor =
                        resources.getColorStateList(R.color.ypBlue, activity?.theme)
                } else {
                    textInputLayout.setBoxStrokeColorStateList(
                        resources.getColorStateList(
                            R.color.ypBlue,
                            activity?.theme
                        )
                    )
                    textInputLayout.defaultHintTextColor =
                        resources.getColorStateList(R.color.ypBlue, activity?.theme)
                }
            }

        editTextTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.newPlaylistAddButton.isEnabled = !s.isNullOrBlank()
            }

            override fun afterTextChanged(s: Editable?) {
                listener(s, binding.newPlaylistNameInputLayer)
            }
        }
        binding.newPlaylistNameField.addTextChangedListener(editTextTextWatcher)

        editTextTextWatcher2 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                listener(s, binding.newPlaylistDescriptionInputLayer)
            }
        }
        binding.newPlaylistDescriptionField.addTextChangedListener(editTextTextWatcher2)

        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(getString(R.string.newPlaylistAddConfirm))
            .setMessage(getString(R.string.newPlalistUnsavedDataLost))
            .setNeutralButton(getString(R.string.cancel)) { dialog, which ->

            }.setPositiveButton(getString(R.string.done)) { dialog, which ->
                findNavController().navigateUp()
            }
        activity?.onBackPressedDispatcher?.addCallback(backCallback)
    }

    private fun pickImage() {
        lifecycleScope.launch {
            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                android.Manifest.permission.READ_MEDIA_IMAGES
            } else {
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            }
            requester.request(permission).collect { result ->
                when (result) {
                    is PermissionResult.Granted -> {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }

                    is PermissionResult.Denied.DeniedPermanently -> {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.data = Uri.fromParts("package", context?.packageName, null)
                        context?.startActivity(intent)
                    }

                    is PermissionResult.Denied.NeedsRationale -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.playlistsIconsPermission),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    is PermissionResult.Cancelled -> {
                        return@collect
                    }
                }
            }
        }
    }

    open fun backPressedHandle() {
        if (!binding.newPlaylistNameField.text.isNullOrEmpty() ||
            !binding.newPlaylistDescriptionField.text.isNullOrEmpty() ||
            !imageId.isEmpty()
        ) {
            confirmDialog.show()
        } else {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backCallback.remove()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        binding.newPlaylistNameField.removeTextChangedListener(editTextTextWatcher)
        binding.newPlaylistDescriptionField.removeTextChangedListener(editTextTextWatcher2)
        _binding = null
    }
}