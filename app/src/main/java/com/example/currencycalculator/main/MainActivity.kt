package com.example.currencycalculator.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.composable
import com.example.currencycalculator.main.navigation.Destination
import com.example.currencycalculator.main.navigation.MainActivityNavigation
import com.example.currencycalculator.main.theme.AppTheme
import com.example.currencycalculator.main.views.exchange.ExchangeRatesView
import com.example.currencycalculator.main.views.settings.SettingsView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                MainActivityNavigation {
                    composable(Destination.Exchange.route) {
                        ExchangeRatesView()
                    }
                    composable(Destination.Settings.route) {
                        SettingsView()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityNavigationPreview() {
    MaterialTheme {
        MainActivityNavigation {
            composable(Destination.Exchange.route) {
                ExchangeRatesView()
            }
            composable(Destination.Settings.route) {
                Text(text = "settings")
            }
        }
    }
}