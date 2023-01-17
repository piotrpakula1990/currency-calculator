package com.example.currencycalculator.main.views.settings

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.currencycalculator.R
import com.example.currencycalculator.main.navigation.Destination
import com.example.currencycalculator.main.theme.AppTheme
import com.example.currencycalculator.main.views.dialogs.ChooseCurrencyDialog
import com.example.currencycalculator.main.views.dialogs.OrderCurrencyDialog
import com.example.currencycalculator.main.views.settings.SettingsUiState.*
import com.example.currencycalculator.main.views.settings.SettingsAction.*
import com.example.data.models.Settings
import com.example.data.models.Currency
import com.example.data.respositories.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalUnitApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(viewModel: SettingsViewModel = koinViewModel()) {

    val state = viewModel.state.collectAsState()
    val result = state.value as? Result

    var isOpenChooseCurrencyDialog by remember { mutableStateOf(false) }
    var isOpenOrderDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
    ) {
        Text(
            text = Destination.Settings.label,
            fontSize = TextUnit(25f, TextUnitType.Sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        SettingRow(label = stringResource(id = R.string.settings_default_base), content = {
            val fullCurrencyName = stringResource(id = state.value.baseCurrency.fullNameId)
            Text(
                text = "${state.value.baseCurrency} $fullCurrencyName",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onSurface
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
                            .align(Alignment.BottomEnd)
                            .size(15.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color.Gray, CircleShape),
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = stringResource(id = R.string.settings_default_base_description)
                    )
                }
            }
        })

        SettingRow(label = stringResource(id = R.string.settings_value_precision), content = {
            Text(
                text = state.value.valuePrecisionPreview,
                modifier = Modifier
                    .weight(0.8f)
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onSurface
            )

            TextField(
                value = state.value.valuePrecision.toString(),
                enabled = state.value !is Initialize,
                modifier = Modifier.weight(0.2f),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    textColor = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    textAlign = TextAlign.Center,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { stringValue ->
                    val value = stringValue
                        .firstOrNull { it != state.value.valuePrecision.digitToChar() }
                        ?.digitToIntOrNull()
                        ?: return@TextField

                    viewModel.intent(SetValuePrecision(value))
                })
        })

        SettingRow(
            label = stringResource(id = R.string.settings_value_filter_and_order),
            content = {
                Text(
                    text = state.value.orderPreview,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(
                    enabled = state.value !is Initialize,
                    onClick = { isOpenOrderDialog = true }
                ) {
                    Box(
                        modifier = Modifier.padding(1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(30.dp)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onSurface,
                                    RoundedCornerShape(3.dp)
                                ),
                            imageVector = Icons.Filled.FormatListBulleted,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(id = R.string.settings_value_filter_and_order_description)
                        )

                        Icon(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, Color.Gray, CircleShape),
                            imageVector = Icons.Filled.UnfoldMore,
                            contentDescription = stringResource(id = R.string.settings_value_filter_and_order_description)
                        )
                    }
                }
            })

        if (isOpenChooseCurrencyDialog) {
            ChooseCurrencyDialog(
                onDismiss = { isOpenChooseCurrencyDialog = false },
                onChoseCurrency = { currency ->
                    isOpenChooseCurrencyDialog = false
                    viewModel.intent(SetBaseCurrency(currency))
                }
            )
        }

        if (isOpenOrderDialog && result != null) {
            OrderCurrencyDialog(
                initOrder = result.order,
                onDismiss = { isOpenOrderDialog = false },
                onSave = { viewModel.intent(SetOrder(order = it)) }
            )
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun SettingRow(label: String, content: @Composable RowScope.() -> Unit) {
    Column {
        Text(
            text = label,
            textAlign = TextAlign.Left,
            fontSize = TextUnit(12f, TextUnitType.Sp),
            color = MaterialTheme.colorScheme.onSurface
        )

        Divider(color = MaterialTheme.colorScheme.onSurfaceVariant, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content(this)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsViewPreview() {
    val mockViewModel = SettingsViewModel(object : SettingsRepository {

        override fun getSettings(): Flow<Settings> = flow { }

        override suspend fun setBaseCurrency(baseCurrency: Currency) {}

        override suspend fun setValuePrecision(valuePrecision: Int) {}

        override suspend fun setCurrenciesOrder(order: List<Currency>) {}
    })

    AppTheme {
        SettingsView(viewModel = mockViewModel)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsViewPreviewNightMode() {
    val mockViewModel = SettingsViewModel(object : SettingsRepository {

        override fun getSettings(): Flow<Settings> = flow { }

        override suspend fun setBaseCurrency(baseCurrency: Currency) {}

        override suspend fun setValuePrecision(valuePrecision: Int) {}

        override suspend fun setCurrenciesOrder(order: List<Currency>) {}
    })

    AppTheme {
        SettingsView(viewModel = mockViewModel)
    }
}