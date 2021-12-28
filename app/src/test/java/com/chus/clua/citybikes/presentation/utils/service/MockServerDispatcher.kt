package com.chus.clua.citybikes.presentation.utils.service

import com.chus.clua.citybikes.data.service.ID
import com.chus.clua.citybikes.data.service.NETWORKS
import com.chus.clua.citybikes.data.service.NETWORK_ID
import com.chus.clua.citybikes.presentation.utils.BICING_ID
import com.chus.clua.citybikes.presentation.utils.NETWORKS_LIST_JSON
import com.chus.clua.citybikes.presentation.utils.NETWORK_JSON
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockServerDispatcher : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/$NETWORKS" -> enqueueMockResponseFromFile(NETWORKS_LIST_JSON)
            "/${NETWORK_ID.replace("{$ID}", BICING_ID)}" ->
                enqueueMockResponseFromFile(NETWORK_JSON)
            else -> MockResponse().setResponseCode(404)
        }
    }
}