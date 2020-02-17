package com.dreams.moviemvvm.data.api

import com.dreams.moviemvvm.data.vo.MovieDetails
import com.dreams.moviemvvm.data.vo.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {

    // Base Url For retrofit:
    // https://api.themoviedb.org/3/
    // Base for popular movies
    // https://api.themoviedb.org/3/movie/popular?api_key=a70bede5f34a31b5f0098fc98ccfb971&page=1
    // Base for single movie
    // https://api.themoviedb.org/3/299534?api_key=a70bede5f34a31b5f0098fc98ccfb971

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int) : Single<MovieDetails>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int) : Single<MovieResponse>
}