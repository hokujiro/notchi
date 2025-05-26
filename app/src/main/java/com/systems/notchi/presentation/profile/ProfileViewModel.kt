package com.systems.notchi.presentation.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.systems.notchi.domain.usecase.profile.GetCurrentUserUseCase
import com.systems.notchi.domain.usecase.profile.UpdateUserProfileUseCase
import com.systems.notchi.domain.usecase.profile.UploadProfilePhotoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val uploadProfilePhoto: UploadProfilePhotoUseCase,
) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _profileImageUri = MutableStateFlow<String?>(null)
    val profileImageUri: StateFlow<String?> = _profileImageUri.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init {
        viewModelScope.launch {
            loadUserProfile()
        }
    }

    private suspend fun loadUserProfile() {
        try {
            _loading.value = true
            val user = getCurrentUserUseCase.execute()
            _username.value = user.username
            _profileImageUri.value = user.photo
        } finally {
            _loading.value = false
        }
    }


    fun updateUsername(name: String) {
        _username.value = name
    }

    fun selectProfileImage(uri: String) {
        _profileImageUri.value = uri
    }

    fun saveProfile() {
        viewModelScope.launch {
            updateUserProfileUseCase.execute(
                username = _username.value,
                profileImageUri = _profileImageUri.value
            )
        }
    }

    fun uploadProfileImage(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            try {
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(imageUri) ?: return@launch
                val bytes = inputStream.readBytes()
                val requestFile = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData(
                    "file",
                    "profile_${UUID.randomUUID()}.jpg",
                    requestFile
                )

                val url = uploadProfilePhoto.execute(filePart)
                _profileImageUri.value = url

                // Optionally update user profile
                updateUserProfileUseCase.execute(username = username.value, profileImageUri = url)

            } catch (e: Exception) {
                Log.e("Upload", "Failed to upload image", e)
            }
        }
    }
}