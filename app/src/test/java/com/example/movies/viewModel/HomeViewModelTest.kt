package com.example.movies.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.moodup.movies.model.Movie
import com.moodup.movies.model.Thumbnail
import com.moodup.movies.repository.module.homeViewModelModule
import com.moodup.movies.repository.module.repositoryModule
import com.moodup.movies.repository.module.retrofitModule
import com.moodup.movies.state.UIState
import com.moodup.movies.viewmodel.home.HomeViewModel
import junit.framework.Assert.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.inject

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApp::class, sdk = [28], manifest = Config.NONE)
class HomeViewModelTest {
    private val app: TestApp = ApplicationProvider.getApplicationContext()
    private val viewModel: HomeViewModel by inject(HomeViewModel::class.java)

    private val stateObserver = Observer<UIState> {
        viewModel.UIstateLiveData.postValue(it)
    }
    private val moviesObserver = Observer<List<Movie>> {
        viewModel.movieLiveData.postValue(it)
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

        val modules = listOf(retrofitModule, homeViewModelModule, repositoryModule)
        app.loadModules(modules)
        viewModel.UIstateLiveData.observeForever(stateObserver)
        viewModel.movieLiveData.observeForever(moviesObserver)

    }

    @Test
    fun zeroResultQuery_shouldReturn_EmptyMoviesList() {
        val emptyList: List<Movie> = listOf()
        viewModel.getMovies(0, "blabla")
        assertEquals(emptyList, viewModel.movieLiveData.value)
    }

    @Test
    fun initializeGetMovies_shouldChangeUIStateLiveData_toInitializedState() = runBlocking {
        viewModel.getMovies(0, "")
        assertEquals(UIState.INITIALIZED, viewModel.UIstateLiveData.value)
    }

    @Test
    fun getMoviesOnEmptyResults_shouldChangeUIStateLiveData_toEmptyState() = runBlocking {
        viewModel.getMovies(0, "blabla")
        assertEquals(UIState.ON_EMPTY_RESULTS, viewModel.UIstateLiveData.value)
    }

    @Test
    fun getMoviesOnNotEmptyResults_shouldChangeUIStateLiveData_toResultState() = runBlocking {
        viewModel.getMovies(0, "")
        assertEquals(UIState.ON_RESULT, viewModel.UIstateLiveData.value)
    }

    @Test
    fun shouldUpdateList_and_returnSizePlusOne(){
        val movie = Movie(1, Thumbnail("abc", ".jpg"),"title", "description","format",150)
        val oldList : List<Movie>? = viewModel.movieLiveData.value
        val newList = ArrayList<Movie>()
        newList.add(movie)
        if (oldList != null) {
            newList.addAll(oldList)
        }
        viewModel.updateMovieList(newList)
        assertEquals((oldList!!.size+1), newList.size)
    }
    @Test
    fun checkIfScrollingPossible_shouldReturnTrue(){
        val totalItemCount = 1
        assertTrue(viewModel.totalResults-1>totalItemCount)
    }

    @Test
    fun checkIfScrollingPossible_shouldReturnFalse(){
        val totalItemCount = 1
        assertFalse(viewModel.totalResults-1<totalItemCount)
    }

}