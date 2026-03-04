package fr.isen.sopp.thegreatestcocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fr.isen.sopp.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val drinkId = intent.getStringExtra("drinkId")
        setContent {
            TheGreatestCocktailAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF1A1128)
                ) {
                    DetailCocktailScreen(drinkId)
                }
            }
        }
    }
}
