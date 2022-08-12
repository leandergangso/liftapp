package com.patrykandpatryk.liftapp.domain.format

import java.util.Calendar

interface Formatter {

    suspend fun formatDate(millis: Long, dateFormat: DateFormat): String

    suspend fun getFormattedDate(millis: Long): FormattedDate

    suspend fun getFormattedDate(calendar: Calendar): FormattedDate

    suspend fun is24H(): Boolean

    fun formatNumber(number: Number, format: NumberFormat): String

    fun parseFloatOrZero(value: String): Float

    enum class DateFormat {
        TimeShort,
        TimeLong,
        DateShort,
        DateLong,
    }

    enum class NumberFormat {
        Decimal,
        Integer,
    }
}
