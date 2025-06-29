/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object CustomInstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        val localDateTime = value.toLocalDateTime(TimeZone.UTC)
        val formatted = "${localDateTime.date} ${localDateTime.time}"
        encoder.encodeString(formatted)
    }

    override fun deserialize(decoder: Decoder): Instant {
        val string = decoder.decodeString()

        // API may return this as a trending_datetime, we are not currently using this anyway
        if ("0000-00-00 00:00:00" == string) {
            return Instant.DISTANT_PAST
        }

        return try {
            val (datePart, timePart) = string.split(" ")
            val (year, month, day) = datePart.split("-").map { it.toInt() }
            val (hour, minute, second) = timePart.split(":").map { it.toInt() }
            val localDateTime = LocalDateTime(year, month, day, hour, minute, second)
            localDateTime.toInstant(TimeZone.UTC)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid date-time format: $string", e)
        }
    }
}
