package com.globasure.giftoga.ui.screen.main_fragment.buy_cards

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.globasure.giftoga.R
import com.globasure.giftoga.network.response.Merchant
import com.globasure.giftoga.ui.custom.CustomOutline
import com.squareup.picasso.Picasso
import dagger.hilt.android.internal.managers.ViewComponentManager

class BuyCardsAdapter(var context: Context?, arrayList: ArrayList<Merchant>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    var list = arrayList
    var originalList: ArrayList<Merchant> = ArrayList()

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val giftCardCostTv: TextView = view.findViewById(R.id.gift_card_costTv)
        var giftCardTv: TextView = view.findViewById(R.id.gift_cardTv)
        var giftCardImv: ImageView = view.findViewById(R.id.gift_cardImv)
        var buyNowBtn: Button = view.findViewById(R.id.buy_now_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_buy_gift_card, parent, false)

        context = itemView.context
        return TransactionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOriginalList(data: List<Merchant>?) {
        originalList = data as ArrayList<Merchant>
    }

    fun setMerchants(data: List<Merchant>?) {
        list = data as ArrayList<Merchant>
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as TransactionViewHolder
        if (itemCount > 0) {
            holder.itemView.outlineProvider = CustomOutline()
            Picasso.get().load(list[position].iconUrl).into(holder.giftCardImv)

            holder.buyNowBtn.setOnClickListener {
                ((context as ViewComponentManager.FragmentContextWrapper).fragment as BuyCardsFragment)
                    .moveToSingleItemFragment(list[position])
            }

            holder.giftCardTv.text = list[position].cardName
            holder.giftCardCostTv.text = "From ${list[position].currencyName} ${list[position].minCardValue}"
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val results = FilterResults()
                results.values = filterMerchants(charSequence)

                return results
            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
                setMerchants(filterResults?.values as ArrayList<Merchant>?)
            }

            private fun filterMerchants(constraint: CharSequence): ArrayList<Merchant> {
                var filteredResults = ArrayList<Merchant>()
                if (constraint.isEmpty()) {
                    filteredResults = originalList
                } else {
                    for (item in originalList) {
                        if (item.cardName.contains(constraint, true)) {
                            filteredResults.add(item)
                        }
                    }
                }
                return filteredResults
            }
        }
    }
}