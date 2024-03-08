package com.chuthi.borrowoke.data.repo

import android.util.Log
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.BaseRepo
import com.chuthi.borrowoke.data.database.dao.UserDao
import com.chuthi.borrowoke.data.database.entity.UserEntity
import com.chuthi.borrowoke.data.model.UserModel
import com.chuthi.borrowoke.data.model.toUserEntity
import com.chuthi.borrowoke.data.paging.UserRemoteMediator
import com.chuthi.borrowoke.other.enums.UiText

class UserRepo(
    private val userDao: UserDao
) : BaseRepo() {

    var id = 0
    private val userPagingFlow = pagerConfig(
        mediator = UserRemoteMediator(
            queryAction = { page, _ ->
                (if (page <= 5) {
                    listOf(
                        UserModel(
                            userId = id,
                            name = "huychuong ${id++}",
                            value = UiText.DynamicString("huychuonguyen $id")
                        ),
                        UserModel(
                            userId = id,
                            name = "huychuong ${id++}",
                            value = UiText.DynamicString("huychuonguyen $id")
                        ),
                        UserModel(
                            userId = id,
                            name = "huychuong ${id++}",
                            value = UiText.DynamicString("huychuonguyen $id")
                        ),
                        UserModel(
                            userId = id,
                            name = "huychuong ${id++}",
                            value = UiText.DynamicString("huychuonguyen $id")
                        ),
                        UserModel(
                            userId = id,
                            name = "huychuong ${id++}",
                            value = UiText.DynamicString("huychuonguyen $id")
                        ),
                        UserModel(
                            userId = id,
                            name = "huychuong ${id++}",
                            value = UiText.DynamicString("huychuonguyen $id")
                        ),
                        UserModel(
                            userId = id,
                            name = "huychuong ${id++}",
                            value = UiText.DynamicString("huychuonguyen $id")
                        ),
                        UserModel(
                            userId = id,
                            name = "huychuong ${id++}",
                            value = UiText.DynamicString("huychuonguyen $id")
                        ),
                        UserModel(
                            userId = id,
                            name = "huychuong ${id++}",
                            value = UiText.DynamicString("huychuonguyen $id")
                        ),
                        UserModel(
                            userId = id,
                            name = "huychuong ${id++}",
                            value = UiText.DynamicString("huychuonguyen $id")
                        )

                    )
                } else listOf()).run {
                    map {
                        it.toUserEntity()
                    }
                }

            },
            userDao = userDao
        ),
        pagingSource = {
            userDao.getUserPaging()
        }
    )


    fun getUsersOrderByName() = userDao.getUsersOrderByName()

    suspend fun insertUser(user: UserEntity) = userDao.insert(user)

    suspend fun insertUsers(users: List<UserEntity>) = userDao.insertAll(users)

    suspend fun deleteAll() = userDao.deleteAll()

    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    suspend fun deleteUser(userId: Int) = userDao.deleteUser(userId)

    fun getUsersPaging() = userPagingFlow
    /*pagerConfig {
    UserPagingSource(userDao) { _, _, _ ->
        getDummyUsers().map {
            it.toUserEntity()
        }
    }
}*/

    fun getDummyUsers() = listOf(
        UserModel(
            userId = 0,
            name = "huychuong",
            value = UiText.DynamicString("huychuonguyen 1")
        ),
        UserModel(
            userId = 1,
            name = "huychuong1",
            value = UiText.DynamicString("huychuonguyen 2")
        ),
        UserModel(
            userId = 2,
            name = "huychuong2",
            value = UiText.DynamicString("huychuonguyen 3")
        ),
        UserModel(
            userId = 3,
            name = "huychuong3",
            value = UiText.DynamicString("huychuonguyen 4")
        ),
        UserModel(
            userId = 4,
            name = "huychuong4",
            value = UiText.DynamicString("huychuonguyen 5")
        ),
        UserModel(
            userId = 5,
            name = "huychuong5",
            value = UiText.DynamicString("huychuonguyen 6")
        ),
        UserModel(
            userId = 6,
            name = "huychuong6",
            value = UiText.DynamicString("huychuonguyen 7")
        ),
        UserModel(
            userId = 7,
            name = "huychuong7",
            value = UiText.DynamicString("huychuonguyen 8")
        ),
        UserModel(
            userId = 8,
            name = "huychuong8",
            value = UiText.DynamicString("huychuonguyen 9")
        ),
        UserModel(
            userId = 9,
            name = "huychuong99999",
            value = UiText.StringResource(
                resId = R.string.sample_string,
                "Huy Chương"
            )
        ),
    )

    fun getDummyUsers2() = listOf(
        UserModel(
            userId = 10,
            name = "Medal",
            value = UiText.DynamicString("huychuonguyen 1")
        ),
        UserModel(
            userId = 11,
            name = "huychuong11",
            value = UiText.DynamicString("huychuonguyen 2")
        ),
        UserModel(
            userId = 12,
            name = "huychuong12",
            value = UiText.DynamicString("huychuonguyen 3")
        ),
        UserModel(
            userId = 13,
            name = "huychuong13",
            value = UiText.DynamicString("huychuonguyen 4")
        ),
        UserModel(
            userId = 14,
            name = "huychuong14",
            value = UiText.DynamicString("huychuonguyen 5")
        ),
        UserModel(
            userId = 15,
            name = "huychuong15",
            value = UiText.DynamicString("huychuonguyen 6")
        ),
    )
}