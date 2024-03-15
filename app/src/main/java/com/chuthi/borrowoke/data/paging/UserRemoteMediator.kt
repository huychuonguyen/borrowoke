package com.chuthi.borrowoke.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import com.chuthi.borrowoke.base.paging.BaseRemoteMediator
import com.chuthi.borrowoke.data.database.dao.RemoteKeysDao
import com.chuthi.borrowoke.data.database.dao.UserDao
import com.chuthi.borrowoke.data.database.entity.RemoteKeysEntity
import com.chuthi.borrowoke.data.database.entity.UserEntity

/**************************************
- Created by Chuong Nguyen
- Email : huychuonguyen@gmail.com
- Date : 078/03/2024
- Project : Base Kotlin
 **************************************/
class UserRemoteMediator(
    private val queryAction: suspend (Int, Int) -> List<UserEntity>?,
    private val userDao: UserDao,
    private val remoteKeysDao: RemoteKeysDao
) : BaseRemoteMediator<UserEntity>() {

    override suspend fun queryAction(): suspend (Int, Int) -> List<UserEntity>? = queryAction

    @ExperimentalPagingApi
    override suspend fun onQueryResult(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>,
        data: List<UserEntity>?
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: PAGING_STARTING_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        val endOfPaginationReached = data?.isEmpty() == true
        data?.let {
            // clear all tables in the database
            if (loadType == LoadType.REFRESH) {
                remoteKeysDao.clearRemoteKeys()
                userDao.deleteAll()
            }
            val prevKey = if (page == PAGING_STARTING_PAGE_INDEX) null else page - 1
            val nextKey = if (endOfPaginationReached) null else page + 1
            val keys = data.map {
                RemoteKeysEntity(id = it.userId, prevKey = prevKey, nextKey = nextKey)
            }
            remoteKeysDao.insertAll(keys)
            userDao.insertAll(data)
        }

        return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
    }

    override suspend fun setEndPaging(data: List<UserEntity>?, page: Int): Boolean {
        return data?.isEmpty() ?: true
    }


    //=================================
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UserEntity>): RemoteKeysEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                remoteKeysDao.remoteKeysRepoId(repo.userId)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UserEntity>): RemoteKeysEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                remoteKeysDao.remoteKeysRepoId(repo.userId)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, UserEntity>
    ): RemoteKeysEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.userId?.let { repoId ->
                remoteKeysDao.remoteKeysRepoId(repoId)
            }
        }
    }

    companion object {
        private const val PAGING_STARTING_PAGE_INDEX = 0
    }
}