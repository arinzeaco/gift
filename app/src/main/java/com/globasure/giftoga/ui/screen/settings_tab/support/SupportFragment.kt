package com.globasure.giftoga.ui.screen.settings_tab.support

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.IssueType
import com.globasure.giftoga.databinding.SupportFragBinding
import com.globasure.giftoga.network.request.OpenCaseRequest
import com.globasure.giftoga.network.response.OpenCaseResponse
import com.globasure.giftoga.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SupportFragment : BaseFragment<SupportFragBinding, SupportFragViewModel>(), SupportFragView {

    private val supportViewModel: SupportFragViewModel by viewModels()

    override fun getViewModel(): SupportFragViewModel = supportViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.support_frag

    override fun getPageTitle(): String = SCREEN_TITLE

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

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)
    }

    override fun initView(view: View) {
        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.submit_btn)
    fun onSubmitClick() {
        val subject = viewDataBinding.subjectEdt.text.toString()
        val issue = viewDataBinding.issueEdt.text.toString()
        if (subject.isEmpty() || issue.isEmpty()) {
            showSnackBar(getString(R.string.default_validation))
        } else {
            val request = OpenCaseRequest(IssueType.OTHER.type, subject, issue)
            supportViewModel.submitCase(request)
        }
    }

    override fun submitCaseSuccess(response: OpenCaseResponse) {
        lifecycleScope.launch {
            showSnackBar(getString(R.string.submit_case_successful))
            delay(1500)
            (getBaseActivity() as MainActivity).onBackPressed()
        }
    }

    companion object {
        const val SCREEN_TITLE = "Support"

        fun newInstance(): SupportFragment {
            return SupportFragment()
        }
    }
}