package fr.isen.sopp.thegreatestcocktailapp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryResponse(
    @SerializedName("drinks") val categories: List<Category>
) : Serializable

data class Category(
    @SerializedName("strCategory") val name: String
) : Serializable

data class DrinkResponse(
    @SerializedName("drinks") val drinks: List<Drink>?
) : Serializable

data class Drink(
    @SerializedName("idDrink") val id: String,
    @SerializedName("strDrink") val name: String,
    @SerializedName("strDrinkThumb") val thumb: String,
    @SerializedName("strCategory") val category: String?,
    @SerializedName("strAlcoholic") val alcoholic: String?,
    @SerializedName("strGlass") val glass: String?,
    @SerializedName("strInstructions") val instructions: String?,
    @SerializedName("strIngredient1") val ingredient1: String?,
    @SerializedName("strIngredient2") val ingredient2: String?,
    @SerializedName("strIngredient3") val ingredient3: String?,
    @SerializedName("strIngredient4") val ingredient4: String?,
    @SerializedName("strIngredient5") val ingredient5: String?,
    @SerializedName("strMeasure1") val measure1: String?,
    @SerializedName("strMeasure2") val measure2: String?,
    @SerializedName("strMeasure3") val measure3: String?,
    @SerializedName("strMeasure4") val measure4: String?,
    @SerializedName("strMeasure5") val measure5: String?
) : Serializable {
    fun getIngredients(): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        if (!ingredient1.isNullOrBlank()) list.add(ingredient1 to (measure1 ?: ""))
        if (!ingredient2.isNullOrBlank()) list.add(ingredient2 to (measure2 ?: ""))
        if (!ingredient3.isNullOrBlank()) list.add(ingredient3 to (measure3 ?: ""))
        if (!ingredient4.isNullOrBlank()) list.add(ingredient4 to (measure4 ?: ""))
        if (!ingredient5.isNullOrBlank()) list.add(ingredient5 to (measure5 ?: ""))
        return list
    }
}
