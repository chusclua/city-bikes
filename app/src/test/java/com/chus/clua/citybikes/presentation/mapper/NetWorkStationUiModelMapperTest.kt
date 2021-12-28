package com.chus.clua.citybikes.presentation.mapper

import com.chus.clua.citybikes.domain.model.NetWorkStation
import com.chus.clua.citybikes.presentation.utils.mocks.mockNetWorkStation
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NetWorkStationUiModelMapperTest {

    private val mapper = NetWorkStationUiModelMapper()
    private lateinit var model: NetWorkStation

    @Before
    fun setUp() {
        model = mockNetWorkStation
    }

    @Test
    fun `WHEN map NetWorkStation THEN obtains a NetWorkStationUiModel`() {
        val uiModel = mapper.mapFromDomain(model)
        with(uiModel) {
            assertEquals("e02c5db9e6f6fca078798c9b2d486a81", id)
            assertEquals("JARDINS DE CAN FERRERO/PG.DE LA ZONA FRANCA", name)
            assertNull(address)
            assertEquals(27, emptySlots)
            assertEquals(6, freeBikes)
            assertEquals(30F, markerColor)
            assertEquals(LatLng(41.357067,2.141563), latLng)
        }
    }
}