package com.chus.clua.citybikes.data.mapper

import com.chus.clua.citybikes.data.model.NetWorkDetailRemoteModel
import com.chus.clua.citybikes.presentation.utils.NETWORK_JSON
import com.chus.clua.citybikes.presentation.utils.mocks.RemoteModelGenerator.getEntityFromFile
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.Instant

@RunWith(MockitoJUnitRunner::class)
class NetWorkStationDomainMapperTest {

    private val mapper = NetWorkStationDomainMapper()
    private lateinit var remoteModel: NetWorkDetailRemoteModel

    @Before
    fun setUp() {
        remoteModel = getEntityFromFile(NETWORK_JSON)
    }

    @Test
    fun `WHEN map NetWorkStationRemoteModel THEN obtains a NetWorkStation`() {
        val domainModel = mapper.mapFromRemoteList(remoteModel.network?.stations)
        assertTrue(domainModel.isNotEmpty())
        assertEquals(499, domainModel.size)
        with(domainModel.first()) {
            assertEquals(21, emptySlots)
            assertEquals(11, freeBikes)
            assertEquals("ed25291d0f5edd91615d154f243f82f9", id)
            assertEquals(41.376433, latitude)
            assertEquals(2.17871, longitude)
            assertEquals("PG. DE COLOM (LES RAMBLES)", name)
            assertEquals(Instant.parse("2020-11-07T17:44:22.308000Z"), timestamp?.toInstant())
        }
    }
}