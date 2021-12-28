package com.chus.clua.citybikes.domain.usecase

import com.chus.clua.citybikes.domain.model.NetWork
import com.chus.clua.citybikes.domain.repository.NetworkRepository
import com.chus.clua.citybikes.presentation.utils.BICING_ID
import com.chus.clua.citybikes.presentation.utils.mocks.mockNetwork
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
class GetNetWorkByIdUseCaseTest {

    @Mock
    private lateinit var repository: NetworkRepository
    private lateinit var useCase: GetNetWorkByIdUseCase
    private val testObserver = TestObserver<NetWork>()

    @Before
    fun setUp() {
        useCase = GetNetWorkByIdUseCase(repository)
    }

    @Test
    fun `WHEN GetNetWorkByIdUseCase THEN obtains a NetWork`() {
        whenever(repository.getNetworkById(BICING_ID)).thenReturn(Single.just(mockNetwork))

        useCase.invoke(BICING_ID).subscribe(testObserver)

        verify(repository, times(1)).getNetworkById(BICING_ID)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        val result = testObserver.values().first()

        assertEquals(mockNetwork, result)
    }

    @Test
    fun `WHEN GetNetWorkByIdUseCase THEN obtains an error`() {
        val error = Throwable("exception_message")
        whenever(repository.getNetworkById(BICING_ID)).thenReturn(Single.error(error))
        useCase.invoke(BICING_ID).subscribe(testObserver)

        testObserver.assertError(error)
        testObserver.assertErrorMessage("exception_message")
    }
}