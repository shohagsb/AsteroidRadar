package com.udacity.asteroidradar.network


import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query



private val retrofit = Retrofit.Builder()
    .addConverterFactory(StringOrJsonConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()


interface AsteroidApiService {

    @GET("/planetary/apod")
    @JsonAnnotation
    suspend fun getPictureOfDay(
        @Query("api_key") apiKey: String
    ): PictureOfDay

    @GET("neo/rest/v1/feed")
    @StringAnnotation
    suspend fun getAsteroids(
        @Query("api_key") authToken: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
    ): String
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention()
internal annotation class StringAnnotation

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention()
internal annotation class JsonAnnotation

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}