package com.example.currencycalculator.main.views.dialogs

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.example.currencycalculator.R
import com.example.currencycalculator.main.theme.AppTheme
import com.example.data.models.Currency
import kotlin.math.roundToInt

private data class DragAndDropItem<T>(val value: T, val isHidden: Boolean, val translation: Float)

@OptIn(ExperimentalUnitApi::class)
@Composable
fun OrderCurrencyDialog(
    initOrder: List<Currency>,
    onDismiss: () -> Unit = {},
    onSave: (List<Currency>) -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .clip(RoundedCornerShape(5.dp))
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.inverseSurface), shape = RoundedCornerShape(5.dp)),
            shadowElevation = 5.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.dialogs_order_title),
                    fontSize = TextUnit(25f, TextUnitType.Sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Divider(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    thickness = 1.dp
                )

                DragAndDropLazyList(initOrder = initOrder, onSave)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DragAndDropLazyList(initOrder: List<Currency>, onSave: (List<Currency>) -> Unit) {

    val initItems = initOrder
        .map { currency -> DragAndDropItem(currency, !initOrder.contains(currency), 0f) }
        .plus(
            Currency.getAll()
                .filter { currency -> !initOrder.contains(currency) }
                .map { currency -> DragAndDropItem(currency, !initOrder.contains(currency), 0f) }
        )
        .toTypedArray()

    val items = remember { mutableStateListOf(*initItems) }
    val height = 36.dp
    val itemHeightPx = LocalDensity.current.run { 32.dp.toPx() }

    var bufferIndexTranslation = 0

    fun resetAllTranslation() {
        val buff = items.map { it.copy(translation = 0f) }
        items.clear()
        items.addAll(buff)
        bufferIndexTranslation = 0
    }

    LazyColumn {
        items(items.size) { index ->
            val item = items.getOrNull(index) ?: return@items

            key(item) {
                DragAndDropExampleItem(
                    item = item,
                    itemHeight = height,
                    onMove = { indexTranslation ->
                        // disable any change for same indexTranslation.
                        if (bufferIndexTranslation == indexTranslation) return@DragAndDropExampleItem

                        val toIndex = index + indexTranslation
                        val fromIndex = index + bufferIndexTranslation

                        when {
                            indexTranslation > 0 -> { // is drag item bellow start position
                                if (toIndex >= items.size) return@DragAndDropExampleItem
                                items[toIndex] = items[toIndex].copy(translation = -itemHeightPx)
                                if (indexTranslation < bufferIndexTranslation) { // is drag item above last position
                                    items[fromIndex] = items[fromIndex].copy(translation = 0f)
                                }
                            }
                            indexTranslation < 0 -> { // is drag item above start position
                                if (toIndex < 0) return@DragAndDropExampleItem
                                items[toIndex] = items[toIndex].copy(translation = itemHeightPx)
                                if (indexTranslation > bufferIndexTranslation) {  // is drag item bellow last position
                                    items[fromIndex] = items[fromIndex].copy(translation = 0f)
                                }
                            }
                            else -> {  // is drag item at begin position
                                items[toIndex] = items[toIndex].copy(translation = 0f)
                                if (bufferIndexTranslation != 0) { // is drag item above or bellow last position
                                    items[fromIndex] = items[fromIndex].copy(translation = 0f)
                                }
                            }
                        }

                        bufferIndexTranslation = indexTranslation // update buffer
                    },
                    onDrop = { indexTranslation ->
                        val toIndex = index + indexTranslation
                        if (indexTranslation != 0 && toIndex >= 0 && toIndex <= items.size - 1) {
                            items.remove(item)
                            items.add(toIndex, item)

                            resetAllTranslation()
                            onSave(items.filter { !it.isHidden }.map { it.value })
                            true // success
                        } else {
                            resetAllTranslation()
                            false // error
                        }
                    },
                    onChecked = { isChecked ->
                        items[index] = items[index].copy(isHidden = !isChecked)
                        onSave(items.filter { !it.isHidden }.map { it.value })
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@ExperimentalAnimationApi
@Composable
private fun DragAndDropExampleItem(
    item: DragAndDropItem<Currency>,
    itemHeight: Dp,
    onMove: (indexTranslation: Int) -> Unit,
    onDrop: (indexTranslation: Int) -> Boolean, // isSuccess
    onChecked: (isChecked: Boolean) -> Unit,
) {
    val heightPx = LocalDensity.current.run { itemHeight.toPx() }
    var offsetY by remember { mutableStateOf(0f) }
    val zIndex = if (offsetY != 0f) 1f else 0f

    val verticalTranslation by animateIntAsState(targetValue = item.translation.toInt())

    Row(
        modifier = Modifier
            .offset { IntOffset(0, offsetY.roundToInt() + verticalTranslation) }
            .fillMaxWidth()
            .padding(1.dp)
            .background(color = MaterialTheme.colorScheme.surface)
            .height(itemHeight)
            .border(1.dp, MaterialTheme.colorScheme.inverseSurface, RoundedCornerShape(3.dp))
            .padding(2.dp)
            .zIndex(zIndex),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = !item.isHidden,
            onCheckedChange = { onChecked(it) },
        )

        Text(
            text = item.value.flag,
            fontSize = TextUnit(20f, TextUnitType.Sp),
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .width(0.dp)
                .weight(1f),
            text = "${item.value.name} (${stringResource(id = item.value.fullNameId)})",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )

        Icon(
            modifier = Modifier
                .size(34.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = {}
                )
                .draggable(
                    state = rememberDraggableState { delta ->
                        offsetY += delta

                        if (offsetY != 0f) {
                            onMove((offsetY / heightPx).roundToInt())
                        }
                    },
                    orientation = Orientation.Vertical,
                    onDragStopped = {
                        if (offsetY != 0f) {
                            val isSuccess = onDrop((offsetY / heightPx).roundToInt())
                            if (!isSuccess) offsetY = 0f
                        }
                    }
                ),
            imageVector = Icons.Filled.UnfoldMore,
            contentDescription = stringResource(id = R.string.dialogs_order_move_description)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OrderCurrencyDialogPreview() {
    ChooseCurrencyDialog()
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OrderCurrencyDialogPreviewNightMode() {
    AppTheme {
        ChooseCurrencyDialog()
    }
}