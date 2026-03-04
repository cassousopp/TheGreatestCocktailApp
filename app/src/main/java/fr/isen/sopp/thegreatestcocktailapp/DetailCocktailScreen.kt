package fr.isen.sopp.thegreatestcocktailapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCocktailScreen(drinkId: String? = null) {
    val context = LocalContext.current
    var drink by remember { mutableStateOf<Drink?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(drinkId) {
        isLoading = true
        try {
            val response = if (drinkId == null) {
                NetworkManager.api.getRandomCocktail()
            } else {
                NetworkManager.api.getDrinkDetail(drinkId)
            }
            drink = response.drinks?.firstOrNull()
            isFavorite = drink?.let { isDrinkFavorite(context, it.id) } ?: false
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(drink?.name ?: "Detail", color = Color.White) },
                actions = {
                    IconButton(onClick = {
                        // Reload random if no drinkId
                        if (drinkId == null) {
                            // Trigger re-run of LaunchedEffect by some state if needed
                            // For simplicity, let's just use a refresh button logic
                        }
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    }
                    IconButton(onClick = {
                        drink?.let {
                            isFavorite = toggleFavorite(context, it)
                        }
                    }) {
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1128))
            )
        },
        containerColor = Color(0xFF1A1128)
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (drink != null) {
            val currentDrink = drink!!
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = currentDrink.thumb,
                    contentDescription = currentDrink.name,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentDrink.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    currentDrink.category?.let {
                        Badge(containerColor = Color(0xFF3F51B5), contentColor = Color.White) {
                            Text(it, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                    currentDrink.alcoholic?.let {
                        Badge(containerColor = Color(0xFF4CAF50), contentColor = Color.White) {
                            Text(it, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                currentDrink.glass?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(it, color = Color.LightGray, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2438)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Ingrédients", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        currentDrink.getIngredients().forEach { (name, amount) ->
                            IngredientItem(name, amount)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2438)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Recipe", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(currentDrink.instructions ?: "No instructions", color = Color.White)
                    }
                }
            }
        }
    }
}

fun isDrinkFavorite(context: Context, drinkId: String): Boolean {
    val prefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    val favoritesJson = prefs.getString("favorite_list", null) ?: return false
    val type = object : TypeToken<List<Drink>>() {}.type
    val favorites: List<Drink> = Gson().fromJson(favoritesJson, type)
    return favorites.any { it.id == drinkId }
}

fun toggleFavorite(context: Context, drink: Drink): Boolean {
    val prefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    val favoritesJson = prefs.getString("favorite_list", null)
    val type = object : TypeToken<MutableList<Drink>>() {}.type
    val favorites: MutableList<Drink> = if (favoritesJson == null) {
        mutableListOf()
    } else {
        Gson().fromJson(favoritesJson, type)
    }

    val index = favorites.indexOfFirst { it.id == drink.id }
    val isNowFavorite: Boolean
    if (index != -1) {
        favorites.removeAt(index)
        isNowFavorite = false
        Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
    } else {
        favorites.add(drink)
        isNowFavorite = true
        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
    }

    prefs.edit().putString("favorite_list", Gson().toJson(favorites)).apply()
    return isNowFavorite
}

@Composable
fun IngredientItem(name: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color.White))
            Spacer(modifier = Modifier.width(8.dp))
            Text(name, color = Color.White)
        }
        Text(amount, color = Color.LightGray)
    }
}
