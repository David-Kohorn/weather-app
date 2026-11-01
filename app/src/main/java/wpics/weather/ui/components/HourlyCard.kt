/**
 * David Kohorn
 */
package wpics.weather.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import wpics.weather.model.weather.CurrentForecast
import wpics.weather.model.weather.UnitSystem
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.orEmpty
import kotlin.collections.take

@Composable
fun HourlyCard(forecast: CurrentForecast?, units: UnitSystem) {
    val hours = forecast?.list.orEmpty().take(8)
    if (hours.isEmpty()) return

    val formatter = remember { DateTimeFormatter.ofPattern("h a", Locale.getDefault()) }
    val zone = remember { ZoneId.systemDefault() }

    InfoCard("Next 24 hours") {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            items(hours) { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        formatter.format(parseForecastDateTime(item.dt_txt).atZone(zone)),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    item.weather.let {
                        WeatherIcon(it.firstOrNull()?.icon, it.firstOrNull()?.description, Modifier.size(44.dp))
                    }
                    Text(formatTemp(item.main.temp, units), style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}