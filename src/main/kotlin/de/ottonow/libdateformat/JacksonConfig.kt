package de.ottonow.libdateformat

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

@Configuration
open class JacksonConfig {

    companion object {
        val defensiveFormatter = DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .appendPattern("[XXX][XX][X]")
            .toFormatter()

        fun customDateTimeModule(): SimpleModule {
            val result = SimpleModule()
            result.addDeserializer(OffsetDateTime::class.java, DefensiveIsoOffsetDateTimeDeserializer())
            result.addSerializer(OffsetDateTime::class.java, OffsetDateTimeSerializer())
            result.addDeserializer(LocalDate::class.java, LocalDateDeserializer())
            result.addSerializer(LocalDate::class.java, LocalDateSerializer())
            result.addSerializer(Instant::class.java, InstantSerializer())
            result.addDeserializer(Instant::class.java, DefensiveInstantDeserializer())
            return result
        }
    }

    @Bean
    open fun jacksonCustomizer() = Jackson2ObjectMapperBuilderCustomizer { jacksonObjectMapperBuilder ->
        jacksonObjectMapperBuilder.modules(KotlinModule(), customDateTimeModule())
    }

    class InstantSerializer : JsonSerializer<Instant>() {
        override fun serialize(value: Instant, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeString(value.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME))
        }
    }

    class DefensiveInstantDeserializer : JsonDeserializer<Instant>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Instant {
            return OffsetDateTime.parse(p.text, defensiveFormatter).toInstant()
        }
    }

    class LocalDateSerializer : JsonSerializer<LocalDate>() {
        override fun serialize(value: LocalDate, gen: JsonGenerator, serializers: SerializerProvider) =
            gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    class LocalDateDeserializer : JsonDeserializer<LocalDate>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            LocalDate.parse(p.text, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    class OffsetDateTimeSerializer : JsonSerializer<OffsetDateTime>() {
        override fun serialize(value: OffsetDateTime, gen: JsonGenerator, serializers: SerializerProvider) =
            gen.writeString(value.format(DateTimeFormatter.ISO_DATE_TIME))

        override fun handledType() = OffsetDateTime::class.java
    }

    class DefensiveIsoOffsetDateTimeDeserializer : JsonDeserializer<OffsetDateTime>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            OffsetDateTime.parse(p.text, defensiveFormatter)

        override fun handledType() = OffsetDateTime::class.java
    }
}