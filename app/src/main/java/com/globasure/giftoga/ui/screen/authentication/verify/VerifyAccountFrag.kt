package com.globasure.giftoga.ui.screen.authentication.verify

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.VerifyAccountFragBinding
import com.globasure.giftoga.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class VerifyAccountFrag : BaseFragment<VerifyAccountFragBinding, VerifyAccountFragViewModel>(), VerifyAccountFragView {

    private val verifyAccountViewModel: VerifyAccountFragViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.verify_account_frag

    override fun getViewModel(): VerifyAccountFragViewModel = verifyAccountViewModel

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
            viewDataBinding.emailTxt.text = it.getString(EMAIL)!!
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.verifyEmailBtn)
    fun onVerifyEmailClick() {
        if (viewDataBinding.codeEdt.text.isNotBlank()) {
            verifyAccountViewModel.verifyAccount(viewDataBinding.codeEdt.text.toString())
        } else {
            showSnackBar(getString(R.string.validate_code))
        }
    }

    override fun verifyEmailDone() {
        val intent = MainActivity.callingIntent(requireActivity(), true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
        activity?.finish()
    }

    companion object {
        const val SCREEN_TITLE = "Verify Account"
        private const val EMAIL = "email"
        private const val FIRST_NAME = "first_name"

        fun newInstance(email: String, userName: String): VerifyAccountFrag {
            val args = Bundle()
            args.putString(EMAIL, email)
            args.putString(FIRST_NAME, userName)
            val fragment = VerifyAccountFrag()
            fragment.arguments = args
            return fragment
        }
    }
}