package com.example.currencycalculator.main.views.exchange

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
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
import com.example.data.models.Currency
import com.example.data.models.ExchangeRate

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ExchangeRateView(baseCurrency: Currency, calculatedExchangeRate: CalculatedExchangeRate) {
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
                text = "${calculatedExchangeRate.calculatedValue} ${calculatedExchangeRate.exchangeRate.currency}",
                fontSize = TextUnit(15f, TextUnitType.Sp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "1 $baseCurrency = ${calculatedExchangeRate.exchangeRate.currencyRate} ${calculatedExchangeRate.exchangeRate.currency}",
                color = Color.Gray,
                fontSize = TextUnit(10f, TextUnitType.Sp)
            )
        }

        Box(contentAlignment = Alignment.Center) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = calculatedExchangeRate.exchangeRate.currency.flag,
                fontSize = TextUnit(25f, TextUnitType.Sp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExchangeRateViewPreview() {
    val mock = CalculatedExchangeRate(ExchangeRate(Currency.USD, 1.5f), 5.5f)
    ExchangeRateView(baseCurrency = Currency.EUR, calculatedExchangeRate = mock)
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ExchangeRateViewPreviewNightMode() {
    val mock = CalculatedExchangeRate(ExchangeRate(Currency.USD, 1.5f), 5.5f)
    ExchangeRateView(baseCurrency = Currency.EUR, calculatedExchangeRate = mock)
}


