package com.chus.clua.citybikes.presentation.features.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chus.clua.citybikes.presentation.base.BaseViewModel
import com.chus.clua.citybikes.domain.model.NetWork
import com.chus.clua.citybikes.domain.model.NetWorkStation
import com.chus.clua.citybikes.domain.usecase.GetNetWorkByIdUseCase
import com.chus.clua.citybikes.presentation.mapper.UiModelMapper
import com.chus.clua.citybikes.presentation.models.NetWorkMapUiModel
import com.chus.clua.citybikes.presentation.models.NetWorkStationUiModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MapViewModel(
    private val useCase: GetNetWorkByIdUseCase,
    private val netWorkMapUiModelMapper: UiModelMapper<NetWork, NetWorkMapUiModel>,
    private val netWorkStationUiModelMapper: UiModelMapper<NetWorkStation, NetWorkStationUiModel>,
    private val subscriberScheduler: Scheduler = Schedulers.io(),
    private val observerScheduler: Scheduler = AndroidSchedulers.mainThread()
) : BaseViewModel() {

    private var _currentNetwork: NetWork? = null
        set(value) {
            field = value
            _viewState.postValue(MapViewState.Success(mapNetWork(value)))
        }

    private var _currentNetworkStation: NetWorkStation? = null
        set(value) {
            field = value
            _viewState.postValue(MapViewState.StationDetail(mapNetWorkStation(value)))
        }

    private val _viewState = MutableLiveData<MapViewState>()
    val viewState: LiveData<MapViewState> get() = _viewState

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.LoadAllStations -> loadNetWork(event.netWorkId)
            is MapEvent.MarkerClick -> getNetWorkStationById(event.stationId)
        }
    }

    private fun loadNetWork(netWorkId: String?) {
        _viewState.postValue(MapViewState.Loading)
        getNetWorkById(netWorkId,
            { netWork ->
                netWork?.let { _currentNetwork = it }
            },
            { error ->
                _viewState.postValue(MapViewState.Error(error.message))
            }
        )
    }

    private fun getNetWorkStationById(stationId: String?) {
        val station = _currentNetwork?.stations?.firstOrNull {
            stationId == it.id
        }
        station?.let { _currentNetworkStation = it }
    }

    private fun getNetWorkById(
        netWorkId: String?,
        success: (NetWork?) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        compositeDisposable.add(
            useCase.invoke(netWorkId)
                .subscribeOn(subscriberScheduler)
                .observeOn(observerScheduler)
                .subscribe(
                    { network ->
                        success(network)
                    },
                    { error ->
                        failure(error)
                    }
                )
        )
    }

    private fun mapNetWork(netWork: NetWork?) = netWorkMapUiModelMapper.mapFromDomain(netWork)

    private fun mapNetWorkStation(netWorkStation: NetWorkStation?) =
        netWorkStationUiModelMapper.mapFromDomain(netWorkStation)

}