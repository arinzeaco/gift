package com.globasure.giftoga.ui.screen.settings_tab.security

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.SecurityFragBinding
import com.globasure.giftoga.network.request.ResetPasswordRequest
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.utils.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SecurityFragment : BaseFragment<SecurityFragBinding, SecurityViewModel>(), SecurityFragView {

    private val securityViewModel: SecurityViewModel by viewModels()

    private var isAllowToUpdatePassword: Boolean = false
    private var isOn: Boolean = false

    override fun getLayoutId(): Int = R.layout.security_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): SecurityViewModel = securityViewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getPageTitle(): String = getString(R.string.security)

    override fun setToolBar(view: View) {
        page_title.text = screenTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)
    }

    override fun initView(view: View) {
        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }

        viewDataBinding.newPasswordToggle.setOnClickListener {
            if (isOn) {
                isOn = false
                viewDataBinding.newPasswordToggle.setImageResource(R.drawable.ic_visibility_off_black_24dp)
                viewDataBinding.newPasswordEdt.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                isOn = true
                viewDataBinding.newPasswordToggle.setImageResource(R.drawable.ic_visibility_black_24dp)
                viewDataBinding.newPasswordEdt.transformationMethod =
                    PasswordTransformationMethod.getInstance()
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.save_information_btn)
    fun onSaveInformationClick() {
        if (isAllowToUpdatePassword) { //start update password
            securityViewModel.updatePassword(
                ResetPasswordRequest(
                    hawkHelper.getUserDetail()?.email!!,
                    viewDataBinding.confirmTokenEdt.text.toString(),
                    viewDataBinding.newPasswordEdt.text.toString()
                )
            )
        } else { //need to request token
            securityViewModel.requestToken(hawkHelper.getUserDetail()?.email!!)
        }
    }

    override fun requestTokenSuccess() {
        if (!isAllowToUpdatePassword) {
            viewDataBinding.confirmTokenLabel.visible()
            viewDataBinding.confirmTokenEdt.visible()
            viewDataBinding.saveInformationBtn.text = getString(R.string.save_information)
            showSnackBar(getString(R.string.request_token_success))
            isAllowToUpdatePassword = true
        }
    }

    override fun updatePasswordSuccess() {
        showSnackBar(getString(R.string.update_password_success))
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1500)
            (getBaseActivity() as MainActivity).onBackPressed()
        }
    }

    companion object {
        fun newInstance(): SecurityFragment {
            return SecurityFragment()
        }
    }
}
