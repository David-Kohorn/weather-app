/**
 * David Kohorn
 */
package wpics.weather.ui.components

import wpics.weather.model.weather.UnitSystem
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import kotlin.math.roundToInt

// OpenWeather's 5-day forecast returns timestamps as "yyyy-MM-dd HH:mm:ss".
fun parseForecastDateTime(value: String): LocalDateTime =
    LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

fun formatTemp(temp: Double, units: UnitSystem): String {
    val symbol = when (units) {
        UnitSystem.METRIC -> "°C"
        UnitSystem.IMPERIAL -> "°F"
        UnitSystem.STANDARD -> "K"
    }
    return "${temp.roundToInt()}$symbol"
}

fun formatTime(epochSeconds: Long): String =
    DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())
        .format(Instant.ofEpochSecond(epochSeconds).atZone(ZoneId.systemDefault()))