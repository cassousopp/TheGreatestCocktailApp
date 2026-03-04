package fr.isen.sopp.thegreatestcocktailapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCocktailScreen() {
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = { /* Refresh logic */ }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    }
                    IconButton(onClick = {
                        Toast.makeText(context, "Added to favorites!", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1128))
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF1A1128)) {
                NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Star, contentDescription = null) }, label = { Text("A la une") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Refresh, contentDescription = null) }, label = { Text("Categories") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = null) }, label = { Text("Favoris") })
                NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Search, contentDescription = null) }, label = { Text("Recherche") })
            }
        },
        containerColor = Color(0xFF1A1128)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image (Circular)
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cocktail),
                    contentDescription = "Yoghurt Cooler",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Yoghurt Cooler",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Badge(containerColor = Color(0xFF3F51B5), contentColor = Color.White) {
                    Text("Other / Unknown", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
                Badge(containerColor = Color(0xFF4CAF50), contentColor = Color.White) {
                    Text("Non alcoholic", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Highball Glass", color = Color.LightGray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ingredients Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2438)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ingrédients", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    IngredientItem("Yoghurt", "1 cup")
                    IngredientItem("Fruit", "1 cup")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Recipe Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2438)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Recipe", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Place all ingredients in a blender and blend until smooth. Serve in a highball glass.",
                        color = Color.White
                    )
                }
            }
        }
    }
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
