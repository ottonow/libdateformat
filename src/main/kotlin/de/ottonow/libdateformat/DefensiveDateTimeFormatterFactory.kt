package de.ottonow.libdateformat

import org.springframework.context.support.EmbeddedValueResolutionSupport
import org.springframework.format.AnnotationFormatterFactory
import org.springframework.format.Parser
import org.springframework.format.Printer
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class DefensiveDateTimeFormatterFactory : EmbeddedValueResolutionSupport(), AnnotationFormatterFactory<IsoDateTime> {

    override fun getParser(annotation: IsoDateTime?, fieldType: Class<*>?): Parser<*> {
        return Parser<OffsetDateTime> { text, _ -> OffsetDateTime.parse(text, JacksonConfig.defensiveFormatter) }
    }

    override fun getPrinter(annotation: IsoDateTime, fieldType: Class<*>): Printer<*> {
        return Printer<OffsetDateTime> { obj, _ -> obj.format(DateTimeFormatter.ISO_DATE_TIME) }
    }

    override fun getFieldTypes(): MutableSet<Class<*>> {
        return mutableSetOf(OffsetDateTime::class.java)
    }
}