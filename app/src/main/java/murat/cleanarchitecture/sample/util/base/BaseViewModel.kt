package murat.cleanarchitecture.sample.util.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import murat.cleanarchitecture.sample.util.model.ResultState

/**
 * TODO BaseViewModel?
 */

abstract class BaseViewModel : ViewModel() {

    val loadingErrorState = MutableLiveData<ResultState<Any?>>()

    fun handleTask(task: ResultState<Any?>, callback: () -> Unit = {}) {
        loadingErrorState.postValue(task)
        callback.invoke()
    }
}