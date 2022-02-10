package com.globasure.giftoga.network.repository

import com.globasure.giftoga.domain.repository.AuthRepository
import com.globasure.giftoga.network.remote.ApiHelper
import com.globasure.giftoga.network.request.*
import com.globasure.giftoga.network.response.*

class AuthRepositoryImpl(
    private val apiHelper: ApiHelper
) : AuthRepository {

    override suspend fun signUpPersonal(request: SignUpPersonalRequest): SignUpResponse {
        return apiHelper.signUpPersonal(request)
    }

    override suspend fun signUpBusiness(request: SignUpOtherRequest): SignUpResponse {
        return apiHelper.signUpBusiness(request)
    }

    override suspend fun signUpAgent(request: SignUpOtherRequest): SignUpResponse {
        return apiHelper.signUpAgent(request)
    }

    override suspend fun signUpMerchant(request: SignUpOtherRequest): SignUpResponse {
        return apiHelper.signUpMerchant(request)
    }

    override suspend fun signIn(request: LoginRequest): LoginResponse {
        return apiHelper.login(request)
    }

    override suspend fun refreshToken(refresh_token: String): LoginResponse {
        return apiHelper.refreshToken(refresh_token)
    }

    override suspend fun getAccountDetails(): ProfileResponse {
        return apiHelper.getAccountDetails()
    }

    override suspend fun requestResetToken(email: String): ResetTokenResponse {
        return apiHelper.requestResetToken(email)
    }

    override suspend fun validateToken(request: ValidateTokenRequest): ValidateTokenResponse {
        return apiHelper.validateToken(request)
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): ResetTokenResponse {
        return apiHelper.resetPassword(request)
    }

    override suspend fun getCountryList(): CountryListResponse {
        return apiHelper.getCountryList()
    }

    override suspend fun getStatesByCountry(country: String): StateListResponse {
        return apiHelper.getStatesByCountry(country)
    }

    override suspend fun requestConfirmAccount(security_token: String): SignUpResponse {
        return apiHelper.requestConfirmAccount(security_token)
    }

}