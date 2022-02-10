package com.globasure.giftoga.domain.repository

import com.globasure.giftoga.network.request.*
import com.globasure.giftoga.network.response.BankListResponse
import com.globasure.giftoga.network.response.ProfileResponse
import com.globasure.giftoga.network.response.ValidateBankAccountResponse
import okhttp3.MultipartBody

interface SettingRepository {

    suspend fun updateProfile(request: UpdateProfileRequest): ProfileResponse

    suspend fun updateAddress(request: UpdateAddressRequest): ProfileResponse

    suspend fun updateAvatar(avatar: MultipartBody.Part?): ProfileResponse

    suspend fun updateNotification(request: UpdateNotificationRequest): ProfileResponse

    suspend fun getBankList(): BankListResponse

    suspend fun validateBankAccount(request: ValidateBankAccountRequest): ValidateBankAccountResponse

    suspend fun allowDailyTransfer(request: AllowDailyTransferRequest): ProfileResponse
}