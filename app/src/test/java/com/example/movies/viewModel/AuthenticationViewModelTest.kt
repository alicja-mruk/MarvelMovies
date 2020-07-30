package com.example.movies.viewModel

import com.moodup.movies.viewmodel.authentication.AuthenticationViewModel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.moodup.movies.model.Movie
import com.moodup.movies.repository.firebase.FirebaseAuthLoginHelper
import com.moodup.movies.repository.firebase.FirebaseAuthRegisterHelper
import com.moodup.movies.repository.module.*
import com.moodup.movies.state.AuthLoginState
import com.moodup.movies.state.AuthRegisterState
import com.moodup.movies.state.LogoutState
import com.moodup.movies.state.ResetPasswordState
import junit.framework.Assert.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import okhttp3.internal.wait
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.koin.java.KoinJavaComponent.inject
import org.mockito.Mockito.mock
import org.mockito.Spy

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApp::class, sdk = [28], manifest = Config.NONE)
class AuthenticationViewModelTest {
    private val app: TestApp = ApplicationProvider.getApplicationContext()
    private val viewModel: AuthenticationViewModel by inject(AuthenticationViewModel::class.java)


    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @ObsoleteCoroutinesApi
    @Mock
    private val threadContext = newSingleThreadContext("UI thread")


    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(threadContext)
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().targetContext)
        val modules = listOf(retrofitModule, authenticationViewModelModule, repositoryModule, loginModule, registerModule)
        app.loadModules(modules)

    }

    @Test
    fun isEmailOrPasswordEmpty_shouldReturnTrue() {
        val email: String = ""
        val password: String = ""
        assertTrue("", viewModel.isEmailOrPasswordEmpty(email, password))
    }

    @Test
    fun isEmailOrPasswordEmpty_shouldReturnFalse() {
        val email: String = "email@gmail.com"
        val password: String = "password"
        assertFalse("", viewModel.isEmailOrPasswordEmpty(email, password))
    }

    @Test
    fun should_login() = runBlocking {
        val email: String = "alicja.mruk99@gmail.com"
        val password: String = "asd123"

        viewModel.login(email, password)
        assertEquals(AuthLoginState.ON_LOGIN_SUCCESS, viewModel.authenticationLoginState.value)
        }


    @Test
    fun should_notLogin() = runBlocking {
        val email: String = "user"
        val password: String = "password"
        viewModel.login(email, password)
        assertEquals(AuthLoginState.ON_LOGIN_FAILURE, viewModel.authenticationLoginState.value)
    }

    @Test
    fun should_register() = runBlocking {


    }

    @Test
    fun should_notRegister() = runBlocking {


    }

    @Test
    fun should_logout() = runBlocking {


    }

}