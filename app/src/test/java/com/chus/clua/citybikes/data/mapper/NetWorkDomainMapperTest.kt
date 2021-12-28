package com.chus.clua.citybikes.data.mapper

import com.chus.clua.citybikes.data.model.NetWorkDetailRemoteModel
import com.chus.clua.citybikes.presentation.utils.NETWORK_JSON
import com.chus.clua.citybikes.presentation.utils.mocks.RemoteModelGenerator.getEntityFromFile
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class NetWorkDomainMapperTest {

    private val mapper = NetWorkDomainMapper(mock(), mock())
    private lateinit var remoteModel: NetWorkDetailRemoteModel

    @Before
    fun setUp() {
        remoteModel = getEntityFromFile(NETWORK_JSON)
    }

    @Test
    fun `WHEN map NetWorkRemoteModel THEN obtains a NetWork`() {
        val domainModel = mapper.mapFromRemote(remoteModel.network)
        with(domainModel) {
            assertEquals(listOf("Barcelona de Serveis Municipals, S.A. (BSM)", "CESPA", "PBSC"), companyList)
            assertEquals("bicing", id)
            assertEquals("Bicing", name)
        }
    }
}