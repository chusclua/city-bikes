package com.chus.clua.citybikes.presentation.features.networkslist

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chus.clua.citybikes.presentation.base.BaseViewModel
import com.chus.clua.citybikes.R
import com.chus.clua.citybikes.domain.model.NetWork
import com.chus.clua.citybikes.domain.usecase.GetAllNetWorksUseCase
import com.chus.clua.citybikes.domain.usecase.GetNetWorksByCountryUseCase
import com.chus.clua.citybikes.presentation.mapper.UiModelMapper
import com.chus.clua.citybikes.presentation.models.NetWorkListUiModel
import com.chus.clua.citybikes.presentation.utils.ComparatorNetWorkByLocation
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class NetWorksListViewModel(
    private val getNetWorksByCountryUseCase: GetNetWorksByCountryUseCase,
    private val getAllNetWorksUseCase: GetAllNetWorksUseCase,
    private val netWorkListUiModelMapper: UiModelMapper<NetWork, NetWorkListUiModel>,
    private val subscriberScheduler: Scheduler = Schedulers.io(),
    private val observerScheduler: Scheduler = AndroidSchedulers.mainThread()
) : BaseViewModel() {

    private var _currentNetworks: List<NetWork>? = null
        set(value) {
            field = value
            _viewState.postValue(NetWorkListState.Success(mapNetWorks(value)))
        }

    private val _viewState = MutableLiveData<NetWorkListState>()
    val viewState: LiveData<NetWorkListState> get() = _viewState

    fun onEvent(event: NetWorkListEvent) {
        when (event) {
            NetWorkListEvent.LoadAllNetWorks -> getAllNetWorks()
            is NetWorkListEvent.LoadNearbyNetWorks -> getNearbyNetworks(
                event.countryCode,
                event.location
            )
            is NetWorkListEvent.FilterNetWorksByQuery -> filterNetworksBy(event.query)
        }
    }

    private fun getAllNetWorks() {
        _viewState.postValue(NetWorkListState.Loading)
        getNetWorks(
            { netWorks ->
                _currentNetworks = netWorks
            },
            { error ->
                _viewState.postValue(NetWorkListState.Error(error.message))
            })
    }

    private fun getNearbyNetworks(countryCode: String?, location: Location?) {
        _viewState.postValue(NetWorkListState.Loading)
        getNetWorksByCountryCode(countryCode,
            { netWorks ->
                sortNetWorksByNameAndLocation(netWorks, location)
                _currentNetworks = netWorks
            },
            { error ->
                _viewState.postValue(NetWorkListState.Error(error.message))
            }
        )
    }

    private fun filterNetworksBy(query: String?) = when (query?.isBlank()) {
        false -> {
            val filtered = filterNetworks(query)
            when {
                filtered.isNullOrEmpty() -> _viewState.postValue(NetWorkListState.Error(R.string.empty_list_error))
                else -> _viewState.postValue(
                    NetWorkListState.Success(filtered)
                )
            }
        }
        else -> _viewState.postValue(
            NetWorkListState.Success(mapNetWorks(_currentNetworks))
        )
    }

    private fun getNetWorks(
        success: (List<NetWork>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        compositeDisposable.add(
            getAllNetWorksUseCase.invoke()
                .subscribeOn(subscriberScheduler)
                .observeOn(observerScheduler)
                .subscribe(
                    { list ->
                        success(list)
                    },
                    { error ->
                        failure(error)
                    }
                )
        )
    }

    private fun getNetWorksByCountryCode(
        countryCode: String?,
        success: (List<NetWork>) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        compositeDisposable.add(
            getNetWorksByCountryUseCase.invoke(countryCode)
                .subscribeOn(subscriberScheduler)
                .observeOn(observerScheduler)
                .subscribe(
                    { list ->
                        success(list)
                    },
                    { error ->
                        failure(error)
                    }
                )
        )
    }

    private fun filterNetworks(query: String) =
        mapNetWorks(_currentNetworks?.filter { network ->
            network.name?.contains(query, true) ?: false
        }, query)

    private fun mapNetWorks(list: List<NetWork>?, query: String? = null): List<NetWorkListUiModel> {
        return query?.let {
            netWorkListUiModelMapper.mapFromDomainList(list).map { it.copy(query = query) }
        } ?: run {
            netWorkListUiModelMapper.mapFromDomainList(list)
        }
    }

    private fun sortNetWorksByNameAndLocation(
        netWorks: List<NetWork>,
        currentPosition: Location?
    ): List<NetWork> {
        netWorks.sortedBy { it.name }
        currentPosition?.let { location ->
            Collections.sort(netWorks, ComparatorNetWorkByLocation(location))
        }
        return netWorks
    }

}