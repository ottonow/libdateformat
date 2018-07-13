# Relaxed Date Time

This library adds relaxed date time parsing for APIs in Spring Boot 2 using Jackson. The library is configured automatically, when included.

[![Build Status](https://travis-ci.org/ottonow/relaxed-date-time.svg?branch=master)](https://travis-ci.org/ottonow/relaxed-date-time)

## Supported types

* Instant
* OffsetDateTime
* LocalDate

## Supported Formats

### DateTime

* 2018-08-08T17:23:59.000Z
* 2018-08-08T17:23:59Z
* 2018-08-08T17:23:59+00:00
* 2018-08-08T17:23:59+0000
* 2018-08-08T17:23:59+00
* 2018-08-08T17:23:59

### Date

* 2018-08-08
* TBD

## Usage

### DateTime in Dtos

The parser is auto-configured for the default Spring Boot 2 Jackson ObjectMapper.

```kotlin
data class Dto (
    private val createdAt: Instant
)
```

```kotlin
@RequestMapping(value = ["/my-service/create"], method = [RequestMethod.PUT])
fun createDto(@RequestBody dto: Dto) {
    println(dto.createdAt)
}
```

### RequestParam

When parsing date time as RequestParam, you need to use `@IsoDateTime` explicitly.


```kotlin
@RequestMapping(value = ["/my-service/since"], method = [RequestMethod.GET])
fun findEntitiesSince(
    @IsoDateTime since: Instant
) {
    println(since)
}

```