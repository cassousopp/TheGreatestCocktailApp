package fr.isen.sopp.thegreatestcocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import fr.isen.sopp.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheGreatestCocktailAppTheme {
                DetailCocktailScreen()
            }
        }
    }
}
