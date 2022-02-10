package com.globasure.giftoga.ui.screen.main_fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.constant.AccountType
import com.globasure.giftoga.databinding.HomeFragBinding
import com.globasure.giftoga.network.response.ProfileResponse
import com.globasure.giftoga.network.response.Transaction
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.fund_wallet.FundWalletFragment
import com.globasure.giftoga.ui.screen.main_fragment.account.AccountFragment
import com.globasure.giftoga.utils.extension.formatCurrency
import com.globasure.giftoga.utils.extension.formatDecimal
import com.globasure.giftoga.utils.extension.gone
import com.globasure.giftoga.utils.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragBinding, HomeFragViewModel>(), HomeFragView {

    private val homeFragViewModel: HomeFragViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.home_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): HomeFragViewModel = homeFragViewModel

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun getColorStatusBar(): Int = R.color.app_back_ground_color

    override fun getPageTitle(): String = SCREEN_TITLE

    override fun setToolBar(view: View) {
        //do nothing
    }

    private lateinit var transactionHistoryAdapter: TransactionHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
    }

    override fun initView(view: View) {
        homeFragViewModel.getAllTransactionHistory()
        viewDataBinding.name = String.format(
            getString(getWelcomeTitle()),
            "${hawkHelper.getUserDetail()?.firstName} ${hawkHelper.getUserDetail()?.lastName}"
        )
        transactionHistoryAdapter = TransactionHistoryAdapter(requireContext(), arrayListOf(), true)
        viewDataBinding.transactionRv.adapter = transactionHistoryAdapter

        updateAmountDetail()
        viewDataBinding.depositType.setOnClickListener {
            val fragment = FundWalletFragment.newInstance()
            (getBaseActivity() as MainActivity).pushFragment(fragment)
        }
        viewDataBinding.pullToRefresh.setOnRefreshListener {
            homeFragViewModel.getAllTransactionHistory()
        }
        handleShowingCards()
    }

    private fun getWelcomeTitle(): Int {
        val c: Calendar = Calendar.getInstance()
        return when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> R.string.welcome_home_morning_title
            in 12..18 -> R.string.welcome_home_afternoon_title
            else -> R.string.welcome_home_evening_title
        }
    }

    private fun updateAmountDetail() {
        viewDataBinding.depositAmount.text =
            hawkHelper.getUserDetail()?.balance?.walletBalance?.formatDecimal()?.formatCurrency(
                isPrefix = true,
                currencyName = hawkHelper.getUserDetail()?.balance?.currencyName
            )
        viewDataBinding.commissionAmount.text = hawkHelper.getUserDetail()?.balance?.commission
        viewDataBinding.salesAmount.text = hawkHelper.getUserDetail()?.balance?.giftcardSales
    }

    private fun handleShowingCards() {
        when (hawkHelper.getUserDetail()?.accountType) {
            AccountType.PERSONAL.type -> {
                viewDataBinding.depositCard.visible()
                viewDataBinding.commissionCard.gone()
                viewDataBinding.salesCard.visible()
                viewDataBinding.redeemCard.gone()
                viewDataBinding.salesAmountCard.gone()
                viewDataBinding.giftOgaCard.visible()
            }
            AccountType.BUSINESS.type -> {
                viewDataBinding.depositCard.visible()
                viewDataBinding.commissionCard.gone()
                viewDataBinding.salesCard.visible()
                viewDataBinding.redeemCard.gone()
                viewDataBinding.salesAmountCard.gone()
                viewDataBinding.giftOgaCard.visible()
            }
            AccountType.AGENT.type -> {
                viewDataBinding.depositCard.visible()
                viewDataBinding.commissionCard.visible()
                viewDataBinding.salesCard.visible()
                viewDataBinding.redeemCard.gone()
                viewDataBinding.salesAmountCard.gone()
                viewDataBinding.giftOgaCard.visible()
            }
            AccountType.MERCHANT.type -> {
                viewDataBinding.depositCard.visible()
                viewDataBinding.commissionCard.visible()
                viewDataBinding.salesCard.visible()
                viewDataBinding.redeemCard.visible()
                viewDataBinding.salesAmountCard.visible()
                viewDataBinding.giftOgaCard.gone()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        homeFragViewModel.getUserDetail()
        homeFragViewModel.getAllTransactionHistory()
        getBaseActivity()?.setStatusBarColor(R.color.app_back_ground_color)
        (getBaseActivity() as MainActivity).showHideNavigationBottom(true)
    }

    override fun getAllTransactionSuccess(listTransaction: List<Transaction>) {
        viewDataBinding.pullToRefresh.isRefreshing = false
        viewDataBinding.transactionRv.layoutManager = LinearLayoutManager(requireContext())

        if (listTransaction.isEmpty()) {
            viewDataBinding.transactionRv.visibility = View.GONE
            viewDataBinding.empty.visibility = View.VISIBLE
        } else {
            viewDataBinding.transactionRv.visibility = View.VISIBLE
            viewDataBinding.empty.visibility = View.GONE
            transactionHistoryAdapter.list = listTransaction as ArrayList<Transaction>
            transactionHistoryAdapter.notifyDataSetChanged()
        }
    }

    override fun getUserDetailSuccess(profileResponse: ProfileResponse) {
        updateAmountDetail()

        (activity as MainActivity).fragNavController.getStack(1)?.let {
            (it[0] as AccountFragment).updateUserDetail()
            (it[0] as AccountFragment).getTransactionHistory()
        }
    }

    companion object {
        const val SCREEN_TITLE = "Home Page"

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}
