package com.chuthi.borrowoke.base.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.chuthi.borrowoke.base.BaseModel
import retrofit2.HttpException
import java.io.IOException


/**************************************
- Created by Chuong Nguyen
- Email : huychuonguyen@gmail.com
- Date : 07/03/2024
- Project : Base Kotlin
 **************************************/
@OptIn(ExperimentalPagingApi::class)
abstract class BaseRemoteMediator<T : BaseModel> : RemoteMediator<Int, T>() {

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

    /**
     * override on extended paging source and implement paging api,
     * callback to raise api calling with BaseResponseModel<List<T>> response,
     * @return BaseResponseModel<List<T>>?
     * */
    abstract suspend fun queryAction(): suspend (Int, Int) -> List<T>?

    /**
     * [onQueryResult] raise this callback when [queryAction] return data.
     * Used for update database.
     */
    abstract suspend fun onQueryResult(
        loadType: LoadType,
        state: PagingState<Int, T>,
        data: List<T>?
    ) : MediatorResult

    abstract suspend fun setEndPaging(data: List<T>?, page: Int): Boolean

    private var page = 0
    override suspend fun load(loadType: LoadType, state: PagingState<Int, T>): MediatorResult {
        /*page = when (loadType) {
            LoadType.REFRESH -> {
                // TODO
                0
            }

            LoadType.PREPEND -> {
                // TODO
                0
            }

            LoadType.APPEND -> {
                // TODO
                0
            }

            else -> PAGING_STARTING_PAGE_INDEX
        }*/

        return try {
            val data = queryAction().invoke(page, PAGING_DEFAULT_SIZE)
            // raise callback result
            onQueryResult(loadType, state, data)

          /*  val endOfPaginationReached = setEndPaging(data, page)

            page++
            // return
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)*/

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}