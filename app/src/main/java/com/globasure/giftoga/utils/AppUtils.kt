package com.globasure.giftoga.utils

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.Constant
import com.globasure.giftoga.network.response.Giftcard
import com.globasure.giftoga.network.response.Transaction
import com.globasure.giftoga.utils.extension.formatCurrency
import com.globasure.giftoga.utils.extension.formatDecimal
import com.globasure.giftoga.utils.extension.gone
import com.globasure.giftoga.utils.extension.runIfNotNull
import com.globasure.giftoga.utils.extension.visible
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_payment_failed.view.ok_button
import kotlinx.android.synthetic.main.dialog_payment_successful.view.count_down_text
import kotlinx.android.synthetic.main.dialog_payment_successful.view.messageTv
import kotlinx.android.synthetic.main.dialog_transaction_item.view.card_quantity
import kotlinx.android.synthetic.main.dialog_transaction_item.view.qr_code
import kotlinx.android.synthetic.main.dialog_transaction_item.view.transaction_close_btn
import kotlinx.android.synthetic.main.dialog_transaction_item.view.transaction_cost
import kotlinx.android.synthetic.main.dialog_transaction_item.view.transaction_id
import kotlinx.android.synthetic.main.dialog_transaction_item.view.transaction_sub_title
import kotlinx.android.synthetic.main.dialog_view_gift_card.view.codeToggle
import kotlinx.android.synthetic.main.dialog_view_gift_card.view.code_Edt
import kotlinx.android.synthetic.main.dialog_view_gift_card.view.copy_gift_code_btn
import kotlinx.android.synthetic.main.dialog_view_gift_card.view.show_qr_code
import kotlinx.android.synthetic.main.show_qr_code.view.copy_code_btn
import kotlinx.android.synthetic.main.show_qr_code.view.qr_image
import kotlinx.android.synthetic.main.show_qr_code.view.switch_to_code_btn
import kotlinx.android.synthetic.main.view_code_dialog.view.closeBtn


class AppUtils {
    @SuppressLint("InflateParams", "CheckResult")
    fun viewCodeDialog(context: Context, pin: String, giftcard: Giftcard?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        // set the custom layout
        val customLayout: View = LayoutInflater.from(context).inflate(R.layout.dialog_view_gift_card, null)
        builder.setView(customLayout)
        customLayout.code_Edt.text = giftcard?.pin ?: pin
        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        var isOn = false
        customLayout.code_Edt.text = context.getString(R.string.code_hidden)

        customLayout.codeToggle.setOnClickListener {
            if (isOn) {
                isOn = false
                customLayout.codeToggle.setImageResource(R.drawable.ic_visibility_off_black_24dp)
                customLayout.code_Edt.text = giftcard?.pin ?: pin
            } else {
                isOn = true
                customLayout.codeToggle.setImageResource(R.drawable.ic_visibility_black_24dp)
                customLayout.code_Edt.text = context.getString(R.string.code_hidden)
            }
        }

        customLayout.show_qr_code.setOnClickListener {
            giftcard.runIfNotNull { showQrCode(it, context) }
        }

        customLayout.copy_gift_code_btn.setOnClickListener {
            copyToClipboard(context, "GiftCard Code", giftcard?.pin ?: pin)
        }

        customLayout.closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showQrCode(giftcard: Giftcard, context: Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val customLayout: View = LayoutInflater.from(context).inflate(
            R.layout.show_qr_code,
            null
        )
        builder.setView(customLayout)
        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        Picasso.get().load(Constant.BASE_URL_IMAGE + giftcard.qrImage).into(customLayout.qr_image)

        customLayout.copy_code_btn.setOnClickListener {
            copyToClipboard(context, "GiftCard Code", giftcard.pin)
        }

        customLayout.switch_to_code_btn.setOnClickListener {
            dialog.dismiss()
        }

        customLayout.closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    @SuppressLint("InflateParams", "CheckResult")
    fun viewTransactionDialog(context: Context, transaction: Transaction) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        // set the custom layout
        val customLayout: View = LayoutInflater.from(context).inflate(R.layout.dialog_transaction_item, null)
        builder.setView(customLayout)
        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        customLayout.transaction_sub_title.text =
            String.format(context.getString(R.string.transaction_successful_description), transaction.title)
        if (transaction.data?.qrImage != null) {
            customLayout.qr_code.visible()
            Picasso.get().load(Constant.BASE_URL_IMAGE + transaction.data.qrImage).into(customLayout.qr_code)
        } else {
            customLayout.qr_code.gone()
        }
        customLayout.transaction_cost.text =
            transaction.amount.formatDecimal().formatCurrency(true, transaction.currencyName)
        customLayout.card_quantity.text =
            (transaction.total.toFloat() / transaction.amount.toFloat()).toInt().toString()
        customLayout.transaction_id.text = transaction.orderId
        customLayout.transaction_close_btn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun successDialog(context: Context, message: String, moveToHome: () -> Unit?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val customLayout: View = LayoutInflater.from(context).inflate(R.layout.dialog_payment_successful, null)
        builder.setView(customLayout)
        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customLayout.messageTv.text = message
        val timer = object : CountDownTimer(5 * 1000, 1000) {

            override fun onFinish() {
                dialog.dismiss()
                moveToHome()
            }

            override fun onTick(remainTime: Long) {
                val seconds = (remainTime / 1000) % 60

                val secondString = if (seconds < 10) {
                    "0$seconds"
                } else {
                    "$seconds"
                }

                customLayout.count_down_text.visible()
                customLayout.count_down_text.text =
                    String.format(context.getString(R.string.redirecting_home_page), secondString)
            }
        }
        timer.start()

        dialog.show()
    }

    fun failedDialog(context: Context, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val customLayout: View = LayoutInflater.from(context).inflate(
            R.layout.dialog_payment_failed,
            null
        )
        builder.setView(customLayout)
        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        customLayout.messageTv.text = message
        customLayout.ok_button.setOnClickListener {
            dialog.dismiss()

        }
        dialog.show()

    }

    @Suppress("unused")
    private fun textToImageEncode(context: Context, value: String?): Bitmap? {
        val bitMatrix: BitMatrix = try {
            MultiFormatWriter().encode(value, BarcodeFormat.QR_CODE, 200, 200, null)
        } catch (ex: IllegalArgumentException) {
            return null
        }
        val bitMatrixWidth = bitMatrix.width
        val bitMatrixHeight = bitMatrix.height
        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)
        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth
            for (x in 0 until bitMatrixWidth) {
                pixels[offset + x] =
                    if (bitMatrix[x, y]) {
                        ContextCompat.getColor(context, R.color.splash_background)
                    } else {
                        ContextCompat.getColor(context, R.color.white)
                    }
            }
        }
        @Suppress("DEPRECATION")
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
        bitmap.setPixels(pixels, 0, 200, 0, 0, bitMatrixWidth, bitMatrixHeight)

        return bitmap
    }

    @Suppress("SameParameterValue")
    private fun copyToClipboard(context: Context, title: String, value: String) {
        val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText(title, value)
        clipboard!!.setPrimaryClip(clip)
        Toast.makeText(context, context.getString(R.string.copy_to_clip_board), Toast.LENGTH_LONG).show()
    }
}