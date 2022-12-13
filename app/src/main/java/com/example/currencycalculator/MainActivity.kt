package com.example.currencycalculator

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.composable
import com.example.currencycalculator.navigation.Destination
import com.example.currencycalculator.navigation.MainActivityNavigation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MainActivityNavigation {
                    composable(Destination.Exchange.route) {
                        Text(text = "exchange")
                    }
                    composable(Destination.Settings.route) {
                        Text(text = "settings")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 700, heightDp = 500)
@Composable
fun MainActivityNavigationPreview() {
    MainActivityNavigation {
        composable(Destination.Exchange.route) {
            Text(text = "exchange")
        }
        composable(Destination.Settings.route) {
            Text(text = "settings")
        }
    }
}