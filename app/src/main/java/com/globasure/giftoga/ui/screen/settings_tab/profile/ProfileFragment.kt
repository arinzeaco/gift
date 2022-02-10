package com.globasure.giftoga.ui.screen.settings_tab.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.ProfileFragBinding
import com.globasure.giftoga.network.request.UpdateProfileRequest
import com.globasure.giftoga.ui.base.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import java.io.*
import java.text.SimpleDateFormat
import java.util.Date
import kotlinx.android.synthetic.main.dialog_change_avatar.view.choose_photo
import kotlinx.android.synthetic.main.dialog_change_avatar.view.take_photo
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileFragBinding, ProfileFragViewModel>(), ProfileFragView {

    private var capturePhotoUri: Uri? = null
    private var photoFile: File? = null
    private var cameraCapturedPhotoPath: String? = null
    private var userAvatar: MultipartBody.Part? = null

    private val profileFragViewModel: ProfileFragViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.profile_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): ProfileFragViewModel = profileFragViewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getPageTitle(): String = getString(R.string.profile_settings)

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

        Picasso.get().load(hawkHelper.getUserDetail()?.profileImage).into(viewDataBinding.profileAvatar)
        viewDataBinding.profileFirstname.setText(hawkHelper.getUserDetail()?.firstName)
        viewDataBinding.profileLastname.setText(hawkHelper.getUserDetail()?.lastName)
        viewDataBinding.profileEmail.setText(hawkHelper.getUserDetail()?.email)
        viewDataBinding.profilePhoneNumber.setText(hawkHelper.getUserDetail()?.mobile)

        viewDataBinding.saveInformationBtn.setOnClickListener {
            val updateProfileRequest = UpdateProfileRequest(
                firstName = viewDataBinding.profileFirstname.text.toString(),
                lastName = viewDataBinding.profileLastname.text.toString(),
                email = viewDataBinding.profileEmail.text.toString(),
                country = DEFAULT_COUNTRY,
                mobile = viewDataBinding.profilePhoneNumber.text.toString()
            )
            profileFragViewModel.updateProfileDetail(updateProfileRequest)
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.profileAvatar)
    fun openChangeImageClick() {
        openChangeImage(requireContext(), { chooseFromCamera() }, { chooseFromGallery() })
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)
    }

    override fun updateProfileSuccess() {
        showSnackBar(R.string.update_profile_success)
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1500)
            (getBaseActivity() as MainActivity).onBackPressed()
        }
    }

    override fun updateAvatarSuccess() {
        showSnackBar(R.string.update_profile_success)
    }

    @SuppressLint("InflateParams")
    fun openChangeImage(context: Context, chooseFromCamera: () -> Unit, chooseFromGallery: () -> Unit) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.transparent_bottom_sheet_dialog_theme)
        val customLayout: View = LayoutInflater.from(context).inflate(R.layout.dialog_change_avatar, null)
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(customLayout)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        customLayout.choose_photo.setOnClickListener {
            chooseFromGallery()
            bottomSheetDialog.dismiss()
        }
        customLayout.take_photo.setOnClickListener {
            chooseFromCamera()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun chooseFromGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
            )
        } else {
            selectFromGallery()
        }
    }

    private fun selectFromGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE)
    }

    private fun chooseFromCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_PERMISSION)
        } else {
            selectFromCamera()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun selectFromCamera() {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val chooserIntent = Intent.createChooser(captureIntent, "Camera")

        if (captureIntent.resolveActivity(requireContext().packageManager) != null) {
            photoFile = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (photoFile != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(), "com.globasure.giftoga.provider",
                    photoFile!!
                )
                capturePhotoUri = photoURI
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureIntent)
                startActivityForResult(chooserIntent, CAMERA)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        cameraCapturedPhotoPath = image.absolutePath
        return image
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                CropImage.activity(data!!.data)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setActivityTitle("")
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setCropMenuCropButtonTitle("Done")
                    .start(requireContext(), this)
            } else if (requestCode == CAMERA) {
                if (capturePhotoUri != null) {
                    CropImage.activity(capturePhotoUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle("")
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setCropMenuCropButtonTitle("Done")
                        .start(requireContext(), this)
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                try {
                    val croppedBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, result.uri)
                    val file = convertBitmapToFile(requireContext(), "avatar.png", croppedBitmap)
                    val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
                    userAvatar = MultipartBody.Part.createFormData("avatar", file.name, requestFile)
                    viewDataBinding.profileAvatar.setImageBitmap(croppedBitmap)

                    profileFragViewModel.updateAvatar(userAvatar)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Convert the reduced size image into file for uploading
     */
    private fun convertBitmapToFile(
        context: Context,
        @Suppress("SameParameterValue") filename: String,
        photo: Bitmap
    ): File {
        val file = File(context.cacheDir, filename)
        file.createNewFile()
        val bos = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bitmapData = bos.toByteArray()
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos!!.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectFromGallery()
            }
        } else if (requestCode == CAMERA_REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectFromCamera()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    companion object {
        const val WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 774
        const val CAMERA_REQUEST_PERMISSION = 775
        const val PICK_IMAGE = 100
        const val CAMERA = 200
        const val DEFAULT_COUNTRY = "Nigeria"

        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}
