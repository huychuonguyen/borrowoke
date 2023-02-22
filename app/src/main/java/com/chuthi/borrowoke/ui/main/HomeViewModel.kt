package com.chuthi.borrowoke.ui.main

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import androidx.work.workDataOf
import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.data.model.UserModel
import com.chuthi.borrowoke.data.paging.UserPagingSource
import com.chuthi.borrowoke.ext.launchViewModelScope
import com.chuthi.borrowoke.other.INPUT_BLUR_WORKER
import com.chuthi.borrowoke.util.pagerConfig
import com.chuthi.borrowoke.woker.MyWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val myWorker: MyWorker
) : BaseViewModel() {

    val countState = savedStateHandle.getStateFlow(COUNT_STATE_ARG, "nothing")

    private var _firstCounter = MutableStateFlow(1)
    private val firstCounter = _firstCounter.asStateFlow()

    private var _secondCounter = MutableStateFlow(1000)
    private val secondCounter = _secondCounter.asStateFlow()

    private val _userState = MutableStateFlow<List<UserModel>>(mutableListOf())
    val userState = _userState.asStateFlow()

    private val _userPaging = MutableStateFlow<PagingData<UserModel>>(PagingData.empty())
    val userPaging: StateFlow<PagingData<UserModel>>
        get() = _userPaging.asStateFlow()

    val blurImageWorkInfo =
        myWorker.workManager
            .getWorkInfoByIdLiveData(MyWorker.blurWorkRequestId)
            .asFlow()


    fun blurImage(uri: Uri) {
        val inputData = workDataOf(INPUT_BLUR_WORKER to "Blur input nè")
        myWorker.applyBlurWork(inputData)
    }

    fun fetchUsersPaging() = launchViewModelScope {
        _userPaging.emit(
            pagerConfig(pagingSource = {
                UserPagingSource { page, size, user ->
                    getDummyUsers2()
                }
            }).cachedIn(viewModelScope).stateIn(viewModelScope).value
        )
    }

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
        //
        updateUsers(users = getDummyUsers())
        // fetch paging users
        fetchUsersPaging()
        /*launchViewModelScope {
            val data2 = fetchData2()
            val data1 = fetchData1()
            Log.i("home_result", "result2: $data2")
            Log.i("home_result", "result1: $data1")
        }*/
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
        val removePaging = userPaging.value.filter { curUser ->
            curUser.userId != user.userId
        }
        _userPaging.emit(removePaging)
    }

    fun updateUserPaging(userUpdate: UserModel) = launchViewModelScope {
        val updatePaging = userPaging.value.map { curUser ->
            if (curUser.userId == userUpdate.userId)
                userUpdate
            else curUser
        }
        _userPaging.emit(updatePaging)
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
            name = "huychuong"
        ),
        UserModel(
            userId = 1,
            name = "huychuong1"
        ),
        UserModel(
            userId = 2,
            name = "huychuong2"
        ),
        UserModel(
            userId = 3,
            name = "huychuong3"
        ),
        UserModel(
            userId = 4,
            name = "huychuong4"
        ),
        UserModel(
            userId = 5,
            name = "huychuong5"
        ),
        UserModel(
            userId = 6,
            name = "huychuong6"
        ),
        UserModel(
            userId = 7,
            name = "huychuong7"
        ),
        UserModel(
            userId = 8,
            name = "huychuong8"
        ),
        UserModel(
            userId = 9,
            name = "huychuong9"
        ),
        UserModel(
            userId = 10,
            name = "huychuong10"
        ),
        UserModel(
            userId = 11,
            name = "huychuong11"
        ),
        UserModel(
            userId = 12,
            name = "huychuong12"
        ),
        UserModel(
            userId = 13,
            name = "huychuong13"
        ),
        UserModel(
            userId = 14,
            name = "huychuong14"
        ),
    )

    fun getDummyUsers2() = listOf(
        UserModel(
            userId = 50,
            name = "Gumi in my heart 50"
        ),
        UserModel(
            userId = 51,
            name = "Gumi in my heart 51"
        ),
        UserModel(
            userId = 52,
            name = "Gumi in my heart 52"
        ),
        UserModel(
            userId = 53,
            name = "Gumi in my heart 53"
        ),
    )

    companion object {
        private const val COUNT_STATE_ARG = "COUNT_STATE_ARG"
    }
}