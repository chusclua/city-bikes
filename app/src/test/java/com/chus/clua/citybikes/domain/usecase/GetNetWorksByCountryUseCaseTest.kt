package com.chus.clua.citybikes.domain.usecase

import com.chus.clua.citybikes.domain.model.NetWork
import com.chus.clua.citybikes.domain.repository.NetworkRepository
import com.chus.clua.citybikes.presentation.utils.SPAIN_COUNTRY_CODE
import com.chus.clua.citybikes.presentation.utils.mocks.mockNetWorks
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetNetWorksByCountryUseCaseTest {

    @Mock
    private lateinit var repository: NetworkRepository
    private lateinit var useCase: GetNetWorksByCountryUseCase
    private val testObserver = TestObserver<List<NetWork>>()

    @Before
    fun setUp() {
        useCase = GetNetWorksByCountryUseCase(repository)
    }

    @Test
    fun `WHEN GetNetWorksByCountryUseCase THEN obtains a List of NetWork`() {
        whenever(repository.getNetworksByCountry(SPAIN_COUNTRY_CODE)).thenReturn(Single.just(
            mockNetWorks
        ))

        useCase.invoke(SPAIN_COUNTRY_CODE).subscribe(testObserver)

        verify(repository, times(1)).getNetworksByCountry(SPAIN_COUNTRY_CODE)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        val result = testObserver.values().first()

        assertEquals(mockNetWorks, result)
    }

    @Test
    fun `WHEN GetNetWorksByCountryUseCase THEN obtains an error`() {
        val error = Throwable("exception_message")
        whenever(repository.getNetworksByCountry(SPAIN_COUNTRY_CODE)).thenReturn(Single.error(error))
        useCase.invoke(SPAIN_COUNTRY_CODE).subscribe(testObserver)

        testObserver.assertError(error)
        testObserver.assertErrorMessage("exception_message")
    }
}