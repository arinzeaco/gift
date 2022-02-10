package com.globasure.giftoga.domain.usecase

import com.globasure.giftoga.domain.repository.SettingRepository
import com.globasure.giftoga.network.response.ProfileResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class UpdateAvatarUseCase @Inject constructor(private val settingRepository: SettingRepository) {

    sealed class Result {
        object Loading : Result()
        data class Success(val response: ProfileResponse) : Result()
        data class Failure(val throwable: Throwable) : Result()
    }

    suspend fun execute(avatar: MultipartBody.Part?): Result {
        return try {
            Result.Success(settingRepository.updateAvatar(avatar))
        } catch (exception: Exception) {
            Result.Failure(exception)
        }
    }
}