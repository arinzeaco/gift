package com.globasure.giftoga.ui.screen.single_giftcard_item

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.globasure.giftoga.R

class SelectedAmountAdapter(arrayList: List<ItemAmount>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list = arrayList
    var onItemClick: ((itemAmount: ItemAmount) -> Unit)? = null

    class AmountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amountTxt: TextView = view.findViewById(R.id.select_amount_label)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): AmountViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_select_amount, parent, false)

        return AmountViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(updatedItems: List<ItemAmount>) {
        list = updatedItems
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as AmountViewHolder

        val amount = list[position]
        holder.amountTxt.text = "${amount.currency} ${amount.value}"
        if (amount.isSelected) {
            holder.amountTxt.setBackgroundResource(R.drawable.button_round_grey_selected)
            holder.amountTxt.setTextColor(ContextCompat.getColor(holder.amountTxt.context, R.color.white))
        } else {
            holder.amountTxt.setBackgroundResource(R.drawable.button_round_grey)
            holder.amountTxt.setTextColor(ContextCompat.getColor(holder.amountTxt.context, R.color.profile_text_color))
        }

        holder.amountTxt.setOnClickListener { onItemClick?.invoke(amount) }
    }
}
