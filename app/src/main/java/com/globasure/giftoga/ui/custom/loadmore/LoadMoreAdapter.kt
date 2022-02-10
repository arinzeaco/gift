package com.globasure.giftoga.ui.custom.loadmore

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.globasure.giftoga.R

@Suppress("unused")
class LoadMoreAdapter() : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        private const val TAG = "LoadMoreAdapter"
        private const val TYPE_FOOTER: Byte = -2
        private const val TYPE_NO_MORE: Byte = -3
        private const val TYPE_LOAD_FAILED: Byte = -4
    }

    private var mAdapter: RecyclerView.Adapter<ViewHolder>? = null
    private var mFooterView: View? = null
    private var mFooterResId = View.NO_ID
    private var mNoMoreView: View? = null
    private var mNoMoreResId = View.NO_ID
    private var mLoadFailedView: View? = null
    private var mLoadFailedResId = View.NO_ID

    private var mRecyclerView: RecyclerView? = null
    private var mOnLoadMoreListener: OnLoadMoreListener? = null

    private var mEnabled: Enabled? = null
    private var mIsLoading = false
    private var mShouldRemove = false
    private var mShowNoMoreEnabled = false
    private var mIsLoadFailed = false

    constructor(adapter: RecyclerView.Adapter<ViewHolder>?) : this() {
        registerAdapter(adapter)
    }

    constructor(adapter: RecyclerView.Adapter<ViewHolder>?, footerView: View?) : this(adapter) {
        registerAdapter(adapter)
        mFooterView = footerView
    }

    constructor(adapter: RecyclerView.Adapter<ViewHolder>?, @LayoutRes resId: Int) : this(adapter) {
        registerAdapter(adapter)
        mFooterResId = resId
    }

    private fun registerAdapter(adapter: RecyclerView.Adapter<ViewHolder>?) {
        if (adapter == null) {
            throw NullPointerException("adapter can not be null!")
        }
        mAdapter = adapter
        mAdapter!!.registerAdapterDataObserver(mObserver)
        mEnabled = Enabled(mOnEnabledListener)
    }

    fun getOriginalAdapter(): RecyclerView.Adapter<ViewHolder>? {
        return mAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            TYPE_FOOTER.toInt() -> {
                if (mFooterResId != View.NO_ID) {
                    mFooterView = LoadMoreHelper().inflate(parent, mFooterResId)
                }
                if (mFooterView != null) {
                    return FooterHolder(mFooterView)
                }
                val view: View = LoadMoreHelper().inflate(parent, R.layout.base_footer)!!
                return FooterHolder(view)
            }
            TYPE_NO_MORE.toInt() -> {
                if (mNoMoreResId != View.NO_ID) {
                    mNoMoreView = LoadMoreHelper().inflate(parent, mNoMoreResId)
                }
                if (mNoMoreView != null) {
                    return NoMoreHolder(mNoMoreView)
                }
                val view: View = LoadMoreHelper().inflate(parent, R.layout.base_no_more)!!
                return NoMoreHolder(view)
            }
            TYPE_LOAD_FAILED.toInt() -> {
                if (mLoadFailedResId != View.NO_ID) {
                    mLoadFailedView = LoadMoreHelper().inflate(parent, mLoadFailedResId)
                }
                var view = mLoadFailedView
                if (view == null) {
                    view = LoadMoreHelper().inflate(parent, R.layout.base_load_failed)
                }
                return LoadFailedHolder(view, mEnabled, mOnLoadMoreListener)
            }
            else -> return mAdapter!!.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any?>) {
        if (holder is FooterHolder) {
            // call loadMore
            if (!canScroll() && mOnLoadMoreListener != null && !mIsLoading) {
                mIsLoading = true
                // fix Cannot call this method while RecyclerView is computing a layout or scrolling
                mRecyclerView!!.post { mOnLoadMoreListener!!.onLoadMore(mEnabled) }
            }
        } else if (holder is NoMoreHolder || holder is LoadFailedHolder) {
            // ignore
        } else {
            mAdapter!!.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int {
        val count: Int = mAdapter!!.itemCount
        return if (getLoadMoreEnabled()) count + 1 else if (mShowNoMoreEnabled) count + 1 else count + if (mShouldRemove) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        if (position == mAdapter!!.itemCount && mIsLoadFailed) {
            return TYPE_LOAD_FAILED.toInt()
        }
        if (position == mAdapter!!.itemCount && getLoadMoreEnabled()) {
            return TYPE_FOOTER.toInt()
        } else if (position == mAdapter!!.itemCount && mShowNoMoreEnabled && !getLoadMoreEnabled()) {
            return TYPE_NO_MORE.toInt()
        }
        return mAdapter!!.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        val itemViewType = getItemViewType(position)
        return if (mAdapter!!.hasStableIds() && itemViewType != TYPE_FOOTER.toInt() && itemViewType != TYPE_LOAD_FAILED.toInt() && itemViewType != TYPE_NO_MORE.toInt()) {
            mAdapter!!.getItemId(position)
        } else super.getItemId(position)
    }

    fun canScroll(): Boolean {
        if (mRecyclerView == null) {
            throw NullPointerException("mRecyclerView is null, you should setAdapter(recyclerAdapter);")
        }
        val lm = mRecyclerView!!.layoutManager
        return if (lm is LinearLayoutManager) {
            ((lm as LinearLayoutManager?)?.findLastCompletelyVisibleItemPosition()!!
                .toInt()) < (mAdapter!!.itemCount - 1)
        } else mRecyclerView!!.canScrollVertically(-1)
    }

    fun setFooterView(footerView: View?) {
        mFooterView = footerView
    }

    fun setFooterView(@LayoutRes resId: Int) {
        mFooterResId = resId
    }

    fun getFooterView(): View? {
        return mFooterView
    }

    fun setNoMoreView(noMoreView: View?) {
        mNoMoreView = noMoreView
    }

    fun setNoMoreView(@LayoutRes resId: Int) {
        mNoMoreResId = resId
    }

    fun getNoMoreView(): View? {
        return mNoMoreView
    }

    fun setLoadFailedView(loadFailedView: View?) {
        mLoadFailedView = loadFailedView
    }

    fun setLoadFailedView(@LayoutRes resId: Int) {
        mLoadFailedResId = resId
    }

    fun getLoadFailedView(): View? {
        return mLoadFailedView
    }

    class FooterHolder(itemView: View?) : ViewHolder(itemView!!) {
        init {
            LoadMoreHelper().setItemViewFullSpan(itemView)
        }
    }

    class NoMoreHolder(itemView: View?) : ViewHolder(itemView!!) {
        init {
            LoadMoreHelper().setItemViewFullSpan(itemView)
        }
    }

    class LoadFailedHolder(itemView: View?, enabled: Enabled?, listener: OnLoadMoreListener?) : ViewHolder(itemView!!) {
        init {
            LoadMoreHelper().setItemViewFullSpan(itemView)
            itemView!!.setOnClickListener {
                enabled!!.setLoadFailed(false)
                listener?.onLoadMore(enabled)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView
        recyclerView.addOnScrollListener(mOnScrollListener)

        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val gridLayoutManager: GridLayoutManager = layoutManager
            val originalSizeLookup: GridLayoutManager.SpanSizeLookup = gridLayoutManager.spanSizeLookup!!
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val itemViewType = getItemViewType(position)
                    return if (itemViewType == TYPE_FOOTER.toInt() || itemViewType == TYPE_NO_MORE.toInt() || itemViewType == TYPE_LOAD_FAILED.toInt()) {
                        gridLayoutManager.spanCount
                    } else {
                        originalSizeLookup.getSpanSize(position)
                    }
                }
            }
        }
    }

    /**
     * Deciding whether to trigger loading
     */
    private val mOnScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!getLoadMoreEnabled() || mIsLoading) {
                return
            }
            if (newState == RecyclerView.SCROLL_STATE_IDLE && mOnLoadMoreListener != null) {
                val isBottom: Boolean
                val layoutManager = recyclerView.layoutManager
                isBottom = when (layoutManager) {
                    is LinearLayoutManager -> {
                        ((layoutManager as LinearLayoutManager?)?.findLastVisibleItemPosition()!!
                            .toInt() >= layoutManager.itemCount - 1)
                    }
                    is StaggeredGridLayoutManager -> {
                        val sgLayoutManager: StaggeredGridLayoutManager = layoutManager
                        val into = IntArray(sgLayoutManager.spanCount)
                        sgLayoutManager.findLastVisibleItemPositions(into)
                        last(into) >= layoutManager.itemCount - 1
                    }
                    else -> {
                        ((layoutManager as GridLayoutManager?)?.findLastVisibleItemPosition()!!
                                >= layoutManager!!.itemCount - 1)
                    }
                }
                if (isBottom) {
                    mIsLoading = true
                    mOnLoadMoreListener!!.onLoadMore(mEnabled)
                }
            }
        }
    }

    private fun last(lastPositions: IntArray): Int {
        var last = lastPositions[0]
        for (value in lastPositions) {
            if (value > last) {
                last = value
            }
        }
        return last
    }

    /**
     * clean
     */
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(mOnScrollListener)
        mAdapter!!.unregisterAdapterDataObserver(mObserver)
        mRecyclerView = null
    }

    fun setLoadMoreListener(listener: OnLoadMoreListener?) {
        mOnLoadMoreListener = listener
    }

    interface OnLoadMoreListener {
        fun onLoadMore(enabled: Enabled?)
    }

    fun setLoadMoreEnabled(enabled: Boolean) {
        mEnabled!!.loadMoreEnabled = enabled
    }

    fun getLoadMoreEnabled(): Boolean {
        return mEnabled!!.loadMoreEnabled && mAdapter!!.itemCount >= 0
    }

    interface OnEnabledListener {
        fun notifyChanged()
        fun notifyLoadFailed(isLoadFailed: Boolean)
    }

    private val mOnEnabledListener: OnEnabledListener = object : OnEnabledListener {
        override fun notifyChanged() {
            mShouldRemove = true
        }

        override fun notifyLoadFailed(isLoadFailed: Boolean) {
            mIsLoadFailed = isLoadFailed
            notifyFooterHolderChanged()
        }
    }

    fun setShouldRemove(shouldRemove: Boolean) {
        mShouldRemove = shouldRemove
    }

    fun setShowNoMoreEnabled(showNoMoreEnabled: Boolean) {
        mShowNoMoreEnabled = showNoMoreEnabled
    }

    fun setLoadFailed(isLoadFailed: Boolean) {
        mEnabled!!.setLoadFailed(isLoadFailed)
    }

    /**
     * [OnLoadMoreListener.onLoadMore]
     */
    class Enabled(private val mListener: OnEnabledListener) {
        private var mLoadMoreEnabled = true
        private var mIsLoadFailed = false

        /**
         * @param isLoadFailed
         */
        fun setLoadFailed(isLoadFailed: Boolean) {
            if (mIsLoadFailed != isLoadFailed) {
                mIsLoadFailed = isLoadFailed
                mListener.notifyLoadFailed(isLoadFailed)
                loadMoreEnabled = !mIsLoadFailed
            }
        }

        /**
         * true
         *
         * @return boolean
         */
        var loadMoreEnabled: Boolean
            get() = mLoadMoreEnabled
            set(enabled) {
                val canNotify = mLoadMoreEnabled
                mLoadMoreEnabled = enabled
                if (canNotify && !mLoadMoreEnabled) {
                    mListener.notifyChanged()
                }
            }

    }

    private val mObserver: RecyclerView.AdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            if (mShouldRemove) {
                mShouldRemove = false
            }
            notifyDataSetChanged()
            mIsLoading = false
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            if (mShouldRemove && positionStart == mAdapter!!.itemCount) {
                mShouldRemove = false
            }
            this@LoadMoreAdapter.notifyItemRangeChanged(positionStart, itemCount)
            mIsLoading = false
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            if (mShouldRemove && positionStart == mAdapter!!.itemCount) {
                mShouldRemove = false
            }
            this@LoadMoreAdapter.notifyItemRangeChanged(positionStart, itemCount, payload)
            mIsLoading = false
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            // when no data is initialized (has loadMoreView)
            // should remove loadMoreView before notifyItemRangeInserted
            if (mRecyclerView!!.childCount == 1) {
                notifyItemRemoved(0)
            }
            notifyItemRangeInserted(positionStart, itemCount)
            notifyFooterHolderChanged()
            mIsLoading = false
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            if (mShouldRemove && positionStart == mAdapter!!.itemCount) {
                mShouldRemove = false
            }
            /*
               use notifyItemRangeRemoved after clear item, can throw IndexOutOfBoundsException
             */
            var shouldSync = false
            if (mEnabled!!.loadMoreEnabled && mAdapter!!.itemCount == 0) {
                setLoadMoreEnabled(false)
                shouldSync = true
                // when use onItemRangeInserted(0, count) after clear item
                // recyclerView will auto scroll to bottom, because has one item(loadMoreView)
                // remove loadMoreView
                if (getItemCount() == 1) {
                    notifyItemRemoved(0)
                }
            }
            notifyItemRangeRemoved(positionStart, itemCount)
            if (shouldSync) {
                setLoadMoreEnabled(true)
            }
            mIsLoading = false
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            require(!(mShouldRemove && (fromPosition == mAdapter!!.itemCount || toPosition == mAdapter!!.itemCount))) { "can not move last position after setLoadMoreEnabled(false)" }
            notifyItemMoved(fromPosition, toPosition)
            mIsLoading = false
        }
    }

    /**
     * update last item
     */
    private fun notifyFooterHolderChanged() {
        if (getLoadMoreEnabled()) {
            this@LoadMoreAdapter.notifyItemChanged(mAdapter!!.itemCount)
        } else if (mShouldRemove) {
            mShouldRemove = false

            /*
              fix IndexOutOfBoundsException when setLoadMoreEnabled(false) and then use onItemRangeInserted
             */
            val position: Int = mAdapter!!.itemCount
            val viewHolder = mRecyclerView!!.findViewHolderForAdapterPosition(position)
            if (viewHolder is FooterHolder) {
                notifyItemRemoved(position)
            } else {
                this@LoadMoreAdapter.notifyItemChanged(position)
            }
        }
    }
}