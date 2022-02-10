package com.globasure.giftoga.ui.screen.single_giftcard_item

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.BuySendType
import com.globasure.giftoga.databinding.SingleGiftItemFragBinding
import com.globasure.giftoga.network.request.SendToFriendRequest
import com.globasure.giftoga.network.response.Merchant
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.complete_order.CompleteOrderFragment
import com.globasure.giftoga.ui.screen.send_to_friend.SendToFriendFragment
import com.globasure.giftoga.utils.extension.gone
import com.globasure.giftoga.utils.extension.hideKeyBoard
import com.globasure.giftoga.utils.extension.visible
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import timber.log.Timber


@AndroidEntryPoint
class SingleGiftItemFragment : BaseFragment<SingleGiftItemFragBinding, SingleGiftItemViewModel>(),
    SingleGiftItemFragView {

    private lateinit var merchant: Merchant

    private val singleGiftItemViewModel: SingleGiftItemViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.single_gift_item_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): SingleGiftItemViewModel = singleGiftItemViewModel

    override fun getPageTitle(): String = SCREEN_TITLE

    private lateinit var amountAdapter: SelectedAmountAdapter

    private var isValidAmount: Boolean = true

    private var selectedMoney: String? = null

    private val listAmount = mutableListOf<ItemAmount>()

    override fun setToolBar(view: View) {
        //do nothing
    }

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)
    }

    private fun initAmountAdapter(merchant: Merchant) {
        if (merchant.isFixedAmount) {
            viewDataBinding.amountEdt.gone()
            viewDataBinding.amountLabel.gone()
        } else {
            viewDataBinding.amountEdt.visible()
            viewDataBinding.amountLabel.visible()
        }

        if (merchant.hasDenomination) {
            listAmount.clear()
            viewDataBinding.selectAmountRv.visible()
            viewDataBinding.selectAmountLabel.visible()
            merchant.denomination.forEach {
                listAmount.add(ItemAmount(merchant.currencyName, it, false))
            }
        } else {
            viewDataBinding.selectAmountRv.gone()
            viewDataBinding.selectAmountLabel.gone()
        }

        amountAdapter = SelectedAmountAdapter(listAmount.toList())
        val mLayoutManager = LinearLayoutManager(getBaseActivity())
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        viewDataBinding.selectAmountRv.layoutManager = mLayoutManager
        viewDataBinding.selectAmountRv.itemAnimator = DefaultItemAnimator()
        viewDataBinding.selectAmountRv.adapter = amountAdapter

        amountAdapter.onItemClick = { clickItem ->
            viewDataBinding.amountEdt.setText("")
            viewDataBinding.amountEdt.clearFocus()
            viewDataBinding.amountEdt.hideKeyBoard()

            listAmount.forEach {
                if (it.value == clickItem.value) {
                    selectedMoney = clickItem.value
                    it.isSelected = true
                } else {
                    it.isSelected = false
                }

            }
            amountAdapter.updateData(listAmount)
        }
    }

    override fun initView(view: View) {
        merchant = Gson().fromJson(arguments?.getString(MERCHANT), Merchant::class.java)
        page_title.text = merchant.cardName
        viewDataBinding.itemTypeTv.text = merchant.cardName
        Picasso.get().load(merchant.iconUrl).into(viewDataBinding.giftCardImv)
        viewDataBinding.amountEdt.setText(merchant.minCardValue)
        viewDataBinding.qualityEdt.setText("1")
        initAmountAdapter(merchant)

        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }

        viewDataBinding.buyGiftBtn.setOnClickListener {
            val amount = viewDataBinding.amountEdt.text.toString()
            val quantity = viewDataBinding.qualityEdt.text.toString()
            if (selectedMoney != null && quantity.isNotEmpty()) {
                handleBuyGiftCard(selectedMoney!!, quantity)
            } else if (selectedMoney == null && (amount.isEmpty() || quantity.isEmpty())) {
                showSnackBar(getString(R.string.default_validation))
            } else if (selectedMoney == null && (amount.isNotEmpty() || quantity.isNotEmpty())) {
                handleBuyGiftCard(amount, quantity)
            } else if (selectedMoney != null && quantity.isEmpty()) {
                showSnackBar(getString(R.string.default_validation))
            }
        }

        viewDataBinding.sendBtn.setOnClickListener {
            val amount = viewDataBinding.amountEdt.text.toString()
            val quantity = viewDataBinding.qualityEdt.text.toString()
            if (selectedMoney != null && quantity.isNotEmpty()) {
                handleSendToFriend(selectedMoney!!, quantity)
            } else if (selectedMoney == null && (amount.isEmpty() || quantity.isEmpty())) {
                showSnackBar(getString(R.string.default_validation))
            } else if (selectedMoney == null && (amount.isNotEmpty() || quantity.isNotEmpty())) {
                handleSendToFriend(amount, quantity)
            } else if (selectedMoney != null && quantity.isEmpty()) {
                showSnackBar(getString(R.string.default_validation))
            }
        }

        viewDataBinding.amountEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty() && s.toString().toFloat() > merchant.maxCardValue.toFloat()) {
                    isValidAmount = false
                    viewDataBinding.amountWarning.visible()
                    viewDataBinding.amountWarning.text =
                        String.format(getString(R.string.enter_maximum_amount), merchant.maxCardValue)
                    viewDataBinding.amountEdt.background =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.edittext_error)
                } else {
                    listAmount.forEach {
                        it.isSelected = false
                    }
                    amountAdapter.updateData(listAmount)

                    isValidAmount = true
                    viewDataBinding.amountWarning.gone()
                    viewDataBinding.amountEdt.background =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.rounded_background_blue_selector)
                }
            }
        })
    }

    private fun handleSendToFriend(amount: String, quantity: String) {
        if (isValidAmount) {
            val fragment = SendToFriendFragment.newInstance(
                merchant = merchant,
                amount = amount.toDouble().toPrettyString(),
                quantity = quantity.toDouble().toPrettyString()
            )
            fragmentNavigation.pushFragment(fragment)
        }
    }

    private fun handleBuyGiftCard(amount: String, quantity: String) {
        val request = SendToFriendRequest(
            buyType = BuySendType.SELF.type,
            friendName = "",
            friendEmail = "",
            friendPhone = "",
            message = ""
        )
        if (isValidAmount) {
            val fragment = CompleteOrderFragment.newInstance(
                merchant = merchant,
                amount = amount.toDouble().toPrettyString(),
                quantity = quantity.toDouble().toPrettyString(),
                sendToFriendRequest = request
            )
            fragmentNavigation.pushFragment(fragment)
        }
    }

    private fun Double.toPrettyString() =
        if (this - this.toLong() == 0.0)
            String.format("%d", this.toLong())
        else
            String.format("%s", this)

    companion object {
        const val SCREEN_TITLE = "Verification"
        private const val MERCHANT = "merchant"

        fun newInstance(merchant: Merchant): SingleGiftItemFragment {
            val fragment = SingleGiftItemFragment()
            val gson = Gson()
            val args = Bundle()
            args.putString(MERCHANT, gson.toJson(merchant))
            fragment.arguments = args
            return fragment
        }
    }
}
