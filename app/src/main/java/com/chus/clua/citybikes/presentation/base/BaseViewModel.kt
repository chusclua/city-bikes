package com.chus.clua.citybikes.presentation.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}