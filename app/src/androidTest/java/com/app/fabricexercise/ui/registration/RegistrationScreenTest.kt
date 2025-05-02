package com.app.fabricexercise.ui.registration

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.fabricexercise.data.repository.RegistrationRepository
import com.app.fabricexercise.domain.usecase.RegistrationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ---- Fake Repository ----
    class FakeRegistrationRepository : RegistrationRepository {
        override suspend fun saveSecretKey(key: String) {
            TODO("Not yet implemented")
        }

        override suspend fun getSecretKey(): String? {
            TODO("Not yet implemented")
        }

        override suspend fun saveFirebaseToken(token: String) {
            TODO("Not yet implemented")
        }

        override suspend fun getFirebaseToken(): String? {
            TODO("Not yet implemented")
        }
    }

    // ---- Fake UseCase ----
    class FakeRegistrationUseCase(
        private val fakeRepository: RegistrationRepository
    ) : RegistrationUseCase(fakeRepository) {
    }

    // ---- Testable ViewModel ----
    class TestableRegistrationViewModel : RegistrationViewModel(
        FakeRegistrationUseCase(FakeRegistrationRepository())
    ) {
        private val _testState = MutableStateFlow<RegistrationState>(RegistrationState.Initial)
        override val state: StateFlow<RegistrationState> get() = _testState

        private val _testTime = MutableStateFlow("00:00")
        override val remainingFormattedTime: StateFlow<String> get() = _testTime

        var registerDeviceCalled = false

        override fun registerDevice() {
            registerDeviceCalled = true
        }

        fun setState(state: RegistrationState, time: String = "00:00") {
            _testState.value = state
            _testTime.value = time
        }
    }

    // ---- Test Cases ----

    @Test
    fun registrationScreen_showsInitialState_andTriggersRegister() {
        val viewModel = TestableRegistrationViewModel().apply {
            setState(RegistrationState.Initial)
        }

        composeTestRule.setContent {
            RegistrationScreen(viewModel)
        }

        composeTestRule.onNodeWithText("Register Device").assertIsDisplayed().performClick()
        assert(viewModel.registerDeviceCalled)
    }

    @Test
    fun registrationScreen_showsInProgressState() {
        val viewModel = TestableRegistrationViewModel().apply {
            setState(RegistrationState.InProgress, time = "01:30")
        }

        composeTestRule.setContent {
            RegistrationScreen(viewModel)
        }

        composeTestRule.onNodeWithText("Time left: 01:30").assertIsDisplayed()
        composeTestRule.onNodeWithText("Registration in progress....").assertIsDisplayed()
    }

    @Test
    fun registrationScreen_showsRegisteredState() {
        val viewModel = TestableRegistrationViewModel().apply {
            setState(RegistrationState.Registered)
        }

        composeTestRule.setContent {
            RegistrationScreen(viewModel)
        }

        composeTestRule.onNodeWithText("Registered Successfully!").assertIsDisplayed()
    }

    @Test
    fun registrationScreen_showsExpiredState() {
        val viewModel = TestableRegistrationViewModel().apply {
            setState(RegistrationState.RegistrationExpired)
        }

        composeTestRule.setContent {
            RegistrationScreen(viewModel)
        }

        composeTestRule.onNodeWithText("Registration Expired!").assertIsDisplayed()
    }

    @Test
    fun registrationScreen_showsFailedState() {
        val viewModel = TestableRegistrationViewModel().apply {
            setState(RegistrationState.RegistrationFailed)
        }

        composeTestRule.setContent {
            RegistrationScreen(viewModel)
        }

        composeTestRule.onNodeWithText("Registration Failed!").assertIsDisplayed()
    }
}


