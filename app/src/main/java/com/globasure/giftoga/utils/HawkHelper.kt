package com.globasure.giftoga.utils

import com.globasure.giftoga.network.response.UserDetail
import com.orhanobut.hawk.Hawk

class HawkHelper {

    companion object {
        private const val TOKEN = "token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val USER_DETAIL = "user_detail"
    }

    fun getToken(): String {
        return Hawk.get(TOKEN, "")
    }

    fun setToken(token: String) {
        Hawk.put(TOKEN, "Bearer $token")
    }

    fun getRefreshToken(): String {
        return Hawk.get(REFRESH_TOKEN, "")
    }

    fun setRefreshToken(refreshToken: String) {
        Hawk.put(REFRESH_TOKEN, refreshToken)
    }

    fun getUserDetail(): UserDetail? {
        return Hawk.get(USER_DETAIL)
    }

    fun setUserDetail(userDetail: UserDetail) {
        Hawk.put(USER_DETAIL, userDetail)
    }

    fun clearAll() {
        Hawk.deleteAll()
    }

}