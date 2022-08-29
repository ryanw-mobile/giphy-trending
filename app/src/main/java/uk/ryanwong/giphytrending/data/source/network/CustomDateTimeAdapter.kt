package uk.ryanwong.giphytrending.data.source.network

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
formats: yyyy-MM-dd, yyyy-MM-dd HH:mm:ss, HH:mm:ss etc
 */
@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
internal annotation class JsonDateTimeFormat(val format: String)

class CustomDateTimeAdapter(private val format: String) : JsonAdapter<Date?>() {

    override fun fromJson(reader: JsonReader): Date? {
        val string = reader.nextString()
        return SimpleDateFormat(format, Locale.getDefault()).parse(string)
    }

    override fun toJson(writer: JsonWriter, value: Date?) {
        value?.let {
            writer.value(SimpleDateFormat(format, Locale.getDefault()).format(it))
        }
    }

    class Factory : JsonAdapter.Factory {
        override fun create(
            type: Type,
            annotations: MutableSet<out Annotation>,
            moshi: Moshi
        ): JsonAdapter<Date?>? {

            if (annotations.size <= 1 && type != Date::class.java) return null

            val annotation = annotations.iterator().next() as? JsonDateTimeFormat ?: return null
            val format = annotation.format
            return CustomDateTimeAdapter(format).nullSafe()
        }
    }
}
