package com.example.currencycalculator.main.views.settings

import android.annotation.SuppressLint
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
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.currencycalculator.R
import com.example.currencycalculator.main.navigation.Destination
import com.example.currencycalculator.main.views.dialogs.ChooseCurrencyDialog
import org.koin.androidx.compose.get

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalUnitApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(viewModel: SettingsViewModel = get()) {

    val state = viewModel.state.collectAsState()

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
            textAlign = TextAlign.Center
        )

        SettingRow(label = stringResource(id = R.string.settings_default_base), content = {
            val fullCurrencyName = stringResource(id = state.value.defaultBaseCurrency.fullNameId)
            Text(
                text = "${state.value.defaultBaseCurrency} $fullCurrencyName",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Left
            )

            IconButton(
                onClick = { isOpenChooseCurrencyDialog = true }
            ) {
                Box(
                    modifier = Modifier.padding(1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.value.defaultBaseCurrency.flag,
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

            val zeros = (0 until state.value.valuePrecision - 1).joinToString("") { "0" }

            Text(
                text = "1.${zeros}1",
                modifier = Modifier
                    .weight(0.8f)
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Left
            )

            TextField(
                value = state.value.valuePrecision.toString(),
                modifier = Modifier.weight(0.2f),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                ),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    textAlign = TextAlign.Center,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { stringValue ->
                    val value = stringValue.toIntOrNull() ?: return@TextField
                    viewModel.intent(SettingsAction.SetValuePrecision(value))
                })
        })

        SettingRow(
            label = stringResource(id = R.string.settings_value_filter_and_order),
            content = {
                val examplePrecision = viewModel.state.value.order
                    .take(4)
                    .joinToString()
                    .plus(", ...")

                Text(
                    text = examplePrecision,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Left
                )

                IconButton(onClick = { isOpenOrderDialog = true }) {
                    Box(
                        modifier = Modifier.padding(1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(30.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(3.dp)),
                            imageVector = Icons.Filled.FormatListBulleted,
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
                    viewModel.intent(SettingsAction.SetBaseCurrency(currency))
                }
            )
        }

        if (isOpenOrderDialog) {
            // todo create dialog
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
            fontSize = TextUnit(12f, TextUnitType.Sp)
        )

        Divider(color = Color.Gray, thickness = 1.dp)

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
    val mockViewModel = SettingsViewModel()
    SettingsView(viewModel = mockViewModel)
}