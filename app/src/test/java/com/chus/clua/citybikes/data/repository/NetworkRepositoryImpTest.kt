package com.chus.clua.citybikes.data.repository

import com.chus.clua.citybikes.data.mapper.NetWorkDomainMapper
import com.chus.clua.citybikes.data.model.NetWorkDetailRemoteModel
import com.chus.clua.citybikes.data.model.NetWorksRemoteModel
import com.chus.clua.citybikes.data.service.CityBikesService
import com.chus.clua.citybikes.domain.model.NetWork
import com.chus.clua.citybikes.domain.repository.NetworkRepository
import com.chus.clua.citybikes.presentation.utils.BICIMAD_ID
import com.chus.clua.citybikes.presentation.utils.NETWORKS_JSON
import com.chus.clua.citybikes.presentation.utils.NETWORK_JSON
import com.chus.clua.citybikes.presentation.utils.SPAIN_COUNTRY_CODE
import com.chus.clua.citybikes.presentation.utils.mocks.RemoteModelGenerator.getEntityFromFile
import com.chus.clua.citybikes.presentation.utils.mocks.mockNetWorks
import com.chus.clua.citybikes.presentation.utils.mocks.mockNetwork
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class NetworkRepositoryImpTest {

    @Mock
    private lateinit var service: CityBikesService
    @Mock
    private lateinit var mapper: NetWorkDomainMapper
    private lateinit var repo: NetworkRepository
    private val observerList = TestObserver<List<NetWork>>()
    private val observerDetail = TestObserver<NetWork>()

    private lateinit var netWorksRemoteModel: NetWorksRemoteModel
    private lateinit var netWorkDetailRemoteModel: NetWorkDetailRemoteModel

    private val networkID by lazy { BICIMAD_ID }
    private val countryCode by lazy { SPAIN_COUNTRY_CODE }

    @Before
    fun setUp() {
        repo = NetworkRepositoryImp(service, mapper)
        netWorksRemoteModel = getEntityFromFile(NETWORKS_JSON)
        netWorkDetailRemoteModel = getEntityFromFile(NETWORK_JSON)
    }

    @Test
    fun `WHEN NetworkRepository getNetworks THEN obtains a list of NetWork`() {
        whenever(service.getNetWorks()).thenReturn(Single.just(netWorksRemoteModel))
        whenever(mapper.mapFromRemoteList(netWorksRemoteModel.networks)).thenReturn(mockNetWorks)
        repo.getNetworks().subscribe(observerList)

        observerList.assertComplete()
        observerList.assertNoErrors()
        observerList.assertValueCount(1)

        val networks = observerList.values().first()
        assertEquals(mockNetWorks, networks)
    }

    @Test
    fun `WHEN NetworkRepository getNetworks THEN obtains an error`() {
        val error = Throwable("exception_message")
        whenever(service.getNetWorks()).thenReturn(Single.error(error))
        repo.getNetworks().subscribe(observerList)

        observerList.assertError(error)
        observerList.assertErrorMessage("exception_message")
    }

    @Test
    fun `WHEN NetworkRepository getNetworksByCountry ES THEN obtains a list of NetWork`() {
        whenever(service.getNetWorks()).thenReturn(Single.just(netWorksRemoteModel))
        whenever(mapper.mapFromRemoteList(netWorksRemoteModel.networks)).thenReturn(mockNetWorks)
        repo.getNetworksByCountry(countryCode).subscribe(observerList)

        observerList.assertComplete()
        observerList.assertNoErrors()
        observerList.assertValueCount(1)

        val networks = observerList.values().first()
        networks.forEach {
            assertTrue(countryCode.equals(it.location?.country, ignoreCase = true))
        }
    }

    @Test
    fun `WHEN NetworkRepository getNetworksByCountry ES THEN obtains an error`() {
        val error = Throwable("exception_message")
        whenever(service.getNetWorks()).thenReturn(Single.error(error))
        repo.getNetworksByCountry(countryCode).subscribe(observerList)

        observerList.assertError(error)
        observerList.assertErrorMessage("exception_message")
    }

    @Test
    fun `WHEN NetworkRepository getNetworkById THEN obtains a NetWork`() {
        whenever(service.getNetWorkById(networkID)).thenReturn(Single.just(netWorkDetailRemoteModel))
        whenever(mapper.mapFromRemote(netWorkDetailRemoteModel.network)).thenReturn(mockNetwork)
        repo.getNetworkById(networkID).subscribe(observerDetail)

        observerDetail.assertComplete()
        observerDetail.assertNoErrors()
        observerDetail.assertValueCount(1)

        val network = observerDetail.values().first()
        assertEquals(mockNetwork, network)

    }

    @Test
    fun `WHEN NetworkRepository getNetworkById THEN obtains an error`() {
        val error = Throwable("exception_message")
        whenever(service.getNetWorkById(networkID)).thenReturn(Single.error(error))
        repo.getNetworkById(networkID).subscribe(observerDetail)

        observerDetail.assertError(error)
        observerDetail.assertErrorMessage("exception_message")
    }

}