package com.chus.clua.citybikes.data.mapper

import com.chus.clua.citybikes.data.model.NetWorkDetailRemoteModel
import com.chus.clua.citybikes.presentation.utils.NETWORK_JSON
import com.chus.clua.citybikes.presentation.utils.mocks.RemoteModelGenerator.getEntityFromFile
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class NetWorkLocationDomainMapperTest {

    private val mapper = NetWorkLocationDomainMapper()
    private lateinit var remoteModel: NetWorkDetailRemoteModel

    @Before
    fun setUp() {
        remoteModel = getEntityFromFile(NETWORK_JSON)
    }

    @Test
    fun `WHEN map NetWorkLocationRemoteModel THEN obtains a NetWorkLocation`() {
        val domainModel = mapper.mapFromRemote(remoteModel.network?.location)
        with(domainModel) {
            assertEquals("Barcelona", city)
            assertEquals("ES", country)
            assertEquals(41.3850639, latitude)
            assertEquals(2.1734035, longitude)
        }
    }
}