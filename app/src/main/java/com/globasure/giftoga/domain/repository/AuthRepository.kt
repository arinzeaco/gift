package com.globasure.giftoga.domain.repository

import com.globasure.giftoga.network.request.*
import com.globasure.giftoga.network.response.*

interface AuthRepository {

    suspend fun signUpPersonal(request: SignUpPersonalRequest): SignUpResponse

    suspend fun signUpBusiness(request: SignUpOtherRequest): SignUpResponse

    suspend fun signUpAgent(request: SignUpOtherRequest): SignUpResponse

    suspend fun signUpMerchant(request: SignUpOtherRequest): SignUpResponse

    suspend fun signIn(request: LoginRequest): LoginResponse

    suspend fun refreshToken(refresh_token: String): LoginResponse

    suspend fun getAccountDetails(): ProfileResponse

    suspend fun requestResetToken(email: String): ResetTokenResponse

    suspend fun validateToken(request: ValidateTokenRequest): ValidateTokenResponse

    suspend fun resetPassword(request: ResetPasswordRequest): ResetTokenResponse

    suspend fun getCountryList(): CountryListResponse

    suspend fun getStatesByCountry(country: String): StateListResponse

    suspend fun requestConfirmAccount(security_token: String): SignUpResponse
}