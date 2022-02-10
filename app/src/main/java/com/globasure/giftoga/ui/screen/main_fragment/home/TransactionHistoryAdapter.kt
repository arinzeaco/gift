package com.globasure.giftoga.ui.screen.main_fragment.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.TransactionType
import com.globasure.giftoga.network.response.Transaction
import com.globasure.giftoga.utils.AppUtils
import com.globasure.giftoga.utils.CalendarUtils
import com.globasure.giftoga.utils.extension.gone
import com.globasure.giftoga.utils.extension.toPixel
import com.globasure.giftoga.utils.extension.visible

class TransactionHistoryAdapter(
    var context: Context?,
    arrayList: ArrayList<Transaction>,
    private val showMargin: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list = arrayList

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val summaryTitle: TextView = view.findViewById(R.id.summary_title)
        var transactionDate: TextView = view.findViewById(R.id.transaction_date)
        var transactionType: ImageView = view.findViewById(R.id.arrow_image)
        var transactionTypeBackGround: ImageView = view.findViewById(R.id.type_background)
        var divider: ImageView = view.findViewById(R.id.divider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_transaction, parent, false)

        context = itemView.context
        return TransactionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as TransactionViewHolder

        val transaction = list[position]
        holder.itemView.setOnClickListener {
            AppUtils().viewTransactionDialog(context!!, transaction)
        }

        holder.summaryTitle.text = transaction.title
        holder.transactionDate.text = CalendarUtils.convertTransactionDateTime(transaction.createdDate)

        when (transaction.type) {
            TransactionType.SALES.type,
            TransactionType.REDEEM.type,
            TransactionType.DEPOSIT.type -> {
                holder.transactionTypeBackGround.setImageResource(R.color.background_history_icon_blue)
                holder.transactionType.setImageResource(R.drawable.ic_arrow_down)
            }

            TransactionType.VERIFICATION.type,
            TransactionType.CHARGE.type -> {
                holder.transactionTypeBackGround.setImageResource(R.color.background_history_icon_green)
                holder.transactionType.setImageResource(R.drawable.ic_arrow_up)
            }
        }

        val layoutParams: ViewGroup.MarginLayoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (position == list.size - 1) {
            holder.divider.gone()
            if (showMargin) {
                layoutParams.bottomMargin = 90.toPixel().toInt()
            } else {
                layoutParams.bottomMargin = 0.toPixel().toInt()
            }
        } else {
            holder.divider.visible()
            layoutParams.bottomMargin = 0.toPixel().toInt()
        }
        holder.itemView.requestLayout()
    }
}
