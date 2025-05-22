package com.proyecto.libroschill.core.network
import com.proyecto.libroschill.data.model.Volume
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.proyecto.libroschill.BuildConfig

interface BooksApiService {

    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("key") apiKey: String = BuildConfig.GOOGLE_BOOKS_API_KEY ,
        @Query("langRestrict") lang: String = "es"
    ): SearchResponse

    @GET("volumes/{volumeId}")
    suspend fun getBookDetails(
        @Path("volumeId") volumeId: String,
        @Query("key") apiKey: String = BuildConfig.GOOGLE_BOOKS_API_KEY
    ): com.proyecto.libroschill.data.model.Volume
}
data class SearchResponse(
    val items: List<Volume>
)