package com.chuthi.borrowoke.base.paging

import com.chuthi.borrowoke.base.BaseModel

/**
 * The base paging source based on lasted item
 */
abstract class LastedItemPagingSource<T : BaseModel> : BasePagingSource<T>() {

    // Override this method to check end paging
    // for lasted item paging source
    override fun onNextKey(data: List<T>?): Int? {
        return data?.run {
            // if(list is empty or lasted item is null
            // -> return null to end paging
            if (this.isEmpty() || lastedItem == null) null else 1
        }
    }
}