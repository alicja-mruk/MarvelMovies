package com.moodup.movies.repository.module
import com.moodup.movies.viewmodel.authentication.AuthenticationViewModel
import com.moodup.movies.viewmodel.details.DetailsViewModel
import com.moodup.movies.viewmodel.favourites.FavouritesViewModel
import com.moodup.movies.viewmodel.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val homeViewModelModule = module{
   viewModel{
       HomeViewModel(get())
   }
}
val favouritesViewModelModule = module{
    viewModel {
        FavouritesViewModel()
    }
}
val authenticationViewModelModule = module{
    viewModel{
        AuthenticationViewModel()
    }
}
val detailsViewModelModule = module{
    viewModel{
        DetailsViewModel()
    }
}


