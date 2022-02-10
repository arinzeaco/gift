package com.globasure.giftoga.ui.screen.settings_tab.debit_card

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.globasure.giftoga.R
import com.globasure.giftoga.network.response.CardData
import com.globasure.giftoga.utils.extension.gone
import com.globasure.giftoga.utils.extension.visible

class DebitCardsAdapter(
    var context: Context?,
    arrayList: ArrayList<CardData>,
    private val allowToChooseCard: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list = arrayList
    lateinit var onOptionClick: ((cardData: CardData) -> Unit)
    lateinit var onItemClick: ((cardData: CardData) -> Unit)

    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardBrand: TextView = view.findViewById(R.id.card_brand)
        var cardNumber: TextView = view.findViewById(R.id.card_number)
        var optionButton: ImageView = view.findViewById(R.id.option_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_debit_card, parent, false)

        context = itemView.context
        return CardViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as CardViewHolder
        val cardData = list[position]

        if (allowToChooseCard) {
            holder.optionButton.gone()
            holder.itemView.setOnClickListener {
                onItemClick.invoke(cardData)
            }
        } else {
            holder.optionButton.visible()
            holder.optionButton.setOnClickListener {
                onOptionClick.invoke(cardData)
            }
        }
        holder.cardBrand.text = cardData.cardBrand
        holder.cardNumber.text = cardData.cardNumber
    }
}