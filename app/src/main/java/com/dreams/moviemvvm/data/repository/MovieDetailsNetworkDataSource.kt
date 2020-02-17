package com.dreams.moviemvvm.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dreams.moviemvvm.data.api.TheMovieDBInterface
import com.dreams.moviemvvm.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


// Call API using RXJAVA, Api will return movie details.
// Then we assign movie details in live data
class MovieDetailsNetworkDataSource(private val apiSource: TheMovieDBInterface, private val compositeDisposable: CompositeDisposable) {

    private val _networkState = MutableLiveData<NetworkState>()
    // With this get, no need to implement get function to get networkState
    val networkState: LiveData<NetworkState> get() = _networkState

    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val downloadedMovieDetailsResponse: LiveData<MovieDetails> get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieId: Int) {

        _networkState.postValue(NetworkState.LOADING)


        try {
            // Use RxJava thread to make network calls

            compositeDisposable.add(
                apiSource.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            // On Success post the value of it
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)

                        },
                        {
                            // On Throwable
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDataSource", it.message)
                        }
                    )
            )
        } catch (e: Exception){
            Log.e("MovieDetailsDataSource", e.message)
        }
    }
}