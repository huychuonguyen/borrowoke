package com.chuthi.borrowoke.base.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.chuthi.borrowoke.base.BaseModel

/**************************************
- Created by Chuong Nguyen
- Email : chuong.nguyen@gumiviet.com
- Date : 06/01/2022
- Project : Nifehub
 **************************************/

/**
 * BasePagingAdapter.
 *
 * - This base paging source extended PagingSource,
 * convert raw data to paging data,
 * then submit to PagingAdapter
 *
 * - Used for extended paging source with different objects of BaseModel
 *
 * @param T type of BaseModel - paging return object
 */
abstract class BasePagingSource<T : BaseModel> : PagingSource<Int, T>() {

    /**
     * Hold the lasted item at current page
     */
    private var _lastedItem: T? = null
    val lastedItem: T?
        get() = _lastedItem

    /**
     * override on extended paging source and implement paging api,
     * callback to raise api calling with BaseResponseModel<List<T>> response,
     * @return BaseResponseModel<List<T>>?
     * */
    abstract fun submitAction(): suspend (Int, Int, T?) -> List<T>?

    /**
     * On error
     * callback with a paging error message
     * @return Unit
     */
    abstract fun onError(): (String) -> Unit

    /**
     * Get next key to paging source,
     * default is: <page, limit> paging
     * Override this method to custom next key
     * (Exp for: <limit, lastedItem> paging)
     */
    open fun onNextKey(data: List<T>?): Int? {
        return data?.run {
            // currentPage >= totalPages
            // to fix continuous request to server when api error
            // -> end paging
            if (data.isEmpty()) null
            else 1
        }
    }

    /**
     * Get refresh key
     * Detect when need to continue call api to next page
     * @param state
     * @return
     */
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            // get page to paging data, default is 1
            var page = params.key ?: PAGING_STARTING_PAGE_INDEX
            // if page start at 0, set to 1
            if (page == 0) page++
            // response data of query action
            val data = submitAction().invoke(page, PAGING_DEFAULT_SIZE, _lastedItem)
            // update lasted item
            _lastedItem = data?.lastOrNull()
            // set next key to next paging,
            // if null -> end paging
            val nextKey = onNextKey(data)?.plus(page)
            // paging data
            LoadResult.Page(
                data = data ?: emptyList(),
                prevKey = if (page <= 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (ex: Exception) {
            // raise error when exception
            onError().invoke(ex.message.toString())
            LoadResult.Error(ex)
        }
    }

    companion object {
        /**
         * Paging Starting Page Index
         * The default starting page is 0
         */
        private const val PAGING_STARTING_PAGE_INDEX = 1

        /**
         * Paging Default Size
         * The default size of item on each page
         */
        const val PAGING_DEFAULT_SIZE = 10
    }
}