package com.globasure.giftoga.ui.screen.on_boarding


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.AuthenticationType
import com.globasure.giftoga.databinding.ActivityOnboardingBinding
import com.globasure.giftoga.ui.base.BaseActivity
import com.globasure.giftoga.ui.screen.authentication.AuthenticationActivity
import com.globasure.giftoga.ui.screen.on_boarding.boardingFragment.OnBoarding1Fragment
import com.globasure.giftoga.ui.screen.on_boarding.boardingFragment.OnBoarding2Fragment
import com.globasure.giftoga.ui.screen.on_boarding.boardingFragment.OnBoarding3Fragment
import com.globasure.giftoga.ui.screen.on_boarding.boardingFragment.OnBoarding4Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_onboarding.pageIndicatorView
import kotlinx.android.synthetic.main.activity_onboarding.viewPager
import timber.log.Timber

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnboardingBinding, OnBoardingViewModel>(), OnBoardingView {

    private lateinit var pagerAdapterView: MyPagerAdapter

    private val onBoardingViewModel: OnBoardingViewModel by viewModels()

    override fun getLayoutId() = R.layout.activity_onboarding

    override fun getViewModel() = onBoardingViewModel

    override fun getBindingVariable() = BR.viewModel

    override fun getColorStatusBar(): Int = R.color.app_back_ground_color

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun initView() {
        pageIndicatorView.count = 4
        pageIndicatorView.selection = 0

        pagerAdapterView = MyPagerAdapter(supportFragmentManager)

        pagerAdapterView.addFragments(OnBoarding1Fragment())
        pagerAdapterView.addFragments(OnBoarding2Fragment())
        pagerAdapterView.addFragments(OnBoarding3Fragment())
        pagerAdapterView.addFragments(OnBoarding4Fragment())

        viewPager.adapter = pagerAdapterView
    }

    override fun navigateToLogin() {
        val intent = AuthenticationActivity.callingIntent(this, AuthenticationType.LOGIN.type)
        startActivity(intent)
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
    }

    override fun navigateToSignUp() {
        val intent = AuthenticationActivity.callingIntent(this, AuthenticationType.SIGN_UP.type)
        startActivity(intent)
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.signUpBtn)
    fun onSignUpClick() {
        navigateToSignUp()
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.loginBtn)
    fun onLoginClick() {
        navigateToLogin()
    }
}