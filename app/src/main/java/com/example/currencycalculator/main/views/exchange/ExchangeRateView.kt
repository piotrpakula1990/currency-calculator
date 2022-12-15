package com.example.currencycalculator.main.views.exchange

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.currencycalculator.utils.Currency

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ExchangeRateView(baseCurrency: String, exchangeRate: CalculatedExchangeRate) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 24.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${exchangeRate.calculatedValue} ${exchangeRate.currency}",
                fontSize = TextUnit(15f, TextUnitType.Sp)
            )

            Text(
                text = "1 $baseCurrency = ${exchangeRate.rate} ${exchangeRate.currency}",
                color = Color.Gray,
                fontSize = TextUnit(10f, TextUnitType.Sp)
            )
        }

        Box(contentAlignment = Alignment.Center) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = Currency.getFlag(exchangeRate.currency),
                fontSize = TextUnit(25f, TextUnitType.Sp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExchangeRateViewPreview() {
    val mock = CalculatedExchangeRate("USD", 1.5f, 5.5f)
    ExchangeRateView(baseCurrency = "EUR", exchangeRate = mock)
}


