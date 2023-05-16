package com.chuthi.borrowoke.ui.dog

import com.chuthi.borrowoke.base.BaseViewModel
import com.chuthi.borrowoke.data.model.response.DogResponse
import com.chuthi.borrowoke.data.repo.DogRepo
import com.chuthi.borrowoke.ext.apiCall
import com.chuthi.borrowoke.ext.launchViewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DogViewModel(private val dogRepo: DogRepo) : BaseViewModel() {

    private val _dogs = MutableStateFlow<List<DogResponse>>(emptyList())
    val dog = _dogs.asStateFlow()

    init {
        getDogs()
    }

    fun getDogs() = launchViewModelScope {
        showLoading()
        dogRepo.getDogs().apiCall(
            onSuccess = {
                it.data ?: return@apiCall
                _dogs.emit(it.data)
            },
            onError = {
            },
            onFinished = {
                hideLoading()
            }
        )
    }

    fun clearDogs() = launchViewModelScope {
        _dogs.emit(listOf())
    }
}