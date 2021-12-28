package com.chus.clua.citybikes.presentation.features.networkslist

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chus.clua.citybikes.R
import com.chus.clua.citybikes.domain.model.NetWork
import com.chus.clua.citybikes.domain.usecase.GetAllNetWorksUseCase
import com.chus.clua.citybikes.domain.usecase.GetNetWorksByCountryUseCase
import com.chus.clua.citybikes.presentation.mapper.UiModelMapper
import com.chus.clua.citybikes.presentation.models.NetWorkListUiModel
import com.chus.clua.citybikes.presentation.utils.SPAIN_COUNTRY_CODE
import com.chus.clua.citybikes.presentation.utils.mocks.mockNetWorkListUiModel
import com.chus.clua.citybikes.presentation.utils.mocks.mockNetWorks
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
import org.powermock.reflect.Whitebox

@RunWith(MockitoJUnitRunner::class)
class NetWorksListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var location: Location

    @Mock
    private lateinit var getNetWorksByCountryUseCase: GetNetWorksByCountryUseCase

    @Mock
    private lateinit var getAllNetWorksUseCase: GetAllNetWorksUseCase

    @Mock
    private lateinit var netWorkListUiModelMapper: UiModelMapper<NetWork, NetWorkListUiModel>

    @Mock
    lateinit var observer: Observer<NetWorkListState>

    private lateinit var subscriberScheduler: TestScheduler
    private lateinit var observerScheduler: TestScheduler
    private lateinit var viewModel: NetWorksListViewModel

    @Before
    fun setUp() {
        subscriberScheduler = TestScheduler()
        observerScheduler = TestScheduler()
        viewModel = NetWorksListViewModel(
            getNetWorksByCountryUseCase,
            getAllNetWorksUseCase,
            netWorkListUiModelMapper,
            subscriberScheduler,
            observerScheduler
        )
        viewModel.viewState.observeForever(observer)
    }

    @Test
    fun `WHEN NetWorkListEvent is LoadAllNetWorks THEN obtains a NetWorkListState Success`() {
        whenever(getAllNetWorksUseCase.invoke()).thenReturn(
            Single.just(mockNetWorks)
        )
        whenever(netWorkListUiModelMapper.mapFromDomainList(mockNetWorks)).thenReturn(
            listOf(mockNetWorkListUiModel)
        )

        viewModel.onEvent(NetWorkListEvent.LoadAllNetWorks)

        verify(observer).onChanged(NetWorkListState.Loading)

        subscriberScheduler.triggerActions()
        observerScheduler.triggerActions()

        verify(getAllNetWorksUseCase, times(1)).invoke()

        verify(observer).onChanged(
            NetWorkListState.Success(listOf(mockNetWorkListUiModel))
        )

    }

    @Test
    fun `WHEN NetWorkListEvent is LoadNearbyNetWorks THEN obtains a NetWorkListState Success`() {
        whenever(getNetWorksByCountryUseCase.invoke(SPAIN_COUNTRY_CODE)).thenReturn(
            Single.just(mockNetWorks)
        )
        whenever(netWorkListUiModelMapper.mapFromDomainList(mockNetWorks)).thenReturn(
            listOf(mockNetWorkListUiModel)
        )

        viewModel.onEvent(NetWorkListEvent.LoadNearbyNetWorks(SPAIN_COUNTRY_CODE, location))

        verify(observer).onChanged(NetWorkListState.Loading)

        subscriberScheduler.triggerActions()
        observerScheduler.triggerActions()

        verify(getNetWorksByCountryUseCase, times(1)).invoke(SPAIN_COUNTRY_CODE)

        verify(observer).onChanged(
            NetWorkListState.Success(listOf(mockNetWorkListUiModel))
        )

    }

    @Test
    fun `WHEN NetWorkListEvent is LoadNearbyNetWorks THEN obtains a NetWorkListState Error`() {
        val error = Throwable("error_message")

        whenever(getNetWorksByCountryUseCase.invoke(SPAIN_COUNTRY_CODE)).thenReturn(
            Single.error(error)
        )

        viewModel.onEvent(NetWorkListEvent.LoadNearbyNetWorks(SPAIN_COUNTRY_CODE, location))

        verify(observer).onChanged(NetWorkListState.Loading)

        subscriberScheduler.triggerActions()
        observerScheduler.triggerActions()

        verify(getNetWorksByCountryUseCase, times(1)).invoke(SPAIN_COUNTRY_CODE)

        verify(observer).onChanged(NetWorkListState.Error(error.message))

    }

    @Test
    fun `WHEN NetWorkListEvent is LoadAllNetWorks THEN obtains a NetWorkListState Error`() {
        val error = Throwable("error_message")

        whenever(getAllNetWorksUseCase.invoke()).thenReturn(Single.error(error))

        viewModel.onEvent(NetWorkListEvent.LoadAllNetWorks)

        verify(observer).onChanged(NetWorkListState.Loading)

        subscriberScheduler.triggerActions()
        observerScheduler.triggerActions()

        verify(getAllNetWorksUseCase, times(1)).invoke()

        verify(observer).onChanged(NetWorkListState.Error(error.message))

    }

    @Test
    fun `WHEN NetWorkListEvent is FilterNetWorksByQuery THEN obtains NetWorkListState Success`() {
        val query = "Bicin"
        Whitebox.setInternalState(viewModel, "_currentNetworks", mockNetWorks)
        whenever(netWorkListUiModelMapper.mapFromDomainList(listOf(mockNetWorks[2]))).thenReturn(
            listOf(mockNetWorkListUiModel)
        )
        viewModel.onEvent(NetWorkListEvent.FilterNetWorksByQuery(query))

        verify(observer).onChanged(NetWorkListState.Success(listOf(mockNetWorkListUiModel.copy(query = query))))

    }

    @Test
    fun `WHEN NetWorkListEvent is FilterNetWorksByQuery THEN obtains NetWorkListState Error`() {
        viewModel.onEvent(NetWorkListEvent.FilterNetWorksByQuery("xxx"))

        verify(observer).onChanged(NetWorkListState.Error(R.string.empty_list_error))

    }

}