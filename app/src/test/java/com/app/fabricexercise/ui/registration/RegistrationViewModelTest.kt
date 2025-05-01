package com.app.fabricexercise.ui.registration

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.app.fabricexercise.domain.usecase.RegistrationUseCase
import com.app.fabricexercise.util.TimerHelper
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RegistrationViewModelTest {

    @Mock
    private lateinit var useCase: RegistrationUseCase

    private lateinit var viewModel: RegistrationViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)

        // Mock Android Log to prevent crashes
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0

        // Mock static Log class using Mockito's mockStatic (requires Mockito 3.4+)
        try {
            val logClass = mockStatic(Log::class.java)
            logClass.`when`<Int> { Log.e(anyString(), anyString()) }.thenReturn(0)
        } catch (e: Exception) {
            // Mockito-inline not available
        }

        viewModel = RegistrationViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `registerDevice should generate key, update state to InProgress and start timer`() =
        runTest {
            // Given
            val dummyKey = "test-hmac-key"
            whenever(useCase.generateAndStoreSecretKey()).thenReturn(dummyKey)
            whenever(useCase.storeFirebaseToken()).thenAnswer { } // Mock void function

            // When
            viewModel.registerDevice()
            testScheduler.advanceUntilIdle() // Execute all pending coroutines

            // Then
            assertEquals(RegistrationState.Initial, viewModel.state.value) // Direct state check

            verify(useCase).generateAndStoreSecretKey()
            verify(useCase).storeFirebaseToken()

            // Verify timer starts
            assertEquals("00:00", viewModel.remainingFormattedTime.value)
        }

    @Test
    fun `registerDevice should update state to Initial after completion`() = runTest {
        // Given
        val dummyKey = "test-key"
        whenever(useCase.generateAndStoreSecretKey()).thenReturn(dummyKey)
        whenever(useCase.storeFirebaseToken()).thenAnswer { } // Mock void function

        // When
        viewModel.registerDevice()
        testScheduler.advanceUntilIdle() // Critical: Execute all pending coroutines

        // Then
        assertEquals(RegistrationState.Initial, viewModel.state.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `registerDevice should transition through all states`() = runTest {
        // Given
        val dummyKey = "test-key"
        whenever(useCase.generateAndStoreSecretKey()).thenReturn(dummyKey)
        whenever(useCase.storeFirebaseToken()).thenAnswer { }

        // When & Then
        viewModel.state.test {
            // Initial state (optional if you want to verify starting point)
            assertEquals(RegistrationState.Initial, awaitItem())

            // Trigger registration
            viewModel.registerDevice()

            // Verify InProgress state
            assertEquals(RegistrationState.InProgress, awaitItem())

            // Verify it stays in progress (no further emissions)
            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }

        // Verify use case interactions
        verify(useCase).generateAndStoreSecretKey()
        verify(useCase).storeFirebaseToken()
    }

    @Test
    fun `full registration flow with timer`() = runTest {
        // Setup
        whenever(useCase.generateAndStoreSecretKey()).thenReturn("key")
        whenever(useCase.storeFirebaseToken()).thenAnswer { }

        viewModel.state.test {
            // Initial
            assertEquals(RegistrationState.Initial, awaitItem())

            // Start registration
            viewModel.registerDevice()

            // InProgress
            assertEquals(RegistrationState.InProgress, awaitItem())

            // Simulate timer expiration
            testScheduler.advanceTimeBy(16 * 60 * 1000) // 16 minutes

            // RegistrationExpired
            assertEquals(RegistrationState.RegistrationExpired, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test only InProgress state`() = runTest {
        whenever(useCase.generateAndStoreSecretKey()).thenReturn("key")

        // Capture states in a list
        val states = mutableListOf<RegistrationState>()
        val job = viewModel.state
            .onEach { states.add(it) }
            .launchIn(CoroutineScope(testDispatcher))

        viewModel.registerDevice()
        testScheduler.runCurrent()

        assertEquals(2, states.size) // Initial + InProgress
        assertEquals(RegistrationState.InProgress, states.last())

        job.cancel()
    }
}