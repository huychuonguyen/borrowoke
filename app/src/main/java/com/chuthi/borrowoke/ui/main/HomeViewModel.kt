package com.chuthi.borrowoke.ui.main

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import androidx.work.workDataOf
import com.chuthi.borrowoke.R
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.data.database.entity.toUserModel
import com.chuthi.borrowoke.data.model.UserModel
import com.chuthi.borrowoke.data.model.toUserEntity
import com.chuthi.borrowoke.data.repo.UserRepo
import com.chuthi.borrowoke.ext.launchViewModelScope
import com.chuthi.borrowoke.other.INPUT_BLUR_WORKER
import com.chuthi.borrowoke.other.enums.UiText
import com.chuthi.borrowoke.woker.MyWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val myWorker: MyWorker,
    private val userRepo: UserRepo
) : BaseViewModel() {

    val countState = savedStateHandle.getStateFlow(COUNT_STATE_ARG, "nothing")

    private var _firstCounter = MutableStateFlow(1)
    private val firstCounter = _firstCounter.asStateFlow()

    private var _secondCounter = MutableStateFlow(1000)
    private val secondCounter = _secondCounter.asStateFlow()

    private val _userState = MutableStateFlow<List<UserModel>>(mutableListOf())
    val userState = _userState.asStateFlow()

    private val allUserEntity = userRepo.getUsersOrderByName()

    /**
     * Observe user paging as Flow
     */
    val userPaging = userRepo.getUsersPaging().map {
        it.map { entity ->
            entity.toUserModel()
        }
    }
    /*allUserEntity.map { userEntities ->
        PagingData.from(userEntities.map { userEntity ->
            userEntity.toUserModel()
        })
    }*/

    val blurImageWorkInfo =
        myWorker.workManager
            .getWorkInfoByIdLiveData(MyWorker.blurWorkRequestId)
            .asFlow()


    val counter = combine(
        firstCounter,
        secondCounter
    ) { first, second ->
        val total = first + second
        // return total
        total
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    init {
        // apply blur worker
        blurImage(Uri.EMPTY)
        // insert users
        insertUsers(getDummyUsers())
        // update users
        updateUsers(getDummyUsers())


        /*launchViewModelScope {
            val data2 = fetchData2()
            val data1 = fetchData1()
            Log.i("home_result", "result2: $data2")
            Log.i("home_result", "result1: $data1")
        }*/
    }

    fun insertUser(user: UserModel) = launchViewModelScope {
        userRepo.insertUser(user.toUserEntity())
    }

    private fun insertUsers(users: List<UserModel>) = launchViewModelScope {
        userRepo.insertUsers(users.map {
            it.toUserEntity()
        })
    }

    fun clearUsers() = launchViewModelScope {
        userRepo.deleteAll()
    }


    fun blurImage(uri: Uri) {
        val inputData = workDataOf(INPUT_BLUR_WORKER to "Blur input nè")
        myWorker.applyBlurWork(inputData)
    }

    fun increaseFirstCounter() = launchViewModelScope {
        val count = _firstCounter.value + 1
        _firstCounter.emit(count)
    }

    fun increaseSecondCounter() = launchViewModelScope {
        val count = _secondCounter.value + 100
        _secondCounter.emit(count)
    }

    private suspend fun fetchData1(): Int {
        delay(1000)
        return 1
    }

    private suspend fun fetchData2(): Int {
        delay(1000)
        return 2
    }

    fun saveStateTitle(value: String) {
        savedStateHandle[COUNT_STATE_ARG] = value
    }

    fun updateUsers(users: List<UserModel>) =
        launchViewModelScope {
            _userState.emit(users)
        }

    fun removeUser(user: UserModel) = launchViewModelScope {
        _userState.update {
            it.filter { curUser ->
                curUser.userId != user.userId
            }
        }
    }

    fun removeUserPaging(user: UserModel) = launchViewModelScope {
        userRepo.deleteUser(user.userId)
    }

    fun updateUserPaging(userUpdate: UserModel) = launchViewModelScope {
        userRepo.updateUser(userUpdate.toUserEntity())
    }

    fun updateUser(userUpdate: UserModel) = launchViewModelScope {
        _userState.update {
            it.map { curUser ->
                if (curUser.userId == userUpdate.userId)
                    userUpdate
                else curUser
            }
        }
    }

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
            name = "huychuong9",
            value = UiText.StringResource(
                resId = R.string.sample_string,
                "Huy Chương"
            )
        ),
    )

    companion object {
        private const val COUNT_STATE_ARG = "COUNT_STATE_ARG"
    }
}