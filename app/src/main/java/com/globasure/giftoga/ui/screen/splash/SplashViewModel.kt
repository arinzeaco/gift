package com.globasure.giftoga.ui.screen.splash

import androidx.lifecycle.viewModelScope
import com.globasure.giftoga.domain.usecase.SplashScreenUseCase
import com.globasure.giftoga.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashScreenUseCase: SplashScreenUseCase
) : BaseViewModel<SplashActivityView>() {

    fun handleTokenProcess() {
        viewModelScope.launch {
            handleTokenResult(splashScreenUseCase.execute())
        }
    }

    private fun handleTokenResult(result: SplashScreenUseCase.Result) {
        when (result) {
            is SplashScreenUseCase.Result.Success -> {
                getView()?.redirectToMainActivity()
            }
            is SplashScreenUseCase.Result.Failure -> {
                getView()?.redirectToOnBoarding()
            }
            else -> {
            }
        }
    }
}