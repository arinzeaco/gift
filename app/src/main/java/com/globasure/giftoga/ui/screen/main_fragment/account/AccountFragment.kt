package com.globasure.giftoga.ui.screen.main_fragment.account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.AccountFragBinding
import com.globasure.giftoga.network.response.Transaction
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.screen.main_fragment.home.TransactionHistoryAdapter
import com.globasure.giftoga.ui.screen.transactions.TransactionsFrag
import com.globasure.giftoga.utils.CalendarUtils
import com.globasure.giftoga.utils.extension.formatCurrency
import com.globasure.giftoga.utils.extension.formatDecimal
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import timber.log.Timber

@AndroidEntryPoint
class AccountFragment : BaseFragment<AccountFragBinding, AccountFragViewModel>(), AccountFragView {

    private val accountFragViewModel: AccountFragViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.account_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): AccountFragViewModel = accountFragViewModel

    override fun getPageTitle(): String = SCREEN_TITLE

    private lateinit var transactionHistoryAdapter: TransactionHistoryAdapter

    override fun setToolBar(view: View) {
        page_title.text = screenTitle
    }

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
        getTransactionHistory()
    }

    fun getTransactionHistory() {
        accountFragViewModel.getAllTransactionHistory()
    }

    override fun initView(view: View) {
        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }

        accountFragViewModel.getAllTransactionHistory()

        viewDataBinding.viewAllBtn.setOnClickListener {
            val fragment = TransactionsFrag.newInstance()
            fragmentNavigation.pushFragment(fragment)
        }
        transactionHistoryAdapter = TransactionHistoryAdapter(requireContext(), arrayListOf(), true)
        viewDataBinding.transactionRv.adapter = transactionHistoryAdapter
        viewDataBinding.pullToRefresh.setOnRefreshListener {
            accountFragViewModel.getAllTransactionHistory()
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateUserDetail() {
        hawkHelper.getUserDetail()?.profileImage?.let {
            Picasso.get().load(it).into(viewDataBinding.userAvatar)
        }
        viewDataBinding.fullName.text =
            "${hawkHelper.getUserDetail()?.firstName} ${hawkHelper.getUserDetail()?.lastName}"
        viewDataBinding.emailEd.text = hawkHelper.getUserDetail()?.email
        viewDataBinding.dateJoinedTv.text = hawkHelper.getUserDetail()?.created_date
        val datetime = hawkHelper.getUserDetail()?.created_date!!.split(" ")
        viewDataBinding.dateJoinedTv.text = CalendarUtils.formattedDate(CalendarUtils.parseStringToDate(datetime[0]))
        viewDataBinding.walletBalanceTv.text =
            hawkHelper.getUserDetail()?.balance?.walletBalance?.formatDecimal()?.formatCurrency(
                isPrefix = true,
                currencyName = hawkHelper.getUserDetail()?.balance?.currencyName
            )
    }

    override fun onResume() {
        super.onResume()
        updateUserDetail()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(true)
    }

    override fun getAllTransactionSuccess(listTransaction: List<Transaction>) {
        viewDataBinding.transactionRv.layoutManager = LinearLayoutManager(requireContext())
        viewDataBinding.pullToRefresh.isRefreshing = false

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

    companion object {
        const val SCREEN_TITLE = "Account"

        fun newInstance(): AccountFragment {
            return AccountFragment()
        }
    }
}
