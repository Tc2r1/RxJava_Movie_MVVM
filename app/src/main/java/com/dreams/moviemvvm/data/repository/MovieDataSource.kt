package com.dreams.moviemvvm.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.dreams.moviemvvm.data.api.FIRST_PAGE
import com.dreams.moviemvvm.data.api.TheMovieDBInterface
import com.dreams.moviemvvm.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

// Create PageKeyedDataSource for Pagination.
// In order to load data based on page number.

class MovieDataSource (private val apiService: TheMovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, Movie>() {

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    // request first page
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    )
    {
        networkState.postValue(NetworkState.LOADING)

        // attempt to get the first page, which will return a single observable
        compositeDisposable.add(
            apiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.moviesList, null, page+1)
                        networkState.postValue(NetworkState.LOADED)

                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )

    }

    // when user scrolls down
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {


        networkState.postValue(NetworkState.LOADING)

        // attempt to get the first page, which will return a single observable
        compositeDisposable.add(
            apiService.getPopularMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        // Check to see if we have more pages to load.
                       if(it.totalPages >= params.key){
                           callback.onResult(it.moviesList, params.key + 1)
                           networkState.postValue(NetworkState.LOADED)
                       } else {
                           // No more pages
                           networkState.postValue(NetworkState.ENDOFLIST)
                       }

                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )

    }

    // when user scrolls up
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }
}