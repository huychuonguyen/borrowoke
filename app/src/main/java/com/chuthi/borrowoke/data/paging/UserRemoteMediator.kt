package com.chuthi.borrowoke.data.paging

import com.chuthi.borrowoke.base.paging.BaseRemoteMediator
import com.chuthi.borrowoke.data.database.dao.UserDao
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
) : BaseRemoteMediator<UserEntity>() {

    override suspend fun queryAction(): suspend (Int, Int) -> List<UserEntity>? = queryAction

    override suspend fun onQueryResult(data: List<UserEntity>?) {
        data?.let { userDao.insertAll(it) }

    }

    override suspend fun setEndPaging(data: List<UserEntity>?, page: Int): Boolean {
        return data?.isEmpty() ?: true
    }
}