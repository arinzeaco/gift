package com.globasure.giftoga.ui.dialog.banks

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.ItemAvailableBankBinding
import com.globasure.giftoga.network.response.BankData

class DialogListBankAdapter(val context: Context, countries: List<BankData>) :
    RecyclerView.Adapter<DialogListBankAdapter.BankViewHolder>() {

    val list = countries as MutableList
    var onItemClick: ((bank: BankData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = DataBindingUtil.inflate<ItemAvailableBankBinding>(
            layoutInflater,
            R.layout.item_available_bank,
            parent,
            false
        )
        return BankViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        holder.bind(list[position])
        holder.binding.contentLayout.setOnClickListener { onItemClick?.invoke(list[position]) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class BankViewHolder(val binding: ItemAvailableBankBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BankData) {
            binding.bankAccount.text = item.name
        }
    }
}