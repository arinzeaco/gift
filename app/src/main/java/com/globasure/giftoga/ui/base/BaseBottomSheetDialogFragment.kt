package com.globasure.giftoga.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.globasure.giftoga.R
import com.globasure.giftoga.common.ErrorHandler
import com.globasure.giftoga.common.ErrorNotification
import com.globasure.giftoga.ui.custom.GiftOgaProgressDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import javax.inject.Inject

abstract class BaseBottomSheetDialogFragment<B : ViewDataBinding, V : BaseViewModel<*>> : BaseView,
    BottomSheetDialogFragment(), ErrorNotification {

    private var baseActivity: BaseActivity<*, *>? = null

    private lateinit var viewModel: V

    private lateinit var progressDialog: GiftOgaProgressDialog

    @Inject
    lateinit var errorHandler: ErrorHandler

    private var mErrorPopup: AlertDialog? = null

    abstract fun getViewModel(): V

    abstract fun getBindingVariable(): Int

    abstract fun getEnableDismissClickOutside(): Boolean

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initView(view: View)

    private var unBinder: Unbinder? = null

    protected lateinit var viewDataBinding: B

    protected fun getBaseActivity(): BaseActivity<*, *>? = baseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            this.baseActivity = context
        }
    }

    override fun onDetach() {
        this.baseActivity = null
        super.onDetach()
    }

    open fun getStyle() {
        setStyle(
            DialogFragment.STYLE_NORMAL,
            R.style.transparent_bottom_sheet_dialog_theme
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewModel = this.getViewModel()

        this.progressDialog = GiftOgaProgressDialog(requireContext())

        this.getStyle()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        // ButterKnife
        unBinder = ButterKnife.bind(this, this.viewDataBinding.root)
        return this.viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.viewDataBinding.setVariable(this.getBindingVariable(), this.viewModel)
        this.viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        this.viewDataBinding.executePendingBindings()
        dialog?.setCanceledOnTouchOutside(getEnableDismissClickOutside())
        initView(view)
    }

    override fun showProgressDialog(isShow: Boolean) {
        if (isShow) {
            showProgress()
        } else {
            dismissProgress()
        }
    }

    private fun showProgress() {
        if (!progressDialog.isShowing) progressDialog.show()
    }

    private fun dismissProgress() {
        if (progressDialog.isShowing) progressDialog.dismiss()
    }

    override fun handleError(error: Throwable, option: Any?) {
        try {
            errorHandler.handle(error, this, option, baseActivity as Context)
        } catch (e: Exception) {
            Timber.d("Unable to handle error")
        }
    }

    override fun logout() {}

    override fun screenBack() {}

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

            }
            ErrorNotification.AfterNotify.SYSTEM_ERROR -> {

            }
            else -> {
            }
        }
    }

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
    }
}