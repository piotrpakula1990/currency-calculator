package com.example.currencycalculator.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class SuffixTransformation(private val suffix: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return suffixFilter(text, suffix)
    }

    private fun suffixFilter(number: AnnotatedString, suffix: String): TransformedText {

        val out = number.text + suffix
        val suffixOffset = suffix.length

        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset >= suffixOffset) {
                    return number.text.length
                }
                return offset
            }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }
}