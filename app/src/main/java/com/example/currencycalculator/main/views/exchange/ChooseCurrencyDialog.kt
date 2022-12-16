package com.example.currencycalculator.main.views.exchange

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.currencycalculator.utils.Currency

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ChooseCurrencyDialog(
    onDismiss: () -> Unit = {},
    onChoseCurrency: (String) -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismiss() }) {

        Surface(
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .clip(RoundedCornerShape(5.dp))
                .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(5.dp)),
            shadowElevation = 5.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Choose Base Currency",
                    fontSize = TextUnit(25f, TextUnitType.Sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    textAlign = TextAlign.Center
                )

                Divider(
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = Color.Black,
                    thickness = 1.dp
                )

                LazyColumn {
                    val size = Currency.values().size
                    Currency.values().forEachIndexed { index, currency ->
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple(color = Color.Gray),
                                        onClick = { onChoseCurrency(currency.name)}
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        modifier = Modifier.padding(end = 8.dp),
                                        text = currency.flag,
                                        fontSize = TextUnit(20f, TextUnitType.Sp)
                                    )
                                }

                                Text(text = "${currency.name} (${currency.fullName})")
                            }

                            if (index < size - 1) {
                                Divider(
                                    modifier = Modifier.padding(vertical = 2.dp),
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

@Preview(showBackground = true)
@Composable
fun ChooseCurrencyDialogPreview() {
    ChooseCurrencyDialog()
}