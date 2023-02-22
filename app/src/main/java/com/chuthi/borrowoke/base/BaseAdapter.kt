package com.chibijob.ui.base

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chuthi.borrowoke.base.BaseDiffAdapter
import com.chuthi.borrowoke.base.BaseModel
import com.chuthi.borrowoke.ext.onSafeClick
import com.chuthi.borrowoke.other.DEFAULT_CLICK_INTERVAL
import java.util.concurrent.Executors

/**************************************
- Created by Chuong Nguyen
- Email : chuong.nguyen@gumiviet.com
- Date : 10/17/2022
- Project : Chibijob
 **************************************/

/**
 * BaseAdapter.
 * @param clickDelay Delay time between each click action
 */
abstract class BaseAdapter<T : BaseModel, VB : ViewBinding>(
    private val clickDelay: Long = DEFAULT_CLICK_INTERVAL
) : ListAdapter<T, BaseAdapter<T, VB>.BaseHolder>(
    AsyncDifferConfig
        .Builder(BaseDiffAdapter<T>())
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
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
    ) = holder.bindData(getItem(position), position)

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
