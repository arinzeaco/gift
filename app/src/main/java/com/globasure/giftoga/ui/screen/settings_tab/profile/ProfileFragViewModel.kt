package com.globasure.giftoga.ui.screen.settings_tab.profile

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.UpdateAvatarUseCase
import com.globasure.giftoga.domain.usecase.UpdateProfileUseCase
import com.globasure.giftoga.network.request.UpdateProfileRequest
import com.globasure.giftoga.ui.base.BaseViewModel
import com.globasure.giftoga.utils.HawkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import timber.log.Timber

@HiltViewModel
class ProfileFragViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val updateAvatarUseCase: UpdateAvatarUseCase,
    private val hawkHelper: HawkHelper
) : BaseViewModel<ProfileFragView>() {

    fun updateProfileDetail(request: UpdateProfileRequest) {
        handleProfileResult(UpdateProfileUseCase.Result.Loading)

        viewModelScope.launch {
            handleProfileResult(updateProfileUseCase.execute(request))
        }
    }

    private fun handleProfileResult(result: UpdateProfileUseCase.Result) {
        when (result) {
            is UpdateProfileUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is UpdateProfileUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                result.response.data.user?.let {
                    hawkHelper.setUserDetail(it)
                    getView()?.updateProfileSuccess()
                }
            }
            is UpdateProfileUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }

    fun updateAvatar(avatar: MultipartBody.Part?) {
        viewModelScope.launch {
            handleAvatarResult(updateAvatarUseCase.execute(avatar))
        }
    }

    private fun handleAvatarResult(result: UpdateAvatarUseCase.Result) {
        when (result) {
            is UpdateAvatarUseCase.Result.Loading -> {
                getView()?.showProgressDialog(true)
            }
            is UpdateAvatarUseCase.Result.Success -> {
                getView()?.showProgressDialog(false)
                result.response.data.user?.let {
                    hawkHelper.setUserDetail(it)
                    getView()?.updateAvatarSuccess()
                }
            }
            is UpdateAvatarUseCase.Result.Failure -> {
                Timber.e(result.throwable)
                getView()?.showProgressDialog(false)
                getView()?.handleError(result.throwable)
            }
        }
    }
}