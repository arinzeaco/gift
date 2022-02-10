package com.globasure.giftoga.ui.screen.authentication.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.LoginFragBinding
import com.globasure.giftoga.ui.base.BaseFragment
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import timber.log.Timber

@AndroidEntryPoint
class LoginFrag : BaseFragment<LoginFragBinding, LoginFragViewModel>(), LoginFragView {

    private var isOn: Boolean = false
    private var isValid: Boolean = false

    private val loginFragViewModel: LoginFragViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.login_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): LoginFragViewModel = loginFragViewModel

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
        viewDataBinding.passwordToggle.setOnClickListener {
            if (isOn) {
                isOn = false
                viewDataBinding.passwordToggle.setImageResource(R.drawable.ic_visibility_off_black_24dp)
                viewDataBinding.passwordEdt.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                isOn = true
                viewDataBinding.passwordToggle.setImageResource(R.drawable.ic_visibility_black_24dp)
                viewDataBinding.passwordEdt.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }

        validateButtonLogin()
    }

    @SuppressLint("CheckResult")
    private fun validateButtonLogin() {
        val emailEdObservable = RxTextView.textChanges(viewDataBinding.emailEdt)
        val passwordObservable = RxTextView.textChanges(viewDataBinding.passwordEdt)
        val isSignInEnabled: Observable<Boolean> = Observable.combineLatest(
            emailEdObservable,
            passwordObservable,
            { email, password ->
                email.isNotEmpty() && password.isNotEmpty()
            })

        isSignInEnabled.subscribe {
            isValid = it
            if (it) {
                viewDataBinding.loginBtn.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.rounded_button_selector)
            } else {
                viewDataBinding.loginBtn.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.button_round_light_blue)
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.loginBtn)
    fun onLoginClick() {
        if (isValid) {
            loginFragViewModel.doLogin(
                email = viewDataBinding.emailEdt.text.toString(),
                password = viewDataBinding.passwordEdt.text.toString()
            )
        }
    }

    override fun loginSuccess() {
        val intent = MainActivity.callingIntent(requireActivity(), true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
        activity?.finish()
    }

    companion object {
        const val SCREEN_TITLE = "Login"

        fun newInstance(): LoginFrag {
            return LoginFrag()
        }
    }
}