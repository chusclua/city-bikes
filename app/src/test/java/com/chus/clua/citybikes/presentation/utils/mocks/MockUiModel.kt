package com.chus.clua.citybikes.presentation.utils.mocks

import com.chus.clua.citybikes.presentation.models.NetWorkListUiModel
import com.chus.clua.citybikes.presentation.models.NetWorkMapUiModel
import com.chus.clua.citybikes.presentation.models.NetWorkStationUiModel
import com.google.android.gms.maps.model.LatLng

val mockNetWorkStationUiModel = NetWorkStationUiModel(
    "e02c5db9e6f6fca078798c9b2d486a81",
    "JARDINS DE CAN FERRERO/PG.DE LA ZONA FRANCA",
    null,
    27,
    6,
    30F,
    LatLng(41.357067, 2.141563)
)

val mockNetWorkMapUiModel = NetWorkMapUiModel(
    "bicing",
    "Bicing",
    "Barcelona",
    "Barcelona de Serveis Municipals, S.A. (BSM), CESPA, PBSC",
    listOf(
        mockNetWorkStationUiModel, mockNetWorkStationUiModel.copy(
            "ed25291d0f5edd91615d154f243f82f9",
            "PG. DE COLOM (LES RAMBLES)",
            null,
            21,
            11,
            120F,
            LatLng(41.376433, 2.17871)
        )
    )
)

val mockNetWorkListUiModel = NetWorkListUiModel(
    "bicing", "Bicing", "Barcelona", "ES"
)