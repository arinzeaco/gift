package com.globasure.giftoga.ui.screen.on_boarding.boardingFragment

import android.view.View
import androidx.fragment.app.viewModels
import com.globasure.giftoga.BR
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.Onboarding3FragBinding
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.on_boarding.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OnBoarding3Fragment : BaseFragment<Onboarding3FragBinding, OnBoardingViewModel>() {

    private val onBoardingViewModel: OnBoardingViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.onboarding_3_frag

    override fun getViewModel(): OnBoardingViewModel = onBoardingViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun initView(view: View) {
        //do nothing
    }

    override fun getPageTitle(): String = "OnBoarding 3"

    override fun setToolBar(view: View) {
        //do nothing
    }
}
