package com.chus.clua.citybikes.presentation.mapper

import com.chus.clua.citybikes.domain.model.NetWork
import com.chus.clua.citybikes.presentation.utils.mocks.mockNetwork
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NetWorkListUiModelMapperTest {

    private val mapper = NetWorkListUiModelMapper()
    private lateinit var model: NetWork

    @Before
    fun setUp() {
        model = mockNetwork
    }

    @Test
    fun `WHEN map NetWork THEN obtains a NetWorkListUiModel`() {
        val uiModel = mapper.mapFromDomain(model)
        with(uiModel) {
            assertEquals("bicing", id)
            assertEquals("Bicing", name)
            assertEquals("Barcelona", city)
            assertEquals("ES", country)
        }
    }
}