package com.chus.clua.citybikes.presentation.features.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chus.clua.citybikes.domain.model.NetWork
import com.chus.clua.citybikes.domain.model.NetWorkStation
import com.chus.clua.citybikes.domain.usecase.GetNetWorkByIdUseCase
import com.chus.clua.citybikes.presentation.mapper.UiModelMapper
import com.chus.clua.citybikes.presentation.models.NetWorkMapUiModel
import com.chus.clua.citybikes.presentation.models.NetWorkStationUiModel
import com.chus.clua.citybikes.presentation.utils.BICING_ID
import com.chus.clua.citybikes.presentation.utils.STATION_ID
import com.chus.clua.citybikes.presentation.utils.mocks.mockNetWorkMapUiModel
import com.chus.clua.citybikes.presentation.utils.mocks.mockNetWorkStation
import com.chus.clua.citybikes.presentation.utils.mocks.mockNetWorkStationUiModel
import com.chus.clua.citybikes.presentation.utils.mocks.mockNetwork
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: GetNetWorkByIdUseCase

    @Mock
    lateinit var observer: Observer<MapViewState>

    @Mock
    private lateinit var netWorkMapUiModelMapper: UiModelMapper<NetWork, NetWorkMapUiModel>

    @Mock
    private lateinit var netWorkStationUiModelMapper: UiModelMapper<NetWorkStation, NetWorkStationUiModel>

    private lateinit var subscriberScheduler: TestScheduler
    private lateinit var observerScheduler: TestScheduler
    private lateinit var viewModel: MapViewModel

    @Before
    fun setUp() {
        subscriberScheduler = TestScheduler()
        observerScheduler = TestScheduler()
        viewModel = MapViewModel(
            useCase,
            netWorkMapUiModelMapper,
            netWorkStationUiModelMapper,
            subscriberScheduler,
            observerScheduler
        )
        viewModel.viewState.observeForever(observer)
    }

    @Test
    fun `WHEN MapEvent is LoadAllStations THEN obtains a MapViewState Success`() {
        whenever(useCase.invoke(BICING_ID)).thenReturn(
            Single.just(mockNetwork)
        )
        whenever(netWorkMapUiModelMapper.mapFromDomain(mockNetwork)).thenReturn(
            mockNetWorkMapUiModel
        )

        viewModel.onEvent(MapEvent.LoadAllStations(BICING_ID))

        verify(observer).onChanged(MapViewState.Loading)

        subscriberScheduler.triggerActions()
        observerScheduler.triggerActions()

        verify(useCase, times(1)).invoke(BICING_ID)

        verify(observer).onChanged(MapViewState.Success(mockNetWorkMapUiModel))

    }

    @Test
    fun `WHEN MapEvent is MarkerClick THEN obtains a MapViewState StationDetail`() {
        whenever(useCase.invoke(BICING_ID)).thenReturn(
            Single.just(mockNetwork)
        )
        whenever(netWorkStationUiModelMapper.mapFromDomain(mockNetWorkStation)).thenReturn(
            mockNetWorkStationUiModel
        )

        viewModel.onEvent(MapEvent.LoadAllStations(BICING_ID))

        subscriberScheduler.triggerActions()
        observerScheduler.triggerActions()

        viewModel.onEvent(MapEvent.MarkerClick(STATION_ID))

        verify(observer).onChanged(MapViewState.StationDetail(mockNetWorkStationUiModel))

    }

    @Test
    fun `WHEN MapEvent is LoadAllStations THEN obtains a MapViewState Error`() {
        val error = Throwable("error_message")

        whenever(useCase.invoke(BICING_ID)).thenReturn(Single.error(error))

        viewModel.onEvent(MapEvent.LoadAllStations(BICING_ID))

        verify(observer).onChanged(MapViewState.Loading)

        subscriberScheduler.triggerActions()
        observerScheduler.triggerActions()

        verify(useCase, times(1)).invoke(BICING_ID)

        verify(observer).onChanged(MapViewState.Error(error.message))


    }

    @Test
    fun `WHEN MapEvent is MarkerClick THEN obtains nothing`() {

        viewModel.onEvent(MapEvent.MarkerClick(""))

        verify(observer, never()).onChanged(MapViewState.StationDetail(mockNetWorkStationUiModel))

    }

}