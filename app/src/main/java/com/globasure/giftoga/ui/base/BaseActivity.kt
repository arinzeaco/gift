package com.globasure.giftoga.ui.base

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
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
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject
import timber.log.Timber

abstract class BaseActivity<B : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(),
    BaseActivityView, ErrorNotification {

    protected lateinit var viewDataBinding: B
    private lateinit var viewModel: V
    private lateinit var progressDialog: GiftOgaProgressDialog

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): V

    abstract fun getBindingVariable(): Int

    open fun getColorStatusBar(): Int? = null

    abstract fun initView()

    private var unBinder: Unbinder? = null

    @Inject
    lateinit var hawkHelper: HawkHelper

    @Inject
    lateinit var errorHandler: ErrorHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performBinding()

        unBinder = ButterKnife.bind(this)
        this.progressDialog = GiftOgaProgressDialog(this)

        getColorStatusBar()?.let { setStatusBarColor(it) }
    }

    @Suppress("RedundantOverride")
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initView()
    }

    private fun performBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = getViewModel()
        viewDataBinding.setVariable(getBindingVariable(), viewModel)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.executePendingBindings()
    }

    override fun onDestroy() {
        super.onDestroy()
        unBinder?.unbind()
    }

    fun setStatusBarColor(color: Int) {
        @Suppress("DEPRECATION")
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val statusBarColor = getString(color)
        window.statusBarColor = Color.parseColor(statusBarColor)
    }

    fun isAuthenticated(): Boolean {
        return hawkHelper.getToken().isNotBlank()
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

        } catch (e: Exception) {
            Timber.d("Unable to handle this unknown error")
        }
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
                showSnackBar(getString(R.string.system_error))
            }
            else -> {
                showSnackBar(message)
            }
        }
    }

    override fun showSnackBar(resId: Int) {
        Snackbar.make(viewDataBinding.root, resId, Snackbar.LENGTH_LONG).show()
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(viewDataBinding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun handleLogout() {
        hawkHelper.clearAll()
        val intent = AuthenticationActivity.callingIntent(this, AuthenticationType.LOGIN.type)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        (this as MainActivity).overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
        (this as MainActivity).finish()
    }

}