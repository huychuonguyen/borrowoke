package com.chuthi.borrowoke.ui.main

import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.ext.launchViewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : BaseViewModel() {

    private var _dummyData = MutableStateFlow<List<Int>>(listOf(1,2))
    val dummyData = _dummyData.asStateFlow()

    private var _dummyData2 = MutableStateFlow("conmeno")
    val dummyData2 = _dummyData2.asStateFlow()

    init {
        fetchData()
        fetchDummyData()
    }

    fun fetchData() {
        launchViewModelScope {
            showLoading()
            delay(500)
            hideLoading()
            _dummyData2.emit("Á đù")
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