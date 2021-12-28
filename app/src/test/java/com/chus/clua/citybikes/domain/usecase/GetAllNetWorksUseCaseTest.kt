package com.chus.clua.citybikes.domain.usecase

import com.chus.clua.citybikes.domain.model.NetWork
import com.chus.clua.citybikes.domain.repository.NetworkRepository
import com.chus.clua.citybikes.presentation.utils.mocks.mockNetWorks
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAllNetWorksUseCaseTest {

    @Mock
    private lateinit var repository: NetworkRepository
    private lateinit var useCase: GetAllNetWorksUseCase
    private val testObserver = TestObserver<List<NetWork>>()

    @Before
    fun setUp() {
        useCase = GetAllNetWorksUseCase(repository)
    }

    @Test
    fun `WHEN GetAllNetWorksUseCase THEN obtains a List of NetWork`() {
        whenever(repository.getNetworks()).thenReturn(Single.just(mockNetWorks))

        useCase.invoke().subscribe(testObserver)

        verify(repository, times(1)).getNetworks()

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        val result = testObserver.values().firstOrNull()

        assertEquals(mockNetWorks, result)

    }

    @Test
    fun `WHEN GetAllNetWorksUseCase THEN obtains an error`() {
        val error = Throwable("exception_message")
        whenever(repository.getNetworks()).thenReturn(Single.error(error))
        useCase.invoke().subscribe(testObserver)

        testObserver.assertError(error)
        testObserver.assertErrorMessage("exception_message")

    }
}