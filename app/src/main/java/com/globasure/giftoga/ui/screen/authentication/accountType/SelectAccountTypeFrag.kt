package com.globasure.giftoga.ui.screen.authentication.accountType

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.AccountType
import com.globasure.giftoga.databinding.SelectAccountTypeFragBinding
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.authentication.AuthenticationActivity
import com.globasure.giftoga.ui.screen.authentication.otherAccount.OtherAccountFrag
import com.globasure.giftoga.ui.screen.authentication.personalAccount.PersonalAccountFrag
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SelectAccountTypeFrag : BaseFragment<SelectAccountTypeFragBinding, SelectAccountTypeViewModel>(),
    SelectAccountTypeView {

    private val selectAccountTypeViewModel: SelectAccountTypeViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.select_account_type_frag

    override fun getViewModel(): SelectAccountTypeViewModel = selectAccountTypeViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getPageTitle(): String = SCREEN_TITLE

    override fun setToolBar(view: View) {
        //do nothing
    }

    private var accountType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewModel().setView(this)
    }

    override fun initView(view: View) {
        viewDataBinding.personalAccountLayout.setOnClickListener {
            accountType = AccountType.PERSONAL.toString()
            setLayoutClickTheme(
                viewDataBinding.personalAccountLayout,
                viewDataBinding.personal1,
                viewDataBinding.personal2
            )
        }
        viewDataBinding.corporateAccountLayout.setOnClickListener {
            accountType = AccountType.BUSINESS.type
            setLayoutClickTheme(
                viewDataBinding.corporateAccountLayout,
                viewDataBinding.corporate1,
                viewDataBinding.corporate2
            )
        }
        viewDataBinding.merchantAccountLayout.setOnClickListener {
            accountType = AccountType.MERCHANT.type
            setLayoutClickTheme(
                viewDataBinding.merchantAccountLayout,
                viewDataBinding.merchant1,
                viewDataBinding.merchant2
            )
        }
        viewDataBinding.agentAccountLayout.setOnClickListener {
            accountType = AccountType.AGENT.type
            setLayoutClickTheme(
                viewDataBinding.agentAccountLayout,
                viewDataBinding.agent1,
                viewDataBinding.agent2
            )
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.continue_Btn)
    fun onContinueClick() {
        if (accountType == AccountType.AGENT.type || accountType == AccountType.BUSINESS.type ||
            accountType == AccountType.MERCHANT.type
        ) {
            (getBaseActivity() as AuthenticationActivity).pushFragment(
                OtherAccountFrag.newInstance(
                    accountType
                )
            )
        } else {
            (getBaseActivity() as AuthenticationActivity).pushFragment(PersonalAccountFrag.newInstance())
        }
    }

    private fun resetLayoutClickTheme() {
        viewDataBinding.personalAccountLayout.normalBackGround()
        viewDataBinding.personal1.normalTextColor()
        viewDataBinding.personal2.normalTextColor()

        viewDataBinding.corporateAccountLayout.normalBackGround()
        viewDataBinding.corporate1.normalTextColor()
        viewDataBinding.corporate2.normalTextColor()

        viewDataBinding.merchantAccountLayout.normalBackGround()
        viewDataBinding.merchant1.normalTextColor()
        viewDataBinding.merchant2.normalTextColor()

        viewDataBinding.agentAccountLayout.normalBackGround()
        viewDataBinding.agent1.normalTextColor()
        viewDataBinding.agent2.normalTextColor()
    }

    private fun setLayoutClickTheme(
        layout: ConstraintLayout,
        title: TextView,
        subtitle: TextView
    ) {
        resetLayoutClickTheme()

        layout.highlightBackground()
        title.highLightTextColor()
        subtitle.highLightTextColor()
    }

    private fun TextView.normalTextColor() {
        this.setTextColor(ContextCompat.getColor(requireContext(), R.color.intro_small_text))
    }

    private fun TextView.highLightTextColor() {
        this.setTextColor(Color.WHITE)
    }

    private fun View.normalBackGround() {
        this.setBackgroundResource(R.drawable.grey_unfilled_round_coner)
    }

    private fun View.highlightBackground() {
        this.setBackgroundResource(R.drawable.dark_blue_filled)
    }

    companion object {
        const val SCREEN_TITLE = "Select Account"

        fun newInstance(): SelectAccountTypeFrag {
            return SelectAccountTypeFrag()
        }
    }
}
