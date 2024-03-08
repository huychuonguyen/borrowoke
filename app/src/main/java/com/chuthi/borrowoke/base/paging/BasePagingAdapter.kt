package com.chuthi.borrowoke.base.paging

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chuthi.borrowoke.base.BaseModel
import com.chuthi.borrowoke.base.BasePagingDiffAdapter
import com.chuthi.borrowoke.ext.onSafeClick
import com.chuthi.borrowoke.other.DEFAULT_CLICK_INTERVAL

/**************************************
- Created by Chuong Nguyen
- Email : chuong.nguyen@gumiviet.com
- Date : 06/01/2022
- Project : Nifehub
 **************************************/

/**
 * BasePagingAdapter.
 *
 * - This base adapter extended PagingDataAdapter,
 * define methods for paging data and inherited callbacks
 *
 * - Used for extended adapter with different objects of BaseModel
 *
 * @param T type of BaseModel - object to bind data
 */
abstract class BasePagingAdapter<T : BaseModel, VB : ViewBinding>(
    private val clickDelay: Long = DEFAULT_CLICK_INTERVAL,
    areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    areContentsTheSame: (oldItem: T, newItem: T) -> Boolean
) : PagingDataAdapter<T, BasePagingAdapter<T, VB>.BaseHolder>(
    BasePagingDiffAdapter(
        areItemsTheSame,
        areContentsTheSame
    )
) {
    /**
     * Override to set binding of layout item
     */
    protected abstract fun setViewBinding(parent: ViewGroup): VB

    /**
     * Override to raise on item clicked.
     * Note: if you wanna more child view callbacks,
     * implement your logic on [onBindData].
     */
    protected abstract fun onItemClicked(binding: VB, item: T, position: Int)

    /**
     * Callback return itemView: [RecyclerView.ViewHolder] and data [T]
     * to bind data into item view.
     * To handle more event, implement your logic code on this method.
     * @param binding the [RecyclerView.ViewHolder]
     * @param item the [T]
     */
    protected abstract fun onBindData(binding: VB, item: T, position: Int)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = BaseHolder(setViewBinding(parent))

    @CallSuper
    override fun onBindViewHolder(
        holder: BaseHolder,
        position: Int
    ) = getItem(position)?.let { holder.bindData(it, position) } ?: Unit

    open inner class BaseHolder(private val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Bind data into item view
         */
        fun bindData(item: T, position: Int) {
            // register item clicked
            binding.root.onSafeClick(clickDelay) {
                onItemClicked(binding = binding, item = item, position = position)
            }
            // raise callback bind data
            onBindData(binding = binding, item = item, position = position)
        }
    }
}