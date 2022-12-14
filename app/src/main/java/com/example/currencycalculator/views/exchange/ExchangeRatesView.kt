package com.example.currencycalculator.views.exchange

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.currencycalculator.navigation.Destination
import com.example.currencycalculator.utils.Currency
import com.example.currencycalculator.utils.SuffixTransformation

@OptIn(ExperimentalUnitApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRatesView() {

    var value by remember { mutableStateOf(1.0f) }
    val baseCurrency by remember { mutableStateOf(Currency.EUR) }

    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Text(
            text = Destination.Exchange.label,
            fontSize = TextUnit(25f, TextUnitType.Sp),
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value.toString(),
                modifier = Modifier.weight(1f),
                label = { Text(text = "Type value:") },
                singleLine = true,
                visualTransformation = SuffixTransformation(" ${baseCurrency.name}"),
                textStyle = TextStyle(
                    fontSize = TextUnit(18f, TextUnitType.Sp),
                    textAlign = TextAlign.Right
                ),
                onValueChange = { it.toFloatOrNull()?.let { value = it } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            IconButton(onClick = {}) {
                Box(
                    modifier = Modifier.padding(1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = Currency.EUR.flag, fontSize = TextUnit(25f, TextUnitType.Sp))
                    Icon(
                        modifier = Modifier.align(BottomEnd).size(15.dp).clip(CircleShape).background(Color.White).border(1.dp, Color.Gray, CircleShape),
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = ""
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Black, thickness = 1.dp)

        LazyColumn {
            item {
                ExchangeRateView()
                Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.Gray, thickness = 1.dp)
            }
            item {
                ExchangeRateView()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExchangeRatesViewPreview() {
    ExchangeRatesView()
}



