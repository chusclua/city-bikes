package com.chus.clua.citybikes.presentation.utils.service

import com.chus.clua.citybikes.data.service.CityBikesApi
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import java.net.InetAddress

open class BaseServiceTest {

    lateinit var webServer: MockWebServer
        private set
    lateinit var api: CityBikesApi

    @Before
    open fun setUp() {
        webServer = MockWebServer()
        webServer.start(InetAddress.getByName("localhost"), 8081)
        webServer.dispatcher = MockServerDispatcher()
        api = getRetrofit(webServer.url("/").toString()).create(CityBikesApi::class.java)
    }

    @After
    open fun tearDown() {
        webServer.shutdown()
    }
}