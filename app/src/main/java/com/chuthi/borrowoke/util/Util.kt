package com.chuthi.borrowoke.util

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.chuthi.borrowoke.base.BaseModel
import com.chuthi.borrowoke.base.paging.BasePagingSource
import com.chuthi.borrowoke.base.paging.BasePagingSource.Companion.PAGING_DEFAULT_SIZE
import kotlinx.coroutines.flow.Flow

/**
 * PagerConfig.
 *
 * Config paging source
 *
 * @param pageSize[Int] size on each page
 * @param pagingSource[BasePagingSource<T>] the paging source to apply config
 * @return Flow<PagingData<T>>
 * */
fun <T : BaseModel> pagerConfig(
    pageSize: Int = PAGING_DEFAULT_SIZE,
    pagingSource: () -> BasePagingSource<T>
): Flow<PagingData<T>> {
    return Pager(PagingConfig(pageSize = pageSize, enablePlaceholders = false)) {
        pagingSource.invoke()
    }.flow
}