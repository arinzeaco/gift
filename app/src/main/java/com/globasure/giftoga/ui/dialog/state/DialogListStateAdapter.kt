package com.globasure.giftoga.ui.dialog.state

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.ItemStateBinding
import com.globasure.giftoga.network.response.StateData

class DialogListStateAdapter(val context: Context, states: List<StateData>) :
    RecyclerView.Adapter<DialogListStateAdapter.StateViewHolder>() {

    val list = states as MutableList
    var onItemClick: ((countryName: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = DataBindingUtil.inflate<ItemStateBinding>(
            layoutInflater,
            R.layout.item_state,
            parent,
            false
        )
        return StateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        holder.bind(list[position])
        holder.binding.contentLayout.setOnClickListener { onItemClick?.invoke(list[position].name) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class StateViewHolder(val binding: ItemStateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StateData) {
            binding.name.text = item.name
        }
    }
}