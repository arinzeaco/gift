package com.globasure.giftoga.network.repository

import com.globasure.giftoga.domain.repository.SettingRepository
import com.globasure.giftoga.network.remote.ApiHelper
import com.globasure.giftoga.network.request.*
import com.globasure.giftoga.network.response.BankListResponse
import com.globasure.giftoga.network.response.ProfileResponse
import com.globasure.giftoga.network.response.ValidateBankAccountResponse
import okhttp3.MultipartBody

class SettingRepositoryImpl(
    private val apiHelper: ApiHelper
) : SettingRepository {

    override suspend fun updateProfile(request: UpdateProfileRequest): ProfileResponse {
        return apiHelper.updateProfile(request)
    }

    override suspend fun updateAddress(request: UpdateAddressRequest): ProfileResponse {
        return apiHelper.updateAddress(request)
    }

    override suspend fun updateAvatar(avatar: MultipartBody.Part?): ProfileResponse {
        return apiHelper.updateAvatar(avatar)
    }

    override suspend fun updateNotification(request: UpdateNotificationRequest): ProfileResponse {
        return apiHelper.updateNotification(request)
    }

    override suspend fun getBankList(): BankListResponse {
        return apiHelper.getBankList()
    }

    override suspend fun validateBankAccount(request: ValidateBankAccountRequest): ValidateBankAccountResponse {
        return apiHelper.validateBankAccount(request)
    }

    override suspend fun allowDailyTransfer(request: AllowDailyTransferRequest): ProfileResponse {
        return apiHelper.allowDailyTransfer(request)
    }
}