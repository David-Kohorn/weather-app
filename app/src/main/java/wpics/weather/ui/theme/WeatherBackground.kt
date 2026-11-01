/**
 * David Kohorn
 */
package wpics.weather.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/**
 * Picks a gradient from the OpenWeather icon code. The first two characters
 * are the condition (01 clear, 02-04 clouds, 09/10 rain, 11 storm, 13 snow,
 * 50 mist) and the last character is 'd' (day) or 'n' (night).
 */
fun weatherBackgroundBrush(icon: String?, darkTheme: Boolean): Brush {
    val isDay = icon?.endsWith("d") ?: true

    val (top, bottom) = when (icon?.take(2)) {
        "01" -> if (isDay) ClearDayTop to ClearDayBottom else ClearNightTop to ClearNightBottom
        "09", "10" -> if (isDay) RainDayTop to RainDayBottom else RainNightTop to RainNightBottom
        "11" -> if (isDay) StormDayTop to StormDayBottom else StormNightTop to StormNightBottom
        "13" -> if (isDay) SnowDayTop to SnowDayBottom else SnowNightTop to SnowNightBottom
        "50" -> if (isDay) MistDayTop to MistDayBottom else MistNightTop to MistNightBottom
        else -> if (isDay) CloudsDayTop to CloudsDayBottom else CloudsNightTop to CloudsNightBottom
    }

    // In dark theme, dim the background (this only increases contrast with white text)
    val dim = if (darkTheme) DarkThemeDim else 0f
    return Brush.verticalGradient(
        listOf(lerp(top, Color.Black, dim), lerp(bottom, Color.Black, dim))
    )
}