/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// annotation class SerialNameDateTimeFormat(val format: String)

class CustomDateTimeAdapterKtor : KSerializer<Date> {
    private val format = "yyyy-MM-dd HH:mm:ss"

    private val dateFormat = SimpleDateFormat(format, Locale.getDefault())

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        val formattedDate = dateFormat.format(value)
        encoder.encodeString(formattedDate)
    }

    override fun deserialize(decoder: Decoder): Date {
        val dateStr = decoder.decodeString()
        return dateFormat.parse(dateStr)!!
    }
}

object CustomDateTimeAdapterKtorFactory {
    fun create(): CustomDateTimeAdapterKtor {
        return CustomDateTimeAdapterKtor()
    }
}
