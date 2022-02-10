package com.globasure.giftoga.ui.screen.transactions

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.TransactionsFragBinding
import com.globasure.giftoga.network.response.MetaData
import com.globasure.giftoga.network.response.Transaction
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.custom.loadmore.LoadMoreAdapter
import com.globasure.giftoga.ui.custom.loadmore.LoadMoreWrapper
import com.globasure.giftoga.ui.screen.main_fragment.home.TransactionHistoryAdapter
import com.globasure.giftoga.utils.extension.gone
import com.globasure.giftoga.utils.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class TransactionsFrag : BaseFragment<TransactionsFragBinding, TransactionsFragViewModel>(), TransactionsFragView {

    private var pageNumber = 1
    private lateinit var enable: LoadMoreAdapter.Enabled
    private lateinit var loadMoreWrapper: LoadMoreWrapper
    private var allowToLoad = false
    private var needToReload = false
    private var myListTransactions = mutableListOf<Transaction>()

    private val transactionsFragViewModel: TransactionsFragViewModel by viewModels()

    override fun getLayoutId(): Int = R.layout.transactions_frag

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): TransactionsFragViewModel = transactionsFragViewModel

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
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }
        viewDataBinding.transactionRv.layoutManager = LinearLayoutManager(requireContext())
        transactionHistoryAdapter = TransactionHistoryAdapter(requireContext(), arrayListOf())
        viewDataBinding.transactionRv.adapter = transactionHistoryAdapter

        viewDataBinding.pullToRefresh.setOnRefreshListener {
            pageNumber = 1
            transactionsFragViewModel.getAllTransactionHistory(pageNumber, PAGE_ITEM)
            needToReload = true
        }

        loadMoreWrapper = LoadMoreWrapper(null).with(transactionHistoryAdapter)
        loadMoreWrapper
            .setShowNoMoreEnabled(true)
            .setListener(object : LoadMoreAdapter.OnLoadMoreListener {
                override fun onLoadMore(enabled: LoadMoreAdapter.Enabled?) {
                    enable = enabled!!
                    if (pageNumber > 0) {
                        transactionsFragViewModel.getAllTransactionHistory(pageNumber, PAGE_ITEM)
                        needToReload = false
                        pageNumber++
                    }
                }
            })
            .into(viewDataBinding.transactionRv)
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)

        if (allowToLoad) {
            pageNumber = 1
            transactionsFragViewModel.getAllTransactionHistory(pageNumber, PAGE_ITEM)
            needToReload = false
        }
    }

    override fun getAllTransactionSuccess(listTransaction: List<Transaction>, metaData: MetaData?) {
        if (needToReload) {
            myListTransactions.clear()
            pageNumber++
        }

        if (listTransaction.isEmpty()) {
            if (myListTransactions.size == 0) {
                viewDataBinding.empty.visible()
                viewDataBinding.transactionRv.gone()
                loadMoreWrapper.setLoadMoreEnabled(false)
                loadMoreWrapper.setShowNoMoreEnabled(false)
            } else {
                viewDataBinding.empty.gone()
                viewDataBinding.transactionRv.visible()
                loadMoreWrapper.setLoadMoreEnabled(false)
                loadMoreWrapper.setShowNoMoreEnabled(true)
            }
        } else {
            viewDataBinding.transactionRv.visibility = View.VISIBLE
            viewDataBinding.empty.visibility = View.GONE
            myListTransactions.addAll(listTransaction)
            if (listTransaction.size < metaData?.total ?: PAGE_ITEM) {
                enable.loadMoreEnabled = true
            } else {
                enable.loadMoreEnabled = false
                loadMoreWrapper.setShowNoMoreEnabled(true)
            }

            transactionHistoryAdapter.list = myListTransactions as ArrayList<Transaction>
            transactionHistoryAdapter.notifyDataSetChanged()
        }

        viewDataBinding.pullToRefresh.isRefreshing = false
        allowToLoad = true
    }

    companion object {
        const val SCREEN_TITLE = "My Transactions"
        private const val PAGE_ITEM = 20

        fun newInstance(): TransactionsFrag {
            return TransactionsFrag()
        }
    }
}
