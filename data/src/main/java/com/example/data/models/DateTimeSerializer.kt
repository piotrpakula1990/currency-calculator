package com.example.data.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joda.time.DateTime

object DateTimeSerializer : KSerializer<DateTime> {
    override val descriptor = PrimitiveSerialDescriptor("DateTime", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: DateTime) = encoder.encodeLong(value.millis)
    override fun deserialize(decoder: Decoder): DateTime = DateTime(decoder.decodeLong())
}