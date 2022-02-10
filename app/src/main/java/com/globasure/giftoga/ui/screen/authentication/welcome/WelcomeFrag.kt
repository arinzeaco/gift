package com.globasure.giftoga.ui.screen.authentication.welcome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.WelcomeFragBinding
import com.globasure.giftoga.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class WelcomeFrag : BaseFragment<WelcomeFragBinding, WelcomeFragViewModel>(), WelcomeFragView {

    private val welcomeFragViewModel: WelcomeFragViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.welcome_frag

    override fun getViewModel(): WelcomeFragViewModel = welcomeFragViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getPageTitle(): String = SCREEN_TITLE

    override fun setToolBar(view: View) {
        //do nothing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun initView(view: View) {
        arguments?.let {
            viewDataBinding.createYourAccount.text =
                String.format(getString(R.string.welcome_title), it.getString(WELCOME_NAME))
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.continueBtn)
    fun onContinueClick() {
        navigateToHomePage()
    }

    override fun navigateToHomePage() {
        val intent = MainActivity.callingIntent(requireActivity(), true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
        activity?.finish()
    }

    companion object {
        const val SCREEN_TITLE = "Welcome"
        private const val WELCOME_NAME = "welcome_name"

        fun newInstance(userName: String): WelcomeFrag {
            val args = Bundle()
            args.putString(WELCOME_NAME, userName)
            val fragment = WelcomeFrag()
            fragment.arguments = args
            return fragment
        }
    }
}