package com.globasure.giftoga.ui.screen.settings_tab.verification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.AccountType
import com.globasure.giftoga.databinding.VerificationFragBinding
import com.globasure.giftoga.network.response.BankData
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.dialog.banks.DialogListBank
import com.globasure.giftoga.utils.FileUtils
import com.globasure.giftoga.utils.extension.gone
import com.globasure.giftoga.utils.extension.visible
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
class VerificationFragment : BaseFragment<VerificationFragBinding, VerificationViewModel>(), VerificationFragView {

    private lateinit var bankDialog: DialogListBank
    private lateinit var excelFile: MultipartBody.Part
    private var allowToSubmit: Boolean = false

    private val verificationViewModel: VerificationViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.verification_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): VerificationViewModel = verificationViewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getPageTitle(): String = getString(R.string.verification)

    override fun setToolBar(view: View) {
        page_title.text = screenTitle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
        verificationViewModel.getBankList()
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)
    }

    override fun initView(view: View) {
        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }

        if (hawkHelper.getUserDetail()?.accountVerified == true) {
            allowToSubmit = false
            viewDataBinding.verifiedTxt.visible()
            viewDataBinding.bvnEd.isEnabled = false
            viewDataBinding.tapToUploadTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
            viewDataBinding.sendBtn.setBackgroundResource(R.drawable.button_round_light_blue)
        } else {
            allowToSubmit = true
            viewDataBinding.verifiedTxt.gone()
            viewDataBinding.bvnEd.isEnabled = true
            viewDataBinding.tapToUploadTv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.profile_edit_text_color_bold
                )
            )
            viewDataBinding.sendBtn.setBackgroundResource(R.drawable.button_blue_filled)
        }

        viewDataBinding.tapToUploadTv.setOnClickListener {
            if (allowToSubmit) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                    )
                } else {
                    selectFromFile()
                }
            }
        }
    }

    private fun selectFromFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/*"
        startActivityForResult(intent, PDF_SELECTION_CODE)
        Toast.makeText(context, "Please select JPG, PND or PDF!", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PDF_SELECTION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val returnUri = data.data

            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            val file = File(FileUtils().getPath(requireContext(), returnUri!!))
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            excelFile = MultipartBody.Part.createFormData("document_file", file.name, requestFile)
            viewDataBinding.uploadFileName.text = String.format(getString(R.string.upload_document_title), file.name)
            viewDataBinding.uploadFileName.visible()
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.send_btn)
    fun onSendInformationClick() {
        if (allowToSubmit) {
            verificationViewModel.verifyDocument(
                isCustomer = hawkHelper.getUserDetail()?.accountType == AccountType.PERSONAL.type,
                documentFile = excelFile,
                documentType = DEFAULT_DOCUMENT_TYPE,
                bvn = viewDataBinding.bvnEd.text.toString(),
                rcNumber = ""
            )
        }
    }

    override fun verifySuccess() {

    }

    override fun getBanksSuccess(list: List<BankData>) {
        bankDialog = DialogListBank(list as ArrayList<BankData>)
        viewDataBinding.selectBankTv.setOnClickListener {
            handleChooseBankClick()
        }
    }

    private fun handleChooseBankClick() {
        bankDialog.itemClick = {
            viewDataBinding.selectBankTv.text = it.name
            bankDialog.dismiss()
        }

        bankDialog.onCloseClick = {
            bankDialog.dismiss()
        }

        bankDialog.show(this.childFragmentManager, TAG)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectFromFile()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    companion object {
        private const val TAG = "VerificationFragment"
        private const val PDF_SELECTION_CODE = 99
        private const val DEFAULT_DOCUMENT_TYPE = "passport"
        const val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 774

        fun newInstance(): VerificationFragment {
            return VerificationFragment()
        }
    }
}
