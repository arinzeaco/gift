package com.globasure.giftoga.ui.screen.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.globasure.giftoga.BR
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.AuthenticationType
import com.globasure.giftoga.databinding.ActivityAuthenticationBinding
import com.globasure.giftoga.ui.base.BaseActivity
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.authentication.accountType.SelectAccountTypeFrag
import com.globasure.giftoga.ui.screen.authentication.login.LoginFrag
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavLogger
import com.ncapdevi.fragnav.FragNavTransactionOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AuthenticationActivity :
    BaseActivity<ActivityAuthenticationBinding, AuthenticationActivityViewModel>(),
    AuthenticationActivityView, BaseFragment.FragmentNavigation,
    FragNavController.TransactionListener, FragNavController.RootFragmentListener {

    private val authenticationViewModel: AuthenticationActivityViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.activity_authentication

    override fun getViewModel(): AuthenticationActivityViewModel = authenticationViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getColorStatusBar(): Int = R.color.app_back_ground_color

    private val fragNavController: FragNavController =
        FragNavController(supportFragmentManager, R.id.container)
    private var screenNavigation: String = AuthenticationType.LOGIN.type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun initView() {
        screenNavigation = intent.getStringExtra(SCREEN_REDIRECT)!!

        fragNavController.apply {
            transactionListener = this@AuthenticationActivity
            rootFragmentListener = this@AuthenticationActivity
            createEager = true
            fragNavLogger = object : FragNavLogger {
                override fun error(message: String, throwable: Throwable) {
                    Timber.e(message)
                }
            }

            defaultTransactionOptions = FragNavTransactionOptions.newBuilder().transition(
                FragmentTransaction.TRANSIT_NONE
            ).build()
            fragmentHideStrategy = FragNavController.DETACH_ON_NAVIGATE_HIDE_ON_SWITCH
        }

        fragNavController.initialize(0, null)
    }

    override fun pushFragment(fragment: Fragment, sharedElementList: List<Pair<View, String>>?) {
        val options = FragNavTransactionOptions.newBuilder()
        options.customAnimations(
            R.anim.slide_in_from_right,
            R.anim.slide_out_to_left,
            R.anim.slide_in_from_left,
            R.anim.slide_out_to_right
        )
        sharedElementList?.let {
            it.forEach { pair ->
                options.addSharedElement(pair)
            }
        }
        fragNavController.pushFragment(fragment, options.build())
    }

    override fun onBackPressed() {
        fragNavController.currentFrag?.let {
            val isRootFragment = (it is SelectAccountTypeFrag || it is LoginFrag)
            when {
                isRootFragment -> finish()
                else -> {
                    val options = FragNavTransactionOptions.newBuilder()
                    options.customAnimations(
                        R.anim.slide_in_from_right,
                        R.anim.slide_out_to_left,
                        R.anim.slide_in_from_left,
                        R.anim.slide_out_to_right
                    )
                    fragNavController.popFragment(options.build())
                }
            }
        }

        if (fragNavController.currentFrag == null) {
            super.onBackPressed()
        }
    }

    override val numberOfRootFragments: Int = 1

    override fun getRootFragment(index: Int): Fragment {
        if (index == 0) {
            when (screenNavigation) {
                AuthenticationType.SIGN_UP.type -> return SelectAccountTypeFrag.newInstance()
                AuthenticationType.LOGIN.type -> return LoginFrag.newInstance()
            }
        }
        throw IllegalStateException("Need to send an index that we know")
    }

    override fun onFragmentTransaction(
        fragment: Fragment?,
        transactionType: FragNavController.TransactionType
    ) {
        Timber.d("- onFragmentTransaction - ${fragment?.tag}")
    }

    override fun onTabTransaction(fragment: Fragment?, index: Int) {
        Timber.d("-onTabTransaction - ${fragment?.tag}")
    }

    companion object {
        const val SCREEN_REDIRECT = "screen_redirect"

        fun callingIntent(context: Context, screen: String): Intent {
            val intent = Intent(context, AuthenticationActivity::class.java)
            intent.putExtra(SCREEN_REDIRECT, screen)
            return intent
        }
    }
}