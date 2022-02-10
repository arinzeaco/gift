package com.globasure.giftoga.ui.dialog.country

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.ItemCountryBinding
import com.globasure.giftoga.network.response.CountryData

class DialogListCountryAdapter(val context: Context, countries: List<CountryData>) :
    RecyclerView.Adapter<DialogListCountryAdapter.CountryViewHolder>() {

    val list = countries as MutableList
    var onItemClick: ((countryName: CountryData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = DataBindingUtil.inflate<ItemCountryBinding>(
            layoutInflater,
            R.layout.item_country,
            parent,
            false
        )
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(list[position])
        holder.binding.contentLayout.setOnClickListener { onItemClick?.invoke(list[position]) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CountryViewHolder(val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CountryData) {
            binding.logo.setImageResource(R.drawable.ic_arrow_up)
            binding.name.text = item.name
        }
    }
}