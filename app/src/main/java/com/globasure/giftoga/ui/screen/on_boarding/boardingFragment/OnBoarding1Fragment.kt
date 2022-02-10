package com.globasure.giftoga.ui.screen.on_boarding.boardingFragment

import android.view.View
import androidx.fragment.app.viewModels
import com.globasure.giftoga.BR
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.Onboarding1FragBinding
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.on_boarding.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OnBoarding1Fragment : BaseFragment<Onboarding1FragBinding, OnBoardingViewModel>() {

    private val onBoardingViewModel: OnBoardingViewModel by viewModels()

    override fun getLayoutId() = R.layout.onboarding_1_frag

    override fun getViewModel(): OnBoardingViewModel = onBoardingViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun initView(view: View) {
        //do nothing
    }

    override fun getPageTitle(): String = "OnBoarding 1"

    override fun setToolBar(view: View) {
        //do nothing
    }
}
