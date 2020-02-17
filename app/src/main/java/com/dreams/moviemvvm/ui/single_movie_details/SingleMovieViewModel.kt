package com.dreams.moviemvvm.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dreams.moviemvvm.data.repository.NetworkState
import com.dreams.moviemvvm.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel (private val movieRepository: MovieDetailsRepository, movieId: Int):
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()


    // by lazy so we only get the movie details when we need it, not when the class is initialized.
    val movieDetails: LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    // When a fragment or an activity gets destroyed.
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}