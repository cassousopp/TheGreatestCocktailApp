package fr.isen.sopp.thegreatestcocktailapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.isen.sopp.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheGreatestCocktailAppTheme {
                MainScreen()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Random : Screen("random", "Random", Icons.Default.Star)
    object List : Screen("list", "List", Icons.AutoMirrored.Filled.List)
    object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(Screen.Random, Screen.List, Screen.Favorites)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF1A1128)) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.List.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Random.route) {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF1A1128)) {
                    DetailCocktailScreen()
                }
            }
            composable(Screen.List.route) {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF1A1128)) {
                    CategoriesScreen()
                }
            }
            composable(Screen.Favorites.route) {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF1A1128)) {
                    FavoritesScreen()
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen() {
    val context = LocalContext.current
    var favoriteDrinks by remember { mutableStateOf<List<Drink>>(emptyList()) }

    // Reload favorites when screen is shown
    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val favoritesJson = prefs.getString("favorite_list", null)
        if (favoritesJson != null) {
            val type = object : TypeToken<List<Drink>>() {}.type
            favoriteDrinks = Gson().fromJson(favoritesJson, type)
        }
    }

    if (favoriteDrinks.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No favorites yet", color = Color.White)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(favoriteDrinks) { drink ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(context, DetailActivity::class.java)
                            intent.putExtra("drinkId", drink.id)
                            context.startActivity(intent)
                        },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2438))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = drink.thumb,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = drink.name,
                            modifier = Modifier.padding(16.dp),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
