package de.ottonow.libdateformat

import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration::class)
open class DateFormatAutoConfiguration : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addFormatterForFieldAnnotation(DefensiveDateTimeFormatterFactory())
    }
}