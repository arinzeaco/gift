package com.globasure.giftoga.ui.screen.main_fragment.my_cards

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.Constant
import com.globasure.giftoga.network.response.Giftcard
import com.globasure.giftoga.ui.custom.CustomOutline
import com.globasure.giftoga.utils.AppUtils
import com.globasure.giftoga.utils.extension.gone
import com.globasure.giftoga.utils.extension.toPixel
import com.globasure.giftoga.utils.extension.visible
import com.squareup.picasso.Picasso
import dagger.hilt.android.internal.managers.ViewComponentManager

class MyGiftCardAdapter(
    var context: Context?,
    arrayList: ArrayList<Giftcard>
) : RecyclerView.Adapter<MyGiftCardAdapter.TransactionViewHolder>() {
    var list = arrayList

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val giftCardCostTv: TextView = view.findViewById(R.id.gift_card_costTv)
        var giftCardTv: TextView = view.findViewById(R.id.gift_cardTv)
        var giftCardImv: ImageView = view.findViewById(R.id.gift_cardImv)
        var showCodeBtn: Button = view.findViewById(R.id.show_code_btn)
        var sendBtn: Button = view.findViewById(R.id.send_btn)
        var rootView: ConstraintLayout = view.findViewById(R.id.item_gift_card_root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_gift_card, parent, false)

        context = itemView.context
        return TransactionViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.rootView.outlineProvider = CustomOutline(0.5f)
        val giftCard = list[position]

        if (giftCard.image.contentEquals(ADD_GIFT_CARD)) {
            holder.giftCardTv.text = context!!.getString(R.string.buy_new_gift_card)
            holder.giftCardCostTv.text = context!!.getString(R.string.purchase_gift_cards_instantly_from_the_store)
            holder.sendBtn.gone()
            holder.showCodeBtn.gone()
            holder.giftCardImv.setBackgroundResource(R.drawable.white_circle)

            val layoutParams: ViewGroup.LayoutParams = holder.giftCardImv.layoutParams
            layoutParams.width = 100.toPixel().toInt()
            layoutParams.height = 100.toPixel().toInt()
            holder.giftCardImv.layoutParams = layoutParams
            holder.giftCardImv.setImageResource(R.drawable.ic_white_cross)
            holder.rootView.setBackgroundColor(Color.parseColor(GREY_COLOR))
            ViewCompat.setElevation(holder.rootView, 0f)
            holder.rootView.setOnClickListener {
                ((context as ViewComponentManager.FragmentContextWrapper).fragment as MyCardsFragment).showBuyCardsScreen()
            }
        } else {
            holder.giftCardTv.text = giftCard.name
            holder.giftCardCostTv.text = giftCard.amount
            holder.sendBtn.visible()
            holder.showCodeBtn.visible()
            holder.giftCardImv.setBackgroundResource(R.drawable.rounded_transparent)

            val layoutParams: ViewGroup.LayoutParams = holder.giftCardImv.layoutParams
            layoutParams.width = 180.toPixel().toInt()
            layoutParams.height = 150.toPixel().toInt()
            holder.giftCardImv.layoutParams = layoutParams

            Picasso.get().load(Constant.BASE_URL_IMAGE + giftCard.image).into(holder.giftCardImv)
            holder.rootView.setBackgroundColor(Color.WHITE)
            ViewCompat.setElevation(holder.rootView, 3f)

            holder.rootView.setOnClickListener {
                //do nothing
            }
        }

        holder.showCodeBtn.setOnClickListener {
            if (!giftCard.image.contentEquals(ADD_GIFT_CARD)) {
                AppUtils().viewCodeDialog(context!!, giftCard.pin, giftCard)
            }
        }

        if (giftCard.friendName != null) {
            holder.showCodeBtn.gone()
        } else {
            holder.showCodeBtn.visible()
        }

        holder.sendBtn.setOnClickListener {
            ((context as ViewComponentManager.FragmentContextWrapper).fragment as MyCardsFragment).sendMyGiftCards(
                giftCard
            )
        }

        val layoutParams: ViewGroup.MarginLayoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (position == (itemCount - 1)) {
            layoutParams.topMargin = 5.toPixel().toInt()
            layoutParams.bottomMargin = 90.toPixel().toInt()
        } else {
            layoutParams.topMargin = 5.toPixel().toInt()
            layoutParams.bottomMargin = 10.toPixel().toInt()
            if (position == 0) {
                layoutParams.topMargin = 20.toPixel().toInt()
            }
        }
        holder.itemView.requestLayout()
    }

    companion object {
        private const val ADD_GIFT_CARD = "Add Gift Card"
        private const val GREY_COLOR = "#80EFEFEF"
    }
}