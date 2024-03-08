package com.chuthi.borrowoke.base

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.chuthi.borrowoke.base.paging.BasePagingSource
import com.chuthi.borrowoke.base.paging.BaseRemoteMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

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

    @OptIn(ExperimentalPagingApi::class)
    protected fun <T : BaseModel> pagerConfig(
        pageSize: Int = BasePagingSource.PAGING_DEFAULT_SIZE,
        mediator: BaseRemoteMediator<T>? = null,
        pagingSource: () -> PagingSource<Int, T>
    ): Flow<PagingData<T>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            remoteMediator = mediator,
            pagingSourceFactory = pagingSource
        ).flow

    }
}