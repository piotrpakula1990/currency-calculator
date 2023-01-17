package com.example.currencycalculator.main.views.exchange

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle.Event.*
import com.example.currencycalculator.R
import com.example.currencycalculator.main.navigation.Destination
import com.example.currencycalculator.main.theme.AppTheme
import com.example.currencycalculator.main.views.dialogs.ChooseCurrencyDialog
import com.example.currencycalculator.utils.SuffixTransformation
import com.example.data.models.Currency
import com.example.data.models.ExchangeRates
import com.example.data.models.Settings
import com.example.data.respositories.currency.CurrencyRepository
import com.example.data.respositories.settings.SettingsRepository
import com.example.currencycalculator.main.views.exchange.ExchangeRatesUiState.*
import com.example.currencycalculator.main.views.exchange.ExchangeRatesAction.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalUnitApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRatesView(viewModel: ExchangeRatesViewModel = koinViewModel()) {

    val state = viewModel.state.collectAsState()

    var isOpenChooseCurrencyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
    ) {
        Text(
            text = Destination.Exchange.label,
            fontSize = TextUnit(25f, TextUnitType.Sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                enabled = state.value !is Initialize,
                value = state.value.value.toString(),
                modifier = Modifier.weight(1f),
                label = { Text(text = stringResource(id = R.string.exchange_rates_type_value)) },
                singleLine = true,
                visualTransformation = SuffixTransformation(" ${state.value.baseCurrency}"),
                textStyle = TextStyle(
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    textAlign = TextAlign.Right
                ),
                onValueChange = { stringValue ->
                    val value = stringValue.toFloatOrNull() ?: return@OutlinedTextField
                    viewModel.intent(SetValue(value))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            IconButton(
                enabled = state.value !is Initialize,
                onClick = { isOpenChooseCurrencyDialog = true }
            ) {
                Box(
                    modifier = Modifier.padding(1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.value.baseCurrency.flag,
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
                        contentDescription = stringResource(id = R.string.exchange_rates_set_currency_description)
                    )
                }
            }
        }

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            if (state.value is Loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    trackColor = MaterialTheme.colorScheme.onSecondary,
                    color = MaterialTheme.colorScheme.secondary
                )
            } else {
                Spacer(modifier = Modifier.size(0.dp, 2.dp))
            }
        }

        Divider(
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.onSurface,
            thickness = 1.dp
        )

        when (val uiState = state.value) {
            Initialize -> EmptyExchangeRatesView()
            is Error -> ErrorExchangeRatesView(error = uiState.error)
            is Loading -> EmptyExchangeRatesView()
            is Result -> {
                LazyColumn {
                    uiState.outputs.forEachIndexed { index, calculatedExchangeRate ->
                        item {
                            ExchangeRateView(
                                baseCurrency = state.value.baseCurrency,
                                calculatedExchangeRate = calculatedExchangeRate
                            )
                            if (index < uiState.outputs.size - 1) {
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

    if (isOpenChooseCurrencyDialog) {
        ChooseCurrencyDialog(
            onDismiss = { isOpenChooseCurrencyDialog = false },
            onChoseCurrency = { currency ->
                isOpenChooseCurrencyDialog = false
                viewModel.intent(SetCurrency(currency))
            }
        )
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
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExchangeRatesViewPreview() {

    val mockViewModel = ExchangeRatesViewModel(
        currencyRepository = object : CurrencyRepository {

            override fun getExchangeRates(baseCurrency: Currency): Flow<ExchangeRates> = flow { }
        },
        settingsRepository = object : SettingsRepository {

            override fun getSettings(): Flow<Settings> = flow { }

            override suspend fun setBaseCurrency(baseCurrency: Currency) {}

            override suspend fun setValuePrecision(valuePrecision: Int) {}

            override suspend fun setCurrenciesOrder(order: List<Currency>) {}
        }
    )

    AppTheme {
        ExchangeRatesView(viewModel = mockViewModel)
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ExchangeRatesViewPreviewNightMode() {

    val mockViewModel = ExchangeRatesViewModel(
        currencyRepository = object : CurrencyRepository {

            override fun getExchangeRates(baseCurrency: Currency): Flow<ExchangeRates> = flow { }
        },
        settingsRepository = object : SettingsRepository {

            override fun getSettings(): Flow<Settings> = flow { }

            override suspend fun setBaseCurrency(baseCurrency: Currency) {}

            override suspend fun setValuePrecision(valuePrecision: Int) {}

            override suspend fun setCurrenciesOrder(order: List<Currency>) {}
        }
    )

    AppTheme {
        ExchangeRatesView(viewModel = mockViewModel)
    }
}



