package com.chus.clua.citybikes.data.service

import com.chus.clua.citybikes.data.model.NetWorkDetailRemoteModel
import com.chus.clua.citybikes.data.model.NetWorksRemoteModel
import com.chus.clua.citybikes.presentation.utils.BICING_ID
import com.chus.clua.citybikes.presentation.utils.service.BaseServiceTest
import io.reactivex.observers.TestObserver
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertEquals
import org.junit.Test

class CityBikesApiTest: BaseServiceTest() {

    private val netWorksObserver = TestObserver<NetWorksRemoteModel>()
    private val netWorkObserver = TestObserver<NetWorkDetailRemoteModel>()

    @Test
    fun `WHEN CityBikesApi getNetworks THEN obtains a NetWorksRemoteModel`() {
        api.getNetworks().subscribe(netWorksObserver)

        netWorksObserver.assertComplete()
        netWorksObserver.assertNoErrors()
        netWorksObserver.assertTerminated()

        val dataResult = netWorksObserver.values().firstOrNull()
        assertThat(dataResult, IsInstanceOf.instanceOf(NetWorksRemoteModel::class.java))
        assertEquals(644, dataResult?.networks?.size)

    }

    @Test
    fun `WHEN CityBikesApi getNetworkById THEN obtains a NetWorkDetailRemoteModel`() {
        api.getNetworkById(BICING_ID).subscribe(netWorkObserver)

        netWorkObserver.assertComplete()
        netWorkObserver.assertNoErrors()
        netWorkObserver.assertTerminated()

        val dataResult = netWorkObserver.values().firstOrNull()
        assertThat(dataResult, IsInstanceOf.instanceOf(NetWorkDetailRemoteModel::class.java))
        assertEquals(499, dataResult?.network?.stations?.size)

    }

}