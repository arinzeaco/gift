package com.globasure.giftoga.ui.screen.settings_tab.debit_card

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.OnClick
import com.globasure.giftoga.BR
import com.globasure.giftoga.MainActivity
import com.globasure.giftoga.R
import com.globasure.giftoga.databinding.DebitCardsFragBinding
import com.globasure.giftoga.network.response.CardData
import com.globasure.giftoga.network.response.MetaData
import com.globasure.giftoga.ui.base.BaseFragment
import com.globasure.giftoga.ui.custom.loadmore.LoadMoreAdapter
import com.globasure.giftoga.ui.custom.loadmore.LoadMoreWrapper
import com.globasure.giftoga.ui.screen.settings_tab.debit_card.add_card.AddCardFragment
import com.globasure.giftoga.utils.extension.gone
import com.globasure.giftoga.utils.extension.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_card_action.view.choose_delete
import kotlinx.android.synthetic.main.dialog_card_action.view.choose_edit
import kotlinx.android.synthetic.main.topbar_item.back_button
import kotlinx.android.synthetic.main.topbar_item.page_title
import timber.log.Timber

@AndroidEntryPoint
class DebitCardsFragment : BaseFragment<DebitCardsFragBinding, DebitCardsViewModel>(), DebitCardsView {

    private var pageNumber = 1
    private lateinit var enable: LoadMoreAdapter.Enabled
    private lateinit var loadMoreWrapper: LoadMoreWrapper
    private var allowToLoad = false
    private var needToReload = false
    private var myListCards = mutableListOf<CardData>()
    private lateinit var debitCardsAdapter: DebitCardsAdapter
    private var pageTitle: String = ""
    private var allowToChooseCard: Boolean = false

    private val debitCardsViewModel: DebitCardsViewModel by viewModels()

    override fun getViewModel(): DebitCardsViewModel = debitCardsViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.debit_cards_frag

    override fun getPageTitle(): String = SCREEN_TITLE

    override fun onErrorAfterDialogDismiss(error: Throwable, option: Any?) {
        Timber.e(error)
    }

    override fun setToolBar(view: View) {
        //do nothing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getViewModel().setView(this)
        arguments?.let {
            pageTitle = it.getString(PAGE_TITLE)!!
            allowToChooseCard = it.getBoolean(ALLOW_TO_CHOOSE_CARD)
        }
    }

    override fun initView(view: View) {
        page_title.text = pageTitle

        myListCards.clear()
        back_button.setOnClickListener {
            (getBaseActivity() as MainActivity).onBackPressed()
        }
        viewDataBinding.cardsRv.layoutManager = LinearLayoutManager(requireContext())
        debitCardsAdapter = DebitCardsAdapter(requireContext(), arrayListOf(), allowToChooseCard)
        debitCardsAdapter.onOptionClick = { cardData ->
            openCardActionDialog(requireContext(), {

            }, {
                debitCardsViewModel.removeCard(cardData.token)
            })
        }
        debitCardsAdapter.onItemClick = {
            setFragmentResult("SELECTED_CARD", bundleOf("selected-card" to it))
            (getBaseActivity() as MainActivity).onBackPressed()
        }
        viewDataBinding.cardsRv.adapter = debitCardsAdapter

        viewDataBinding.pullToRefresh.setOnRefreshListener {
            pageNumber = 1
            debitCardsViewModel.getAllCards(pageNumber, PAGE_ITEM)
            needToReload = true
        }

        loadMoreWrapper = LoadMoreWrapper(null).with(debitCardsAdapter)
        loadMoreWrapper
            .setShowNoMoreEnabled(true)
            .setListener(object : LoadMoreAdapter.OnLoadMoreListener {
                override fun onLoadMore(enabled: LoadMoreAdapter.Enabled?) {
                    enable = enabled!!
                    if (pageNumber > 0) {
                        debitCardsViewModel.getAllCards(pageNumber, PAGE_ITEM)
                        needToReload = false
                        pageNumber++
                    }
                }
            })
            .into(viewDataBinding.cardsRv)
    }

    @SuppressLint("InflateParams")
    fun openCardActionDialog(context: Context, chooseEdit: () -> Unit, chooseDelete: () -> Unit) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.transparent_bottom_sheet_dialog_theme)
        val customLayout: View = LayoutInflater.from(context).inflate(R.layout.dialog_card_action, null)
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(customLayout)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        customLayout.choose_edit.setOnClickListener {
            chooseEdit()
            bottomSheetDialog.dismiss()
        }
        customLayout.choose_delete.setOnClickListener {
            chooseDelete()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    override fun onResume() {
        super.onResume()
        (getBaseActivity() as MainActivity).showHideNavigationBottom(false)

        if (allowToLoad) {
            pageNumber = 1
            debitCardsViewModel.getAllCards(pageNumber, PAGE_ITEM)
            needToReload = false
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.add_new_card_button)
    fun addNewCardClick() {
        val fragment = AddCardFragment.newInstance()
        fragmentNavigation.pushFragment(fragment)
    }

    override fun loadCardsSuccess(list: List<CardData>, metaData: MetaData?) {
        if (needToReload) {
            myListCards.clear()
            pageNumber++
        }

        if (list.isEmpty()) {
            if (myListCards.size == 0) {
                viewDataBinding.empty.visible()
                viewDataBinding.cardsRv.gone()
                loadMoreWrapper.setLoadMoreEnabled(false)
                loadMoreWrapper.setShowNoMoreEnabled(false)
            } else {
                viewDataBinding.empty.gone()
                viewDataBinding.cardsRv.visible()
                loadMoreWrapper.setLoadMoreEnabled(false)
                loadMoreWrapper.setShowNoMoreEnabled(true)
            }
        } else {
            viewDataBinding.cardsRv.visibility = View.VISIBLE
            viewDataBinding.empty.visibility = View.GONE
            myListCards.addAll(list)
            myListCards = myListCards.distinctBy { it.cardNumber }.toMutableList()
            if (list.size < metaData?.total ?: PAGE_ITEM) {
                enable.loadMoreEnabled = true
            } else {
                enable.loadMoreEnabled = false
                loadMoreWrapper.setShowNoMoreEnabled(true)
            }

            debitCardsAdapter.list = myListCards as ArrayList<CardData>
            debitCardsAdapter.notifyDataSetChanged()
        }

        viewDataBinding.pullToRefresh.isRefreshing = false
        allowToLoad = true
    }

    override fun deleteCardSuccess() {
        pageNumber = 1
        debitCardsViewModel.getAllCards(pageNumber, PAGE_ITEM)
        needToReload = true
        showSnackBar(getString(R.string.delete_card_done))
    }

    companion object {
        const val SCREEN_TITLE = "Debit Cards"
        private const val PAGE_ITEM = 20
        private const val PAGE_TITLE = "PAGE_TITLE"
        private const val ALLOW_TO_CHOOSE_CARD = "ALLOW_TO_CHOOSE_CARD"

        fun newInstance(title: String, allowToChooseCard: Boolean): DebitCardsFragment {
            val args = Bundle()
            args.putString(PAGE_TITLE, title)
            args.putBoolean(ALLOW_TO_CHOOSE_CARD, allowToChooseCard)
            val fragment = DebitCardsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}