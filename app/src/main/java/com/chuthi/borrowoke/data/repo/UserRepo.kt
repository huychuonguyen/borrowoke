package com.chuthi.borrowoke.data.repo

import com.chuthi.borrowoke.base.BaseRepo
import com.chuthi.borrowoke.data.database.dao.UserDao
import com.chuthi.borrowoke.data.database.entity.UserEntity
import com.chuthi.borrowoke.data.model.UserModel
import com.chuthi.borrowoke.data.model.toUserEntity
import com.chuthi.borrowoke.data.paging.UserPagingSource

class UserRepo(
    private val userDao: UserDao
) : BaseRepo() {

    fun getUsersOrderByName() = userDao.getUsersOrderByName()

    suspend fun insertUser(user: UserEntity) = userDao.insert(user)

    suspend fun insertUsers(users: List<UserEntity>) = userDao.insertAll(users)

    suspend fun deleteAll() = userDao.deleteAll()

    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    suspend fun deleteUser(userId: Int) = userDao.deleteUser(userId)

    fun getUsersPaging() = pagerConfig {
        UserPagingSource(userDao) { _, _, _ ->
            getDummyUsers().map {
                it.toUserEntity()
            }
        }
    }

    private fun getDummyUsers() = listOf(
        UserModel(
            id = 50,
            userId = 50,
            name = "Gumi in my heart 50"
        ),
        UserModel(
            id = 51,
            userId = 51,
            name = "Gumi in my heart 51"
        ),
        UserModel(
            id = 52,
            userId = 52,
            name = "Gumi in my heart 52"
        ),
        UserModel(
            id = 53,
            userId = 53,
            name = "Gumi in my heart 53"
        ),
    )
}