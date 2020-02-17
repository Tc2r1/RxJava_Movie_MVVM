package com.dreams.moviemvvm.ui.single_movie_details

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.dreams.moviemvvm.R
import com.dreams.moviemvvm.data.api.POSTER_BASE_URL
import com.dreams.moviemvvm.data.api.TheMovieDBClient
import com.dreams.moviemvvm.data.api.TheMovieDBInterface
import com.dreams.moviemvvm.data.repository.NetworkState
import com.dreams.moviemvvm.data.vo.MovieDetails
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository =
            MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {

            // Update UI if changes happen.
            bindUI(it)
        })

        // Observe the network state
        viewModel.networkState.observe(this, Observer {

            // Update UI if changes happen.
            progress_bar.visibility =
                if (it == NetworkState.LOADING){
                    View.VISIBLE
                } else {
                    View.GONE
                }

            txt_error.visibility =
                if (it == NetworkState.ERROR){
                    View.VISIBLE
                } else {
                    View.GONE
                }

        })
    }

    fun bindUI(it: MovieDetails) {
        movie_title.text = it.title
        movie_tagLine.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.rating.toString()
        movie_runtime.text = it.runtime.toString() + "minutes"
        movie_overview.text = it.overview

        val formateCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formateCurrency.format(it.budget)
        movie_revenue.text = formateCurrency.format(it.revenue)


        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster)
    }

    // ViewModel provider factory for single movie viewModel

    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(
                    movieRepository,
                    movieId
                ) as T
            }
        })[SingleMovieViewModel::class.java]
    }

}