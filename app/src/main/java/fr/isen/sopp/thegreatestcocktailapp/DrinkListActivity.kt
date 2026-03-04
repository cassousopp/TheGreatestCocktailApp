package fr.isen.sopp.thegreatestcocktailapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.isen.sopp.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class DrinkListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = intent.getStringExtra("category") ?: ""
        setContent {
            TheGreatestCocktailAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF1A1128)
                ) {
                    DrinkListScreen(category) { drinkId ->
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra("drinkId", drinkId)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun DrinkListScreen(category: String, onDrinkClick: (String) -> Unit) {
    var drinks by remember { mutableStateOf<List<Drink>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(category) {
        try {
            val response = NetworkManager.api.getDrinksByCategory(category)
            drinks = response.drinks ?: emptyList()
        } catch (e: Exception) {
            // Handle error
        } finally {
            isLoading = false
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Drinks for $category", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))
        
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(drinks) { drink ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDrinkClick(drink.id) },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2438))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = drink.thumb,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                contentScale = ContentScale.Crop
                            )
                            Text(text = drink.name, modifier = Modifier.padding(16.dp), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
