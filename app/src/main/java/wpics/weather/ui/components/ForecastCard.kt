/**
 * David Kohorn
 */
package wpics.weather.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import wpics.weather.model.weather.CurrentForecast
import wpics.weather.model.weather.UnitSystem
import wpics.weather.ui.theme.OnWeatherText
import java.time.LocalDate
import java.util.Locale
import java.time.format.TextStyle as JavaTextStyle

private data class DayForecast(
    val date: LocalDate,
    val icon: String?,
    val low: Double,
    val high: Double
)

/**
 * Groups 3-hour forecast snapshots by date and returns up to five days.
 * Invalid timestamps are skipped rather than crashing the screen.
 */
private fun buildDailyForecast(
    forecast: CurrentForecast?
): List<DayForecast> {
    val snapshots = forecast?.list.orEmpty().mapNotNull { snapshot ->
        val dateTime = runCatching {
            parseForecastDateTime(snapshot.dt_txt)
        }.getOrNull()

        if (dateTime == null) {
            null
        } else {
            snapshot to dateTime
        }
    }

    return snapshots
        .groupBy { (_, dateTime) -> dateTime.toLocalDate() }
        .toSortedMap()
        .entries
        .take(5)
        .mapNotNull { (date, entries) ->
            val midday = entries.minByOrNull { (_, dateTime) ->
                kotlin.math.abs(dateTime.hour - 12)
            } ?: return@mapNotNull null

            val temperatures = entries.map { (snapshot, _) ->
                snapshot.main.temp
            }

            DayForecast(
                date = date,
                icon = midday.first.weather.firstOrNull()?.icon,
                low = temperatures.minOrNull() ?: return@mapNotNull null,
                high = temperatures.maxOrNull() ?: return@mapNotNull null
            )
        }
}

@Composable
fun ForecastCard(
    forecast: CurrentForecast?,
    units: UnitSystem
) {
    val days = remember(forecast) {
        buildDailyForecast(forecast)
    }

    InfoCard("5-day forecast") {
        if (days.isEmpty()) {
            Text(
                "Forecast is unavailable right now. Please try again later.",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            // A maximum of five rows; the parent screen handles scrolling.
            days.forEach { day ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (day.date == LocalDate.now()) {
                            "Today"
                        } else {
                            day.date.dayOfWeek.getDisplayName(
                                JavaTextStyle.SHORT,
                                Locale.getDefault()
                            )
                        },
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )

                    WeatherIcon(
                        icon = day.icon,
                        contentDescription = "Weather forecast",
                        modifier = Modifier.size(40.dp)
                    )

                    Text(
                        text = formatTemp(day.low, units),
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnWeatherText.copy(alpha = 0.8f),
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(64.dp)
                    )

                    Text(
                        text = formatTemp(day.high, units),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(64.dp)
                    )
                }
            }

            if (days.size < 5) {
                Text(
                    text = "Only ${days.size} of 5 forecast days are available.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnWeatherText.copy(alpha = 0.8f)
                )
            }
        }
    }
}