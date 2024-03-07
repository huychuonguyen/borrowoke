package com.chuthi.borrowoke.data.paging

import com.chuthi.borrowoke.base.paging.BaseRemoteMediator
import com.chuthi.borrowoke.data.database.dao.UserDao
import com.chuthi.borrowoke.data.database.entity.UserEntity

class UserRemoteMediator(
    private val userDao: UserDao,
) : BaseRemoteMediator<UserEntity>() {
    override suspend fun queryAction(): suspend (Int, Int) -> List<UserEntity>? {
        TODO("Not yet implemented")
    }

    override suspend fun onQueryResult(data: List<UserEntity>?) {
        userDao.insertAll(data ?: listOf())
    }
}