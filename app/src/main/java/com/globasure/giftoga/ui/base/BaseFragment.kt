package com.globasure.giftoga.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.common.ErrorHandler
import com.globasure.giftoga.common.ErrorNotification
import com.globasure.giftoga.constant.AuthenticationType
import com.globasure.giftoga.ui.custom.GiftOgaProgressDialog
import com.globasure.giftoga.ui.screen.authentication.AuthenticationActivity
import com.globasure.giftoga.utils.HawkHelper
import com.globasure.giftoga.utils.extension.hideKeyBoardWhenTouchOutside
import com.globasure.giftoga.utils.extension.isConnected
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject
import timber.log.Timber

abstract class BaseFragment<B : ViewDataBinding, V : BaseViewModel<*>> : Fragment(), BaseView, ErrorNotification {

    private var baseActivity: BaseActivity<*, *>? = null
    private lateinit var viewModel: V
    open val screenTitle: String?
        get() = getPageTitle()

    abstract fun getViewModel(): V

    abstract fun getBindingVariable(): Int

    fun getBaseActivity() = this.baseActivity

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getPageTitle(): String?

    abstract fun setToolBar(view: View)

    private var unBinder: Unbinder? = null

    protected lateinit var viewDataBinding: B

    private lateinit var progressDialog: GiftOgaProgressDialog

    lateinit var fragmentNavigation: FragmentNavigation

    @Inject
    lateinit var errorHandler: ErrorHandler

    @Inject
    lateinit var hawkHelper: HawkHelper

    open fun getColorStatusBar(): Int? = null

    abstract fun initView(view: View)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            this.baseActivity = context
        }
        if (context is FragmentNavigation) {
            fragmentNavigation = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewModel = this.getViewModel()

        this.progressDialog = GiftOgaProgressDialog(requireContext())

        getColorStatusBar()?.let { setStatusBarColor(it) }
    }

    private fun setStatusBarColor(color: Int) {
        baseActivity?.setStatusBarColor(color)
    }

    override fun onResume() {
        super.onResume()
        getColorStatusBar()?.let { setStatusBarColor(it) }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDetach() {
        this.baseActivity = null
        super.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        unBinder = ButterKnife.bind(this, this.viewDataBinding.root)

        return this.viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.viewDataBinding.setVariable(this.getBindingVariable(), this.viewModel)
        this.viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        this.viewDataBinding.executePendingBindings()

        hideKeyBoardWhenTouchOutside()
        initView(view)
        setToolBar(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unBinder?.unbind()
    }

    override fun logout() {

    }

    override fun screenBack() {
        baseActivity?.onBackPressed()
    }

    override fun showProgressDialog(isShow: Boolean) {
        if (isShow) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    private fun showProgress() {
        if (!progressDialog.isShowing) progressDialog.show()
    }

    private fun hideProgress() {
        if (progressDialog.isShowing) progressDialog.dismiss()
    }

    override fun handleError(error: Throwable, option: Any?) {
        try {
            if (requireContext().applicationContext.isConnected().not()) {
                showSnackBar(R.string.no_internet)
            } else {
                errorHandler.handle(error, this, option, baseActivity as Context)
            }
        } catch (e: Exception) {
            Timber.d("Unable to handle unknown error")
        }
    }

    override fun showSnackBar(resId: Int) {
        Snackbar.make(viewDataBinding.root, resId, Snackbar.LENGTH_LONG).show()
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(viewDataBinding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun notifyError(
        message: String,
        throwable: Throwable?,
        option: Any?,
        workingType: ErrorNotification.AfterNotify
    ) {
        when (workingType) {
            ErrorNotification.AfterNotify.LOGOUT -> {
                handleLogout()
            }
            ErrorNotification.AfterNotify.SYSTEM_ERROR -> {
                showSnackBar(R.string.system_error)
            }
            ErrorNotification.AfterNotify.SCREEN_BACK -> {
                //do nothing
            }
            else -> {
                showSnackBar(message)
            }
        }
    }

    fun isAuthenticated(): Boolean {
        return hawkHelper.getToken().isNotBlank()
    }

    interface FragmentNavigation {
        fun pushFragment(fragment: Fragment, sharedElementList: List<Pair<View, String>>? = null)
    }

    private fun handleLogout() {
        hawkHelper.clearAll()
        val intent = AuthenticationActivity.callingIntent(requireContext(), AuthenticationType.LOGIN.type)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        (getBaseActivity() as MainActivity).overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
        (getBaseActivity() as MainActivity).finish()
    }

}