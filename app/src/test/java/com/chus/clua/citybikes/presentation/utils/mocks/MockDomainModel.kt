package com.chus.clua.citybikes.presentation.utils.mocks

import com.chus.clua.citybikes.domain.model.NetWork
import com.chus.clua.citybikes.domain.model.NetWorkLocation
import com.chus.clua.citybikes.domain.model.NetWorkStation
import java.time.Instant
import java.util.*

val mockNetWorkStation =
    NetWorkStation(
        "e02c5db9e6f6fca078798c9b2d486a81",
        "JARDINS DE CAN FERRERO/PG.DE LA ZONA FRANCA",
        27,
        6,
        41.357067,
        2.141563,
        Date.from(Instant.parse("2020-11-07T17:44:22.349000Z"))
    )

private val mockStations: List<NetWorkStation> = listOf(
    mockNetWorkStation.copy(
        "ed25291d0f5edd91615d154f243f82f9",
        "PG. DE COLOM (LES RAMBLES)",
        21,
        11,
        41.376433,
        2.17871,
        Date.from(Instant.parse("2020-11-07T17:44:22.349000Z"))
    ),
    mockNetWorkStation
)

val mockNetwork = NetWork(
    id = "bicing",
    name = "Bicing",
    location = NetWorkLocation("Barcelona", "ES", 41.3850639, 2.1734035),
    companyList = listOf(
        "Barcelona de Serveis Municipals, S.A. (BSM)",
        "CESPA",
        "PBSC"
    ),
    stations = mockStations
)

val mockNetWorks = listOf(
    mockNetwork.copy(
        id = "velobike-moscow",
        name = "Velobike",
        location = NetWorkLocation("Moscow", "RU", 55.75, 37.616667),
        companyList = listOf(
            "ЗАО «СитиБайк»"
        )
    ), mockNetwork.copy(
        id = "to-bike",
        name = "[TO]BIKE",
        location = NetWorkLocation("Torino", "IT", 45.07098200000001, 7.685676),
        companyList = listOf(
            "Comunicare S.r.l."
        )
    ), mockNetwork
)

