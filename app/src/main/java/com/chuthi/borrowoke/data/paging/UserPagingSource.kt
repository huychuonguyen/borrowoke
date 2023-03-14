package com.chuthi.borrowoke.data.paging

import com.chuthi.borrowoke.base.paging.BasePagingSource
import com.chuthi.borrowoke.data.database.dao.UserDao
import com.chuthi.borrowoke.data.database.entity.UserEntity

class UserPagingSource(
    private val userDao: UserDao,
    private val queryAction: suspend (page: Int, size: Int, UserEntity?)
    -> List<UserEntity>?
) : BasePagingSource<UserEntity>() {

    override fun onError(): (String) -> Unit = {}

    override fun submitAction(): suspend (Int, Int, UserEntity?) -> List<UserEntity>? =
        { page, size, entity ->
            val userEntities = queryAction.invoke(page, size, entity)?.also {
                userDao.insertAll(it)
            }
            userEntities
        }
}