package com.globasure.giftoga.ui.screen.main_fragment.buy_cards

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.AccountType
import com.globasure.giftoga.databinding.BuyCardsFragBinding
import com.globasure.giftoga.network.request.MerchantsRequest
import com.globasure.giftoga.network.response.CountryData
import com.globasure.giftoga.network.response.Merchant
import com.globasure.giftoga.network.response.MetaData
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.custom.loadmore.LoadMoreAdapter
import com.globasure.giftoga.ui.custom.loadmore.LoadMoreWrapper
import com.globasure.giftoga.ui.dialog.country.DialogListCountry
import com.globasure.giftoga.ui.screen.authentication.personalAccount.PersonalAccountFrag
import com.globasure.giftoga.ui.screen.bulk_purchase.BulkPurchaseFragment
import com.globasure.giftoga.ui.screen.single_giftcard_item.SingleGiftItemFragment
import com.globasure.giftoga.utils.extension.gone
import com.globasure.giftoga.utils.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.buy_card_toolbar.backButton
import timber.log.Timber


@AndroidEntryPoint
class BuyCardsFragment : BaseFragment<BuyCardsFragBinding, BuyCardsFragViewModel>(), BuyCardsFragView {

    private var pageNumber = 1
    private lateinit var enable: LoadMoreAdapter.Enabled
    private lateinit var loadMoreWrapper: LoadMoreWrapper
    private var allowToLoad = false
    private var needToReload = false
    private var listMerchants = mutableListOf<Merchant>()
    private lateinit var countryDialog: DialogListCountry
    private lateinit var buyCardsAdapter: BuyCardsAdapter

    private val buyCardsFragViewModel: BuyCardsFragViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.buy_cards_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): BuyCardsFragViewModel = buyCardsFragViewModel

    override fun getColorStatusBar(): Int = R.color.white

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getPageTitle(): String = getString(R.string.buy_cards)

    override fun setToolBar(view: View) {
        //do nothing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun initView(view: View) {
        backButton.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }

        val gridLayoutManager = GridLayoutManager(context, 2)
        viewDataBinding.buyGiftCardRv.layoutManager = gridLayoutManager

        buyCardsAdapter = BuyCardsAdapter(context, arrayListOf())
        viewDataBinding.buyGiftCardRv.adapter = buyCardsAdapter

        viewDataBinding.pullToRefresh.setOnRefreshListener {
            pageNumber = 1
            val merchantsRequest = MerchantsRequest(page = pageNumber.toString(), perPage = PAGE_ITEM)
            buyCardsFragViewModel.getGetAllMerchant(merchantsRequest)
            needToReload = true
        }

        buyCardsFragViewModel.getCountryList()

        pageNumber = 1
        needToReload = true

        loadMoreWrapper = LoadMoreWrapper(null).with(buyCardsAdapter)
        loadMoreWrapper
            .setShowNoMoreEnabled(true)
            .setListener(object : LoadMoreAdapter.OnLoadMoreListener {
                override fun onLoadMore(enabled: LoadMoreAdapter.Enabled?) {
                    enable = enabled!!
                    if (pageNumber > 0) {
                        val merchantsRequest = MerchantsRequest(page = pageNumber.toString(), perPage = PAGE_ITEM)
                        buyCardsFragViewModel.getGetAllMerchant(merchantsRequest)
                        pageNumber++
                    }
                }
            })
            .into(viewDataBinding.buyGiftCardRv)

        viewDataBinding.giftSearchEdt.doOnTextChanged { text, _, _, _ ->
            buyCardsAdapter.filter.filter(text)
        }
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)
    }

    override fun getCountryList(list: List<CountryData>) {
        countryDialog = DialogListCountry(list as ArrayList<CountryData>)
        viewDataBinding.giftCountrySearch.setOnClickListener {
            handleChooseCountryCLick()
        }
    }

    override fun getMerchantsGiftCard(list: List<Merchant>, metaData: MetaData?) {
        if (needToReload) {
            listMerchants.clear()
            pageNumber++
        }

        if (list.isEmpty()) {
            if (listMerchants.size == 0) {
                viewDataBinding.empty.visible()
                viewDataBinding.buyGiftCardRv.gone()
                loadMoreWrapper.setLoadMoreEnabled(false)
                loadMoreWrapper.setShowNoMoreEnabled(false)
            } else {
                viewDataBinding.empty.gone()
                viewDataBinding.buyGiftCardRv.visible()
                loadMoreWrapper.setLoadMoreEnabled(false)
                loadMoreWrapper.setShowNoMoreEnabled(true)
            }
        } else {
            viewDataBinding.buyGiftCardRv.visible()
            viewDataBinding.empty.gone()
            listMerchants.addAll(list)
            needToReload = false
            if (list.size < metaData?.total!!) {
                enable.loadMoreEnabled = true
            } else {
                enable.loadMoreEnabled = false
                loadMoreWrapper.setShowNoMoreEnabled(true)
            }

            buyCardsAdapter.setMerchants(listMerchants)
            buyCardsAdapter.setOriginalList(listMerchants)
        }

        viewDataBinding.pullToRefresh.isRefreshing = false
        allowToLoad = true
    }

    fun moveToSingleItemFragment(merchant: Merchant) {
        if (hawkHelper.getUserDetail()!!.accountType!!.contentEquals(AccountType.BUSINESS.type)) {
            val fragment = BulkPurchaseFragment.newInstance(merchant)
            (getBaseActivity() as MainActivity).fragNavController.pushFragment(fragment)
        } else {
            val fragment = SingleGiftItemFragment.newInstance(merchant)
            (getBaseActivity() as MainActivity).fragNavController.pushFragment(fragment)
        }
    }

    private fun handleChooseCountryCLick() {
        countryDialog.itemClick = {
            viewDataBinding.giftCountrySearch.text = it.name
            countryDialog.dismiss()
        }

        countryDialog.onCloseClick = {
            countryDialog.dismiss()
        }

        countryDialog.show(this.childFragmentManager, PersonalAccountFrag.TAG)
    }

    companion object {
        private const val PAGE_ITEM = "20"

        fun newInstance(): BuyCardsFragment {
            return BuyCardsFragment()
        }
    }
}
