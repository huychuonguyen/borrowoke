package com.chuthi.borrowoke.ui.main

import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.ext.launchViewModelScope
import com.chuthi.borrowoke.other.enums.CommonError
import com.chuthi.borrowoke.other.enums.HttpError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class MainViewModel : BaseViewModel() {

    private var _dummyData = MutableStateFlow<List<Int>>(listOf(1, 2))
    val dummyData = _dummyData.asStateFlow()

    private var _dummyData2 = MutableStateFlow("conmeno")
    val dummyData2 = _dummyData2.asStateFlow()

    private var _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    private var _errorSharedFlow = MutableSharedFlow<CommonError>()
    val errorSharedFlow = _errorSharedFlow.asSharedFlow()

    init {
        fetchData()
        fetchDummyData()
    }

    fun fetchData() {
        launchViewModelScope {
//            _errorChannel.send("Error nè mày")
//
//            _errorSharedFlow.emit("Error Shared Flow")

//            showLoading()
//            delay(500)
//            hideLoading()
            //_dummyData2.emit("Á đù")
//
//            _errorChannel.send("Error nè mày 2")

            // emit error
            /*  commonError.emit(
                  CommonError.ErrorNormal(
                      UiText.StringResource(R.string.sample_error, "Normal", "Medal")
                  )
              )*/

            commonError.emit(
                HttpError.Unauthorized403
            )

            delay(2000)

            commonError.emit(
                HttpError.Unauthorized401
            )

        }
    }

    private fun fetchDummyData() {
        launchViewModelScope {
            _dummyData.emit(
                listOf(
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10
                )
            )
        }
    }
}