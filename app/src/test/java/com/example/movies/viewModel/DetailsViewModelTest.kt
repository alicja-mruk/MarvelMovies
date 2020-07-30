package com.example.movies.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.moodup.movies.repository.module.detailsViewModelModule

import com.moodup.movies.repository.module.homeViewModelModule
import com.moodup.movies.repository.module.repositoryModule
import com.moodup.movies.repository.module.retrofitModule
import com.moodup.movies.state.AddedToDatabaseState
import com.moodup.movies.state.UIState
import com.moodup.movies.viewmodel.details.DetailsViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApp::class, sdk = [28], manifest = Config.NONE)
class DetailsViewModelTest {
    private val app: TestApp = ApplicationProvider.getApplicationContext()
    private val viewModel: DetailsViewModel by inject(DetailsViewModel::class.java)
    private lateinit var userId: String
    private lateinit var docRef: DocumentReference
    private lateinit var db: FirebaseFirestore

    private val isMoviePresentObserver = Observer<Boolean> {
        viewModel.isMoviePresent.postValue(it)
    }
    private val databaseStateObserver = Observer<AddedToDatabaseState>{
        viewModel.databaseState.postValue(it)
    }

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
        val modules = listOf(retrofitModule, detailsViewModelModule, repositoryModule)
        app.loadModules(modules)
        userId = "999"
        db =  FirebaseFirestore.getInstance()
        docRef = db.collection("favourites").document(userId).collection("movies").document("123")

        viewModel.isMoviePresent.observeForever(isMoviePresentObserver)
        viewModel.databaseState.observeForever(databaseStateObserver)
    }


    @Test
    fun addToDatabaseWhenUserIdEqualsNull() = runBlocking{
        viewModel.addToDatabase()
        assertEquals(null, viewModel.databaseState.value)
    }

//    @Test
//    fun addToDatabase () = runBlocking{
//        viewModel.addToDatabase()
//        assertEquals(AddedToDatabaseState.ADDED_SUCCESS, viewModel.databaseState.value)
//    }
//
//    @Test
//    fun removeFromDatabase() = runBlocking{
//        viewModel.removeFromDatabase()
//        assertEquals(AddedToDatabaseState.REMOVED_SUCCESS, viewModel.databaseState.value)
//    }

}
