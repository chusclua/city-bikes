package com.chus.clua.citybikes.data.service


import com.chus.clua.citybikes.data.model.NetWorkDetailRemoteModel
import com.chus.clua.citybikes.data.model.NetWorksRemoteModel
import com.chus.clua.citybikes.presentation.utils.service.BaseServiceTest
import com.chus.clua.citybikes.presentation.utils.BICING_ID
import io.reactivex.observers.TestObserver
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CityBikesServiceTest: BaseServiceTest() {

    private val netWorksObserver = TestObserver<NetWorksRemoteModel>()
    private val netWorkObserver = TestObserver<NetWorkDetailRemoteModel>()
    private lateinit var service: CityBikesService

    @Before
    override fun setUp() {
        super.setUp()
        service = CityBikesService(api)
    }

    @Test
    fun `WHEN CityBikesService getNetWorks THEN obtains a NetWorksRemoteModel`() {
        service.getNetWorks().subscribe(netWorksObserver)
        netWorksObserver.assertComplete()
        netWorksObserver.assertNoErrors()
        netWorksObserver.assertTerminated()

        val dataResult = netWorksObserver.values().firstOrNull()
        assertThat(dataResult, IsInstanceOf.instanceOf(NetWorksRemoteModel::class.java))
        assertEquals(644, dataResult?.networks?.size)

    }

    @Test
    fun `WHEN CityBikesService getNetworkById THEN obtains a NetWorkDetailRemoteModel`() {
        service.getNetWorkById(BICING_ID).subscribe(netWorkObserver)

        netWorkObserver.assertComplete()
        netWorkObserver.assertNoErrors()
        netWorkObserver.assertTerminated()

        val dataResult = netWorkObserver.values().firstOrNull()
        assertThat(dataResult, IsInstanceOf.instanceOf(NetWorkDetailRemoteModel::class.java))
        assertEquals(499, dataResult?.network?.stations?.size)

    }
}