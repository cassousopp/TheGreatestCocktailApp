package fr.isen.sopp.thegreatestcocktailapp

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("random.php")
    suspend fun getRandomCocktail(): DrinkResponse

    @GET("list.php?c=list")
    suspend fun getCategories(): CategoryResponse

    @GET("filter.php")
    suspend fun getDrinksByCategory(@Query("c") category: String): DrinkResponse

    @GET("lookup.php")
    suspend fun getDrinkDetail(@Query("i") id: String): DrinkResponse
}
