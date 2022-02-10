package com.globasure.giftoga.ui.screen.bulk_purchase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.BulkPurchaseFragBinding
import com.globasure.giftoga.network.response.Merchant
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.single_giftcard_item.SingleGiftItemFragment
import com.globasure.giftoga.utils.FileUtils
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber

@Suppress("DEPRECATION")
@AndroidEntryPoint
class BulkPurchaseFragment : BaseFragment<BulkPurchaseFragBinding, BulkPurchaseViewModel>(), BulkPurchaseView {

    private val bulkPurchaseViewModel: BulkPurchaseViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.bulk_purchase_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): BulkPurchaseViewModel = bulkPurchaseViewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getPageTitle(): String = SCREEN_TITLE

    override fun setToolBar(view: View) {
        page_title.text = screenTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun initView(view: View) {
        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }

        merchant = Gson().fromJson(arguments?.getString(MERCHANT), Merchant::class.java)
        viewDataBinding.sendBtnToSingle.setOnClickListener {
            val fragment = SingleGiftItemFragment.newInstance(
                merchant
            )
            fragmentNavigation.pushFragment(fragment)
        }
        viewDataBinding.imageIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/*"
            startActivityForResult(intent, PDF_SELECTION_CODE)
            Toast.makeText(context, "Please select an excel file!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PDF_SELECTION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val returnUri = data.data

            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            val file = File(FileUtils().getPath(requireContext(), returnUri!!))
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val requestBody = MultipartBody.Part.createFormData("excelFile", file.name, requestFile)
            bulkPurchaseViewModel.validateBulkFile(merchant.id, requestBody)
        }
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)
    }

    override fun uploadFile(mess: String) {

    }

    override fun validateBulkFileSuccess() {
        showSnackBar(getString(R.string.upload_bulk_file_done))
    }

    companion object {
        const val SCREEN_TITLE = "Add Recipients"
        private const val MERCHANT = "merchant"
        private const val PDF_SELECTION_CODE = 99
        lateinit var merchant: Merchant
        fun newInstance(merchant: Merchant): BulkPurchaseFragment {
            val fragment = BulkPurchaseFragment()
            val gson = Gson()
            val args = Bundle()
            args.putString(MERCHANT, gson.toJson(merchant))
            fragment.arguments = args
            return fragment
        }
    }
}