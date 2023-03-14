package com.chuthi.borrowoke.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.chuthi.borrowoke.base.paging.BasePagingSource
import kotlinx.coroutines.flow.Flow

open class BaseRepo {
    /**
     * PagerConfig.
     *
     * Config paging source
     *
     * @param pageSize[Int] size on each page
     * @param pagingSource[BasePagingSource<T>] the paging source to apply config
     * @return Flow<PagingData<T>>
     * */
    protected fun <T : BaseModel> pagerConfig(
        pageSize: Int = BasePagingSource.PAGING_DEFAULT_SIZE,
        pagingSource: () -> BasePagingSource<T>
    ): Flow<PagingData<T>> {
        return Pager(PagingConfig(pageSize = pageSize, enablePlaceholders = false)) {
            pagingSource.invoke()
        }.flow
    }
}