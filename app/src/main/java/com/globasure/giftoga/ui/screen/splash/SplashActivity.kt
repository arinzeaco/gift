package com.globasure.giftoga.ui.screen.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.ActivitySplashBinding
import com.globasure.giftoga.ui.base.BaseActivity
import com.globasure.giftoga.ui.screen.on_boarding.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), SplashActivityView, CoroutineScope {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun getLayoutId() = R.layout.activity_splash

    override fun getViewModel(): SplashViewModel = splashViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getColorStatusBar(): Int = R.color.splash_background

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun initView() {
        if (hawkHelper.getToken().isEmpty()) {
            redirectToOnBoarding()
        } else {
            splashViewModel.handleTokenProcess()
        }
    }

    override fun redirectToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        launch {
            delay(3000)
            startActivity(intent)
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
            finish()
        }
    }

    override fun redirectToOnBoarding() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        launch {
            delay(3000)
            startActivity(intent)
            finish()
        }
    }

    override fun logout() {
        //do nothing
    }

    override fun screenBack() {
        //do nothing
    }
}
