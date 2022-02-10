package com.globasure.giftoga.ui.custom.loadmore

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

@Suppress("unused")
class LoadMoreWrapper(loadMoreAdapter: LoadMoreAdapter?) {

    private var mLoadMoreAdapter: LoadMoreAdapter? = null

    init {
        mLoadMoreAdapter = loadMoreAdapter
    }

    fun with(adapter: Adapter<RecyclerView.ViewHolder>): LoadMoreWrapper {
        val loadMoreAdapter = LoadMoreAdapter(adapter)
        return LoadMoreWrapper(loadMoreAdapter)
    }

    fun setFooterView(@LayoutRes resId: Int): LoadMoreWrapper {
        mLoadMoreAdapter!!.setFooterView(resId)
        return this
    }

    fun setFooterView(footerView: View?): LoadMoreWrapper {
        mLoadMoreAdapter!!.setFooterView(footerView)
        return this
    }

    fun getFooterView(): View? {
        return mLoadMoreAdapter!!.getFooterView()
    }

    fun setNoMoreView(@LayoutRes resId: Int): LoadMoreWrapper {
        mLoadMoreAdapter!!.setNoMoreView(resId)
        return this
    }

    fun setNoMoreView(noMoreView: View?): LoadMoreWrapper {
        mLoadMoreAdapter!!.setNoMoreView(noMoreView)
        return this
    }

    fun getNoMoreView(): View? {
        return mLoadMoreAdapter!!.getNoMoreView()
    }

    fun setLoadFailedView(@LayoutRes resId: Int): LoadMoreWrapper {
        mLoadMoreAdapter!!.setLoadFailedView(resId)
        return this
    }

    fun setLoadFailedView(view: View?): LoadMoreWrapper {
        mLoadMoreAdapter!!.setLoadFailedView(view)
        return this
    }

    fun getLoadFailedView(): View? {
        return mLoadMoreAdapter!!.getLoadFailedView()
    }

    /**
     *
     * @param listener
     */
    fun setListener(listener: LoadMoreAdapter.OnLoadMoreListener?): LoadMoreWrapper {
        mLoadMoreAdapter!!.setLoadMoreListener(listener)
        return this
    }

    /**
     *
     * @param enabled default true
     */
    fun setLoadMoreEnabled(enabled: Boolean): LoadMoreWrapper {
        mLoadMoreAdapter!!.setLoadMoreEnabled(enabled)
        if (!enabled) {
            mLoadMoreAdapter!!.setShouldRemove(true)
        }
        return this
    }

    /**
     *
     * @param enabled default false
     */
    fun setShowNoMoreEnabled(enabled: Boolean): LoadMoreWrapper {
        mLoadMoreAdapter!!.setShowNoMoreEnabled(enabled)
        return this
    }

    fun setLoadFailed(isLoadFailed: Boolean) {
        mLoadMoreAdapter!!.setLoadFailed(isLoadFailed)
    }

    /**
     * adapter
     */
    fun getOriginalAdapter(): Adapter<RecyclerView.ViewHolder>? {
        return mLoadMoreAdapter!!.getOriginalAdapter()
    }

    fun into(recyclerView: RecyclerView): LoadMoreAdapter? {
        mLoadMoreAdapter!!.setHasStableIds(mLoadMoreAdapter!!.getOriginalAdapter()!!.hasStableIds())
        recyclerView.adapter = mLoadMoreAdapter
        return mLoadMoreAdapter
    }
}