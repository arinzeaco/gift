package com.globasure.giftoga.ui.screen.main_fragment.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.AuthenticationType
import com.globasure.giftoga.databinding.SettingsFragBinding
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.custom.CustomOutline
import com.globasure.giftoga.ui.screen.authentication.AuthenticationActivity
import com.globasure.giftoga.ui.screen.settings_tab.address.AddressFragment
import com.globasure.giftoga.ui.screen.settings_tab.debit_card.DebitCardsFragment
import com.globasure.giftoga.ui.screen.settings_tab.profile.ProfileFragment
import com.globasure.giftoga.ui.screen.settings_tab.security.SecurityFragment
import com.globasure.giftoga.ui.screen.settings_tab.support.SupportFragment
import com.globasure.giftoga.ui.screen.settings_tab.verification.VerificationFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import timber.log.Timber

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsFragBinding, SettingsFragViewModel>(), SettingsFragView {

    private val settingsFragViewModel: SettingsFragViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.settings_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): SettingsFragViewModel = settingsFragViewModel

    override fun getPageTitle(): String = getString(R.string.settings)

    override fun setToolBar(view: View) {
        page_title.text = screenTitle
    }

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun initView(view: View) {
        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }

        viewDataBinding.profileLayout.outlineProvider = CustomOutline()
        viewDataBinding.addressLayout.outlineProvider = CustomOutline()
        viewDataBinding.verificationLayout.outlineProvider = CustomOutline()
        viewDataBinding.debitCardLayout.outlineProvider = CustomOutline()
        viewDataBinding.securityLayout.outlineProvider = CustomOutline()
        viewDataBinding.supportLayout.outlineProvider = CustomOutline()

        viewDataBinding.profileLayout.setOnClickListener {
            val fragment = ProfileFragment.newInstance()
            fragmentNavigation.pushFragment(fragment)
        }

        viewDataBinding.addressLayout.setOnClickListener {
            val fragment = AddressFragment.newInstance()
            fragmentNavigation.pushFragment(fragment)
        }

        viewDataBinding.verificationLayout.setOnClickListener {
            val fragment = VerificationFragment.newInstance()
            fragmentNavigation.pushFragment(fragment)
        }

        viewDataBinding.debitCardLayout.setOnClickListener {
            val fragment = DebitCardsFragment.newInstance("Debit Cards", false)
            fragmentNavigation.pushFragment(fragment)
        }

        viewDataBinding.securityLayout.setOnClickListener {
            val fragment = SecurityFragment.newInstance()
            fragmentNavigation.pushFragment(fragment)
        }

        viewDataBinding.supportLayout.setOnClickListener {
            val fragment = SupportFragment.newInstance()
            fragmentNavigation.pushFragment(fragment)
        }

        viewDataBinding.logoutBtn.setOnClickListener {
            hawkHelper.clearAll()
            val intent = AuthenticationActivity.callingIntent(requireContext(), AuthenticationType.LOGIN.type)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            (getBaseActivity() as MainActivity).overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
            (getBaseActivity() as MainActivity).finish()
        }
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(true)
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}
