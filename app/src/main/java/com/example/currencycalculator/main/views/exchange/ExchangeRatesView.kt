package com.example.currencycalculator.main.views.exchange

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.currencycalculator.main.navigation.Destination
import com.example.currencycalculator.utils.Currency
import com.example.currencycalculator.utils.SuffixTransformation
import com.example.data.models.ExchangeRates
import com.example.data.respositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.androidx.compose.get

@OptIn(ExperimentalUnitApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRatesView(viewModel: ExchangeRatesViewModel = get()) {

    val state = viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Text(
            text = Destination.Exchange.label,
            fontSize = TextUnit(25f, TextUnitType.Sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.value.baseCurrencyValue.toString(),
                modifier = Modifier.weight(1f),
                label = { Text(text = "Type value:") },
                singleLine = true,
                visualTransformation = SuffixTransformation(" ${state.value.baseCurrency}"),
                textStyle = TextStyle(
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    textAlign = TextAlign.Right
                ),
                onValueChange = { stringValue ->
                    val value = stringValue.toFloatOrNull() ?: return@OutlinedTextField
                    viewModel.intent(ExchangeRatesAction.SetValue(value))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            IconButton(
                onClick = { viewModel.intent(ExchangeRatesAction.SetCurrency("EUR")) } // todo
            ) {
                Box(
                    modifier = Modifier.padding(1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = Currency.getFlag(state.value.baseCurrency),
                        fontSize = TextUnit(25f, TextUnitType.Sp)
                    )
                    Icon(
                        modifier = Modifier
                            .align(BottomEnd)
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color.Gray, CircleShape),
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Set base currency"
                    )
                }
            }
        }

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            if (state.value.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    trackColor = Color.LightGray,
                    color = Color.Black
                )
            } else {
                Spacer(modifier = Modifier.size(0.dp, 2.dp))
            }
        }

        Divider(modifier = Modifier.padding(bottom = 8.dp), color = Color.Black, thickness = 1.dp)

        when {
            state.value.isEmpty -> EmptyExchangeRatesView()
            state.value.error != null -> ErrorExchangeRatesView(error = state.value.error)
            state.value.outputs.isNotEmpty() -> {
                LazyColumn {
                    state.value.outputs.forEachIndexed { index, exchangeRate ->
                        item {
                            ExchangeRateView(
                                baseCurrency = state.value.baseCurrency,
                                exchangeRate = exchangeRate
                            )
                            if (index < state.value.outputs.size - 1) {
                                Divider(
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    color = Color.Gray,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyExchangeRatesView() {
    // no any view
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ErrorExchangeRatesView(error: Throwable?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 40.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = error?.message ?: "Error",
            fontSize = TextUnit(25f, TextUnitType.Sp),
            color = Color.Red
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExchangeRatesViewPreview() {

    val mockViewModel = ExchangeRatesViewModel(object : CurrencyRepository {
        override fun getExchangeRates(baseCurrency: String): Flow<ExchangeRates> {
            return flow { }
        }
    })

    ExchangeRatesView(viewModel = mockViewModel)
}



